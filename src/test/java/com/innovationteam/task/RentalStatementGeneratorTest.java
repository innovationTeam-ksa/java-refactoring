package com.innovationteam.task;

import com.innovationteam.task.models.Customer;
import com.innovationteam.task.models.Movie;
import com.innovationteam.task.models.MovieRental;
import com.innovationteam.task.repos.MovieRepository;
import com.innovationteam.task.services.RentalStatementGenerator;
import com.innovationteam.task.services.rentalstrategies.ChildrenMovieRentalStrategy;
import com.innovationteam.task.services.rentalstrategies.NewReleaseMovieRentalStrategy;
import com.innovationteam.task.services.rentalstrategies.RegularMovieRentalStrategy;
import com.innovationteam.task.services.rentalstrategies.RentalStatementStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class RentalStatementGeneratorTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private RentalStatementGenerator rentalStatementGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateGenerateStatement() {
        Movie movie1 = new Movie("F001", "You've Got Mail", "regular");
        Movie movie2 = new Movie("F002", "Matrix", "regular");

        when(applicationContext.getBean(RentalStatementStrategy.class, "regular")).thenReturn(new RegularMovieRentalStrategy());
        when(movieRepository.findById("F001")).thenReturn(Mono.just(movie1));
        when(movieRepository.findById("F002")).thenReturn(Mono.just(movie2));

        Customer customer = new Customer("C001", "John Doe", Arrays.asList(
                new MovieRental("F001", 3),
                new MovieRental("F002", 1)
        ));

        Mono<String> statement = rentalStatementGenerator.generateStatement(customer);

        StepVerifier.create(statement)
                .expectNext("""
                        Rental Record for John Doe
                        \tYou've Got Mail\t3.5
                        \tMatrix\t2.0
                        Amount owed is 5.5
                        You earned 2 frequent points
                        """)
                .verifyComplete();
    }

    @Test
    public void testGenerateGenerateStatementWithEmptyRentals() {
        Customer customer = new Customer("C002", "Jane Doe", List.of());

        Mono<String> statement = rentalStatementGenerator.generateStatement(customer);

        StepVerifier.create(statement)
                .expectNext("Rental Record for Jane Doe\nAmount owed is 0.0\nYou earned 0 frequent points\n")
                .verifyComplete();
    }

    @Test
    public void testGenerateGenerateStatementWithMultipleMovies() {
        Movie movie1 = new Movie("F001", "You've Got Mail", "regular");
        Movie movie2 = new Movie("F002", "Matrix", "regular");
        Movie movie3 = new Movie("F003", "Cars", "children");
        Movie movie4 = new Movie("F004", "Fast & Furious X", "new");

        when(applicationContext.getBean(RentalStatementStrategy.class, "regular")).thenReturn(new RegularMovieRentalStrategy());
        when(applicationContext.getBean(RentalStatementStrategy.class, "children")).thenReturn(new ChildrenMovieRentalStrategy());
        when(applicationContext.getBean(RentalStatementStrategy.class, "new")).thenReturn(new NewReleaseMovieRentalStrategy());

        when(movieRepository.findById("F001")).thenReturn(Mono.just(movie1));
        when(movieRepository.findById("F002")).thenReturn(Mono.just(movie2));
        when(movieRepository.findById("F003")).thenReturn(Mono.just(movie3));
        when(movieRepository.findById("F004")).thenReturn(Mono.just(movie4));

        Customer customer = new Customer("C001", "John Doe", Arrays.asList(
                new MovieRental("F001", 3),
                new MovieRental("F002", 1),
                new MovieRental("F003", 5),
                new MovieRental("F004", 2)
        ));

        Mono<String> statement = rentalStatementGenerator.generateStatement(customer);

        StepVerifier.create(statement)
                .expectNext("""
                        Rental Record for John Doe
                        \tYou've Got Mail\t3.5
                        \tMatrix\t2.0
                        \tCars\t4.5
                        \tFast & Furious X\t6.0
                        Amount owed is 16.0
                        You earned 4 frequent points
                        """)
                .verifyComplete();
    }
}
