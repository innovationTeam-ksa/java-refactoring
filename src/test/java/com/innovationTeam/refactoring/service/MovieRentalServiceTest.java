package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.enums.Code;
import com.innovationTeam.refactoring.model.request.MovieRentalRequestDto;
import com.innovationTeam.refactoring.model.response.MovieRentalResponse;
import com.innovationTeam.refactoring.repository.MovieRentalRepository;
import com.innovationTeam.refactoring.service.impl.MovieRentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static com.innovationTeam.refactoring.utils.Constants.MovieConstants.MOVIE_ID_NOT_NULL_ERROR;
import static com.innovationTeam.refactoring.utils.Constants.UserConstants.CUSTOMER_NOT_FOUND_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieRentalServiceTest {

    @Mock
    private CustomerInterface customerService;

    @Mock
    private MovieInterface movieService;

    @Mock
    private MovieRentalRepository movieRentalRepository;

    @InjectMocks
    private MovieRentalService movieRentalService;

    private Customer customer;
    private Movie movie;
    private MovieRentalRequestDto movieRentalRequestDto;
    private MovieRental movieRental;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);

        movie = new Movie();
        movie.setCode(Code.REGULAR);
        movie.setTitle("Movie Title");
        movie.setId(1L);

        movieRentalRequestDto = new MovieRentalRequestDto();
        movieRentalRequestDto.setCustomerId(1L);
        movieRentalRequestDto.setMovieId(1L);
        movieRentalRequestDto.setDays(5L);

        movieRental = new MovieRental();
        movieRental.setCustomer(customer);
        movieRental.setMovie(movie);
        movieRental.setDays(5L);
    }

    @Test
    void rentMovie_ShouldRentMovieSuccessfully() {
        when(customerService.getCustomerById(anyLong())).thenReturn(Mono.just(customer));
        when(movieService.getMovieById(anyLong())).thenReturn(Mono.just(movie));
        when(movieRentalRepository.save(any(MovieRental.class))).thenReturn(movieRental);

        Mono<MovieRentalResponse> responseMono = movieRentalService.rentMovie(movieRentalRequestDto);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.getMovie().getCode().equals(Code.REGULAR.toString()) && response.getDays() == movieRental.getDays())
                .verifyComplete();

        verify(customerService, times(1)).getCustomerById(1L);
        verify(movieService, times(1)).getMovieById(1L);
        verify(movieRentalRepository, times(1)).save(any(MovieRental.class));
    }

    @Test
    void rentMovie_ShouldThrowCustomerNotFoundException() {
        when(customerService.getCustomerById(anyLong())).thenReturn(Mono.empty());
        when(movieService.getMovieById(anyLong())).thenReturn(Mono.just(movie));

        Mono<MovieRentalResponse> responseMono = movieRentalService.rentMovie(movieRentalRequestDto);
        responseMono.subscribe();
        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains(String.format(CUSTOMER_NOT_FOUND_ERROR, movieRentalRequestDto.getCustomerId())))
                .verify();

        verify(customerService, times(1)).getCustomerById(1L);
        verify(movieService, times(1)).getMovieById(anyLong());
        verify(movieRentalRepository, never()).save(any(MovieRental.class));
    }

    @Test
    void rentMovie_ShouldThrowMovieNotFoundException() {
        when(customerService.getCustomerById(anyLong())).thenReturn(Mono.just(customer));
        when(movieService.getMovieById(anyLong())).thenReturn(Mono.empty());

        Mono<MovieRentalResponse> responseMono = movieRentalService.rentMovie(movieRentalRequestDto);
        responseMono.subscribe();
        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains(MOVIE_ID_NOT_NULL_ERROR))
                .verify();

        verify(customerService, times(1)).getCustomerById(1L);
        verify(movieService, times(1)).getMovieById(1L);
        verify(movieRentalRepository, never()).save(any(MovieRental.class));
    }

    @Test
    void getRentalsByCustomer_ShouldReturnRentalsForCustomer() {
        customer.setRentals(Collections.singletonList(movieRental));

        when(customerService.getCustomerById(anyLong())).thenReturn(Mono.just(customer));

        Flux<MovieRentalResponse> responseFlux = movieRentalService.getRentalsByCustomer(1L);

        StepVerifier.create(responseFlux)
                .expectNextMatches(response -> response.getMovie().getCode().equals(Code.REGULAR.toString()) && response.getDays() == response.getDays())
                .verifyComplete();

        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    void getRentalsByCustomer_ShouldHandleCustomerNotFound() {
        when(customerService.getCustomerById(anyLong())).thenReturn(Mono.empty());

        Flux<MovieRentalResponse> responseFlux = movieRentalService.getRentalsByCustomer(1L);

        StepVerifier.create(responseFlux)
                .expectNextCount(0)
                .verifyComplete();

        verify(customerService, times(1)).getCustomerById(1L);
    }
}
