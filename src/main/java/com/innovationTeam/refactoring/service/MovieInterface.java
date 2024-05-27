package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.model.request.MovieRequestDto;
import com.innovationTeam.refactoring.model.response.MovieResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieInterface {
    Mono<Movie> saveMovie(MovieRequestDto movieRequest);

    Mono<Movie> getMovieById(Long id);

    Flux<MovieResponse> getAllMovies();
}
