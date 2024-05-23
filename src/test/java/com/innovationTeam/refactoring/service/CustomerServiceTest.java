package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.enums.Code;
import com.innovationTeam.refactoring.exception.CustomerNotFoundException;
import com.innovationTeam.refactoring.mapper.CustomerMapper;
import com.innovationTeam.refactoring.model.Statement;
import com.innovationTeam.refactoring.model.request.CustomerRequestDto;
import com.innovationTeam.refactoring.model.response.CustomerResponse;
import com.innovationTeam.refactoring.repository.CustomerRepository;
import com.innovationTeam.refactoring.service.StatementCalculationInterface;
import com.innovationTeam.refactoring.service.StatementPrintingInterface;
import com.innovationTeam.refactoring.service.impl.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StatementCalculationInterface statementCalculationService;

    @Mock
    private StatementPrintingInterface statementPrintingService;

    @InjectMocks
    private CustomerService customerService;
    private Statement statement;
    private MovieRental rental;
    private Customer customer;
    private CustomerRequestDto customerRequestDto;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Customer name");

        customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setName("Customer name");

        rental = new MovieRental();
        rental.setId(1L);
        rental.setMovie(new Movie(1L , "title" , Code.NEW));
        rental.setCustomer(customer);
        rental.setDays(23L);

        customer.setRentals(Arrays.asList(rental));
        statement = new Statement();
        statement.setCustomerName("Customer name");
        statement.setRentals(Collections.singletonList(rental));
        statement.setTotalAmount(10.0);
    }

    @Test
    void getAllCustomers_ShouldReturnCustomers() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));

        Flux<CustomerResponse> customerFlux = customerService.getAllCustomers();

        StepVerifier.create(customerFlux)
                .expectNextMatches(customerResponse -> customerResponse.getName().equals("Customer name"))
                .verifyComplete();

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void getCustomerById_ShouldReturnCustomer() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        Mono<Customer> customerMono = customerService.getCustomerById(1L);

        StepVerifier.create(customerMono)
                .expectNextMatches(c -> c.getName().equals("Customer name"))
                .verifyComplete();

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void getCustomerById_ShouldReturnEmpty_WhenCustomerNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Mono<Customer> customerMono = customerService.getCustomerById(1L);

        StepVerifier.create(customerMono)
                .verifyComplete();

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void getCustomerById_ShouldThrowException_WhenIdIsNull() {
        Mono<Customer> customerMono = customerService.getCustomerById(null);

        StepVerifier.create(customerMono)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(customerRepository, never()).findById(null);
    }

    @Test
    void generateCustomerStatement_ShouldReturnStatement() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(statementCalculationService.calculateStatement(anyList(), anyString())).thenReturn(Mono.just(statement));
        when(statementPrintingService.printStatement(any())).thenReturn("printed statement");

        Mono<String> statementMono = customerService.generateCustomerStatement(1L);

        StepVerifier.create(statementMono)
                .expectNext("printed statement")
                .verifyComplete();

        verify(customerRepository, times(1)).findById(1L);
        verify(statementCalculationService, times(1)).calculateStatement(anyList(), anyString());
        verify(statementPrintingService, times(1)).printStatement(any());
    }

    @Test
    void generateCustomerStatement_ShouldThrowCustomerNotFoundException() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Mono<String> statementMono = customerService.generateCustomerStatement(1L);

        StepVerifier.create(statementMono)
                .expectError(CustomerNotFoundException.class)
                .verify();

        verify(customerRepository, times(1)).findById(1L);
        verify(statementCalculationService, never()).calculateStatement(any(), any());
        verify(statementPrintingService, never()).printStatement(any());
    }

    @Test
    void createCustomer_ShouldReturnCreatedCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Mono<Customer> customerMono = customerService.createCustomer(customerRequestDto);

        StepVerifier.create(customerMono)
                .expectNextMatches(c -> c.getName().equals("Customer name"))
                .verifyComplete();

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void createCustomer_ShouldThrowException_WhenCustomerRequestDtoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(null));
    }

    @Test
    void createCustomer_ShouldThrowException_WhenCustomerNameIsEmpty() {
        customerRequestDto.setName("");

        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(customerRequestDto));
    }
}
