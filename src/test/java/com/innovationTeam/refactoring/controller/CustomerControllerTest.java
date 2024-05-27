package com.innovationTeam.refactoring.controller;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.exception.CustomerNotFoundException;
import com.innovationTeam.refactoring.model.request.CustomerRequestDto;
import com.innovationTeam.refactoring.model.response.ApiResponseModel;
import com.innovationTeam.refactoring.model.response.CustomerResponse;
import com.innovationTeam.refactoring.service.impl.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCustomersSuccess() {
        CustomerResponse customerResponse = new CustomerResponse();
        when(customerService.getAllCustomers()).thenReturn(Flux.just(customerResponse));

        Mono<ResponseEntity<ApiResponseModel<List<CustomerResponse>>>> response = customerController.getAllCustomers();

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertEquals(1, entity.getBody().getData().size());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testGetAllCustomersInternalServerError() {
        when(customerService.getAllCustomers()).thenReturn(Flux.error(new RuntimeException("Internal error")));

        Mono<ResponseEntity<ApiResponseModel<List<CustomerResponse>>>> response = customerController.getAllCustomers();

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entity.getStatusCode());
                    assertEquals("Failed to fetch customers", entity.getBody().getError().getCode());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testCreateCustomerSuccess() {
        CustomerRequestDto customerRequest = new CustomerRequestDto();
        when(customerService.createCustomer(any(CustomerRequestDto.class))).thenReturn(Mono.just(new Customer()));

        Mono<ResponseEntity<ApiResponseModel<String>>> response = customerController.createCustomer(customerRequest);

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.CREATED, entity.getStatusCode());
                    assertEquals("User added successfully", entity.getBody().getData());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testCreateCustomerNull() {
        CustomerRequestDto customerRequest = new CustomerRequestDto();
        when(customerService.createCustomer(any(CustomerRequestDto.class))).thenReturn(Mono.empty());

        Mono<ResponseEntity<ApiResponseModel<String>>> response = customerController.createCustomer(customerRequest);

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertEquals("FAILED_CREATE_ERROR", entity.getBody().getError().getCode());
                    return true;
                })
                .verifyComplete();
    }


    @Test
    public void testCreateCustomerInternalServerError() {
        CustomerRequestDto customerRequest = new CustomerRequestDto();
        when(customerService.createCustomer(any(CustomerRequestDto.class))).thenReturn(Mono.error(new RuntimeException("Internal error")));

        Mono<ResponseEntity<ApiResponseModel<String>>> response = customerController.createCustomer(customerRequest);

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entity.getStatusCode());
                    assertEquals("Failed to create customer", entity.getBody().getError().getCode());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testGetCustomerStatementSuccess() {
        Long customerId = 1L;
        String statement = "Sample statement";
        when(customerService.generateCustomerStatement(customerId)).thenReturn(Mono.just(statement));

        Mono<ResponseEntity<ApiResponseModel<String>>> response = customerController.getCustomerStatement(customerId);

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertEquals(statement, entity.getBody().getData());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testGetCustomerStatementCustomerNotFound() {
        Long customerId = 1L;
        when(customerService.generateCustomerStatement(customerId)).thenReturn(Mono.error(new CustomerNotFoundException("Customer not found")));

        Mono<ResponseEntity<ApiResponseModel<String>>> response = customerController.getCustomerStatement(customerId);

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
                    assertEquals("Customer not found", entity.getBody().getError().getCode());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testGetCustomerStatementInternalServerError() {
        Long customerId = 1L;
        when(customerService.generateCustomerStatement(customerId)).thenReturn(Mono.error(new RuntimeException("Internal error")));

        Mono<ResponseEntity<ApiResponseModel<String>>> response = customerController.getCustomerStatement(customerId);

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entity.getStatusCode());
                    assertEquals("Failed to fetch customer statement", entity.getBody().getError().getCode());
                    return true;
                })
                .verifyComplete();
    }
}
