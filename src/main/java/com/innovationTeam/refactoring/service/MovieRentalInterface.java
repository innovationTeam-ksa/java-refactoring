package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.model.request.MovieRentalRequestDto;
import com.innovationTeam.refactoring.model.response.MovieRentalResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieRentalInterface {
    Mono<MovieRentalResponse> rentMovie(MovieRentalRequestDto movieRentalRequestDto);

    Flux<MovieRentalResponse> getRentalsByCustomer(Long customerId);
}
