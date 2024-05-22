package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.repository.MovieRepository;
import com.innovationTeam.refactoring.service.MovieInterface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieServiceImpl implements MovieInterface {
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie getMovieById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
}
