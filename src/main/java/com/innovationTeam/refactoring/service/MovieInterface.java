package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieInterface {
    Movie saveMovie(Movie movie);

    Optional<Movie> getMovieById(Long id);

    List<Movie> getAllMovies();
}
