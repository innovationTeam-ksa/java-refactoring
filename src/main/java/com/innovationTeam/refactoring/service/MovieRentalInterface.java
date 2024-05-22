package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.model.request.MovieRentalRequestDto;
import com.innovationTeam.refactoring.model.response.MovieRentalResponse;

import java.util.List;

public interface MovieRentalInterface {
    MovieRentalResponse rentMovie(MovieRentalRequestDto movieRentalRequestDto);

    List<MovieRentalResponse> getRentalsByCustomer(Long customerId);
}
