package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.service.MovieRentalInterface;

import java.util.List;

public class MovieRentalServiceImpl implements MovieRentalInterface {
    @Override
    public MovieRental rentMovie(Long movieId, Long customerId, int days) {
        return null;
    }

    @Override
    public List<MovieRental> getRentalsByCustomer(Long customerId) {
        return null;
    }
}
