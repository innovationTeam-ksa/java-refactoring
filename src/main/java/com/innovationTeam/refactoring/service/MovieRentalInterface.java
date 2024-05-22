package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.MovieRental;

import java.util.List;

public interface MovieRentalInterface {

      MovieRental rentMovie(Long movieId, Long customerId, int days);

      List<MovieRental> getRentalsByCustomer(Long customerId);
}
