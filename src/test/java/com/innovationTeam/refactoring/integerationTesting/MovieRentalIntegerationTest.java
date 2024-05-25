package com.innovationTeam.refactoring.integerationTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovationTeam.refactoring.controller.MovieRentalController;
import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.enums.Code;
import com.innovationTeam.refactoring.model.request.CustomerRequestDto;
import com.innovationTeam.refactoring.model.request.MovieRentalRequestDto;
import com.innovationTeam.refactoring.repository.MovieRentalRepository;
import com.innovationTeam.refactoring.service.impl.CustomerService;
import com.innovationTeam.refactoring.service.impl.MovieRentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class MovieRentalIntegerationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRentalService movieRentalService;

    @MockBean
    private MovieRentalRepository movieRentalRepository;

    @MockBean
    private CustomerService customerService;

    @InjectMocks
    private MovieRentalController movieRentalController;


    private CustomerRequestDto customerRequestDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setName("Customer name");
    }


    @Test
    void testRentMovieCustomerNotFound() throws Exception {
        MovieRentalRequestDto rentalRequestDto = new MovieRentalRequestDto();
        rentalRequestDto.setCustomerId(1L);
        rentalRequestDto.setMovieId(1L);

        when(customerService.getCustomerById(1L)).thenReturn(Mono.empty());

        mockMvc.perform(post("/v1/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rentalRequestDto)))
                .andExpect(status().isOk());

    }

    @Test
    void testGetRentalsByCustomerNoRentalsFound() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Customer name");

        Movie movie = new Movie(1L, "movieName", Code.NEW);

        // Mock service method to return empty list
        when(customerService.getCustomerById(1L)).thenReturn(Mono.just(new Customer(1L, "customerName", List.of(new MovieRental(1L, movie, customer, 20L)))));

        // Perform GET request
        mockMvc.perform(get("/v1/rentals/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void testGetRentalsByCustomerNotFound() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(Mono.empty());

        mockMvc.perform(get("/v1/rentals/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
