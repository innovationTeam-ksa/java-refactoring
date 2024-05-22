package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.Movie;

import java.util.List;

public interface MovieInterface {
    Movie saveMovie(Movie movie);

    Movie getMovieById(Long id);

    List<Movie> getAllMovies();
}
