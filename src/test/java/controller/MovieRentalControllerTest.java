package controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import repository.MovieRentalRepository;

@WebFluxTest(MovieRentalController.class)
public class MovieRentalControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MovieRentalRepository movieRentalRepository;

    @BeforeEach
    void setUp() {
        // Clear existing movie rentals and insert test data
        movieRentalRepository.deleteAll()
                .thenMany(
                        Mono.just(new MovieRental("1", "1", "1", LocalDate.now())),
                        Mono.just(new MovieRental("2", "2", "2", LocalDate.now())),
                        Mono.just(new MovieRental("3", "3", "3", LocalDate.now()))
                                .flatMap(movieRentalRepository::save)
                )
                .blockLast(); // Block until all operations complete for setup
    }

    @Test
    void testGetAllMovieRentals() {
        webTestClient.get().uri("/api/movie-rentals")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovieRental.class)
                .hasSize(3);
    }

}
