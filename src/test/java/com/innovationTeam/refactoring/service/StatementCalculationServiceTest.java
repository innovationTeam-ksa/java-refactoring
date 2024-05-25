package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.enums.Code;
import com.innovationTeam.refactoring.model.Statement;
import com.innovationTeam.refactoring.repository.MovieRentalRepository;
import com.innovationTeam.refactoring.service.impl.StatementCalculationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class StatementCalculationServiceTest {

    @InjectMocks
    private StatementCalculationService statementCalculationService;

    @Mock
    private MovieRentalRepository movieRentalRepository;

    @Test
    void calculateStatement_RegularMovie() {
        Movie movie = new Movie(1L, "Movie 1", Code.REGULAR);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Customer name");
        MovieRental rental = new MovieRental(1L, movie, customer, 3L);
        List<MovieRental> rentalList = List.of(rental);

        Mono<Statement> statementMono = statementCalculationService.calculateStatement(rentalList, customer.getName());

        StepVerifier.create(statementMono)
                .expectNextMatches(statement ->
                        statement.getTotalAmount() == 3.5 &&
                                statement.getCustomerName().equals(customer.getName()) &&
                                statement.getRentalInfoList().size() == 1 &&
                                statement.getRentalInfoList().get(0).getMovieTitle().equals("Movie 1") &&
                                statement.getRentalInfoList().get(0).getAmount() == 3.5)
                .verifyComplete();

    }

    @Test
    void calculateStatement_NewMovie() {
        Movie movie = new Movie(1L, "Movie 2", Code.NEW);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Customer name");
        MovieRental rental = new MovieRental(1L, movie, customer, 2L);
        List<MovieRental> rentalList = List.of(rental);
        String customerName = "Jane Doe";

        Mono<Statement> statementMono = statementCalculationService.calculateStatement(rentalList, customerName);

        StepVerifier.create(statementMono)
                .expectNextMatches(statement ->
                        statement.getTotalAmount() == 6 &&
                                statement.getCustomerName().equals(customerName) &&
                                statement.getRentalInfoList().size() == 1 &&
                                statement.getRentalInfoList().get(0).getMovieTitle().equals("Movie 2") &&
                                statement.getRentalInfoList().get(0).getAmount() == 6)
                .verifyComplete();

    }

    @Test
    void calculateStatement_ChildrensMovie() {
        Movie movie = new Movie(1L, "Movie 3", Code.CHILDRENS);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Customer name");
        MovieRental rental = new MovieRental(1L, movie, customer, 4L);
        List<MovieRental> rentalList = List.of(rental);
        String customerName = "Jack Smith";

        Mono<Statement> statementMono = statementCalculationService.calculateStatement(rentalList, customerName);

        StepVerifier.create(statementMono)
                .expectNextMatches(statement ->
                        statement.getTotalAmount() == 3.0 &&
                                statement.getCustomerName().equals(customerName) &&
                                statement.getRentalInfoList().size() == 1 &&
                                statement.getRentalInfoList().get(0).getMovieTitle().equals("Movie 3") &&
                                statement.getRentalInfoList().get(0).getAmount() == 3.0)
                .verifyComplete();

    }

    @Test
    void calculateStatement_UnknownMovieType() {
        Movie movie = new Movie(1L, "Movie 4", null);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Customer name");
        MovieRental rental = new MovieRental(1L, movie, customer, 5L);
        List<MovieRental> rentalList = List.of(rental);
        String customerName = "Unknown Customer";

        Mono<Statement> statementMono = statementCalculationService.calculateStatement(rentalList, customerName);

        StepVerifier.create(statementMono)
                .expectErrorMatches(throwable -> throwable instanceof NullPointerException)
                .verify();

    }


}
