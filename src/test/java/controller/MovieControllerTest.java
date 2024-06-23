package controller;

import entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import repository.MovieRepository;

@WebFluxTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        // Clear existing movies and insert test data
        movieRepository.deleteAll()
                .thenMany(
                        Mono.just(new Movie("1", "The Matrix", "Sci-Fi")),
                        Mono.just(new Movie("2", "Inception", "Thriller")),
                        Mono.just(new Movie("3", "The Shawshank Redemption", "Drama"))
                                .flatMap(movieRepository::save)
                )
                .blockLast(); // Block until all operations complete for setup
    }

    @Test
    void testGetAllMovies() {
        webTestClient.get().uri("/api/movies")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Movie.class)
                .hasSize(3);
    }

}
