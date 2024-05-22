package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.model.request.MovieRequestDto;
import com.innovationTeam.refactoring.model.response.MovieResponse;

import java.util.List;
import java.util.Optional;

public interface MovieInterface {
    Movie saveMovie(MovieRequestDto movieRequest);

    Optional<Movie> getMovieById(Long id);

    List<MovieResponse> getAllMovies();
}
