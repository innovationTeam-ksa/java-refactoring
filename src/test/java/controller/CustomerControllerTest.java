package controller;

import entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import repository.CustomerRepository;

@WebFluxTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        // Clear existing customers and insert test data
        customerRepository.deleteAll()
                .thenMany(
                        Mono.just(new Customer("1", "Alice")),
                        Mono.just(new Customer("2", "Bob")),
                        Mono.just(new Customer("3", "Charlie"))
                                .flatMap(customerRepository::save)
                )
                .blockLast(); // Block until all operations complete for setup
    }

    @Test
    void testGetAllCustomers() {
        webTestClient.get().uri("/api/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .hasSize(3);
    }

}
