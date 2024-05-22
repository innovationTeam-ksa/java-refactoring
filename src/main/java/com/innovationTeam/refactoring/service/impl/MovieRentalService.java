package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.repository.MovieRentalRepository;
import com.innovationTeam.refactoring.service.CustomerInterface;
import com.innovationTeam.refactoring.service.MovieInterface;
import com.innovationTeam.refactoring.service.MovieRentalInterface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.innovationTeam.refactoring.utils.Constants.MovieConstants.MOVIE_ID_NOT_NULL_ERROR;
import static com.innovationTeam.refactoring.utils.Constants.UserConstants.CUSTOMER_NOT_FOUND_ERROR;

@Service
@Transactional
public class MovieRentalService implements MovieRentalInterface {
    @Autowired
    CustomerInterface customerService;
    @Autowired
    MovieInterface movieService;
    @Autowired
    MovieRentalRepository movieRentalRepository;

    @Override
    public MovieRental rentMovie(Long movieId, Long customerId, int days) {

        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(CUSTOMER_NOT_FOUND_ERROR, customerId)));
        Movie movie = movieService.getMovieById(movieId)
                .orElseThrow(() -> new IllegalArgumentException(MOVIE_ID_NOT_NULL_ERROR));


        MovieRental movieRental = new MovieRental();
        movieRental.setMovie(movie);
        movieRental.setCustomer(customer);
        movieRental.setDays(days);

        movieRentalRepository.save(movieRental);
        return movieRental;
    }

    @Override
    public List<MovieRental> getRentalsByCustomer(Long customerId) {
        List<MovieRental> movieRentalList = new ArrayList<>();
        Optional<Customer> customer = customerService.getCustomerById(customerId);
        if (customer.isPresent()) {
            movieRentalList = customer.get().getRentals();
        }
        return movieRentalList;
    }
}
