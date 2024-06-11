package com.innovationteam.task;

import com.innovationteam.task.models.Customer;
import com.innovationteam.task.models.MovieRental;
import com.innovationteam.task.repos.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Import(TestConfig.class)
public class RentalIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll().block();

        Customer customer1 = new Customer("C001", "John Doe", Arrays.asList(
                new MovieRental("F001", 3),
                new MovieRental("F002", 1)
        ));

        customerRepository.save(customer1).block();
    }

    @Test
    void testGetRentalInfo() {
        webTestClient.get().uri("/api/v1/rentals/C001")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> {
                    assertThat(response).contains("Rental Record for John Doe");
                    assertThat(response).contains("You've Got Mail");
                    assertThat(response).contains("Matrix");
                });
    }

    @Test
    void testCustomerNotFound() {
        webTestClient.get().uri("/api/v1/rentals/C002")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(response -> assertThat(response).contains("Customer with id C002 not found"));
    }
}
