package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.mapper.MovieMapper;
import com.innovationTeam.refactoring.model.request.MovieRentalRequestDto;
import com.innovationTeam.refactoring.model.response.MovieRentalResponse;
import com.innovationTeam.refactoring.repository.MovieRentalRepository;
import com.innovationTeam.refactoring.service.CustomerInterface;
import com.innovationTeam.refactoring.service.MovieInterface;
import com.innovationTeam.refactoring.service.MovieRentalInterface;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static com.innovationTeam.refactoring.utils.Constants.MovieConstants.MOVIE_ID_NOT_NULL_ERROR;
import static com.innovationTeam.refactoring.utils.Constants.UserConstants.CUSTOMER_NOT_FOUND_ERROR;

@Service
@Transactional
public class MovieRentalService implements MovieRentalInterface {
    private static final Logger logger = LoggerFactory.getLogger(MovieRentalService.class);

    @Autowired
    CustomerInterface customerService;
    @Autowired
    MovieInterface movieService;
    @Autowired
    MovieRentalRepository movieRentalRepository;

    @Override
    public Mono<MovieRentalResponse> rentMovie(MovieRentalRequestDto movieRentalRequestDto) {
        logger.info("Renting movie with ID {} for customer with ID {}", movieRentalRequestDto.getMovieId(), movieRentalRequestDto.getCustomerId());

        Mono<Customer> customerMono = customerService.getCustomerById(movieRentalRequestDto.getCustomerId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException(String.format(CUSTOMER_NOT_FOUND_ERROR, movieRentalRequestDto.getCustomerId()))));

        Mono<Movie> movieMono = movieService.getMovieById(movieRentalRequestDto.getMovieId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException(MOVIE_ID_NOT_NULL_ERROR)));

        return Mono.zip(customerMono, movieMono)
                .flatMap(tuple -> {
                    Customer customer = tuple.getT1();
                    Movie movie = tuple.getT2();

                    MovieRental movieRental = new MovieRental();
                    movieRental.setMovie(movie);
                    movieRental.setCustomer(customer);
                    movieRental.setDays(movieRentalRequestDto.getDays());

                    movieRentalRepository.save(movieRental);

                    return Mono.just(MovieMapper.INSTANCE.mapToMovieRentalResponse(movieRental));
                });
    }

    @Override
    public Flux<MovieRentalResponse> getRentalsByCustomer(Long customerId) {
        logger.info("Fetching rentals for customer with ID {}", customerId);

        return customerService.getCustomerById(customerId)
                .flatMapMany(customer -> {
                    List<MovieRental> movieRentalList = customer.getRentals();

                    List<MovieRentalResponse> movieRentalResponses = movieRentalList.stream()
                            .map(MovieMapper.INSTANCE::mapToMovieRentalResponse)
                            .collect(Collectors.toList());

                    logger.info("Fetched {} rentals for customer with ID {}", movieRentalResponses.size(), customerId);
                    return Flux.fromIterable(movieRentalResponses);
                })
                .switchIfEmpty(Flux.defer(() -> {
                    logger.warn("Customer with ID {} not found", customerId);
                    return Flux.empty();
                }));
    }
}
