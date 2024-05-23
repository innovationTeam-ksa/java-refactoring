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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public MovieRentalResponse rentMovie(MovieRentalRequestDto movieRentalRequestDto) {
        logger.info("Renting movie with ID {} for customer with ID {}", movieRentalRequestDto.getMovieId(), movieRentalRequestDto.getCustomerId());
        Customer customer = customerService.getCustomerById(movieRentalRequestDto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException(String.format(CUSTOMER_NOT_FOUND_ERROR, movieRentalRequestDto.getCustomerId())));
        Movie movie = movieService.getMovieById(movieRentalRequestDto.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException(MOVIE_ID_NOT_NULL_ERROR));

        MovieRental movieRental = new MovieRental();
        movieRental.setMovie(movie);
        movieRental.setCustomer(customer);
        movieRental.setDays(movieRentalRequestDto.getDays());

        movieRentalRepository.save(movieRental);

        return MovieMapper.INSTANCE.mapToMovieRentalResponse(movieRental);
    }

    @Override
    public List<MovieRentalResponse> getRentalsByCustomer(Long customerId) {
        logger.info("Fetching rentals for customer with ID {}", customerId);
        List<MovieRental> movieRentalList = new ArrayList<>();
        Optional<Customer> customer = customerService.getCustomerById(customerId);
        if (customer.isPresent()) {
            movieRentalList = customer.get().getRentals();
        }

        List<MovieRentalResponse> movieRentalResponses = movieRentalList.stream()
                .map(MovieMapper.INSTANCE::mapToMovieRentalResponse)
                .collect(Collectors.toList());

        logger.info("Fetched {} rentals for customer with ID {}", movieRentalResponses.size(), customerId);
        return movieRentalResponses;
    }

}
