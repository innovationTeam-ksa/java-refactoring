package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.mapper.MovieMapper;
import com.innovationTeam.refactoring.model.request.MovieRequestDto;
import com.innovationTeam.refactoring.model.response.MovieResponse;
import com.innovationTeam.refactoring.repository.MovieRepository;
import com.innovationTeam.refactoring.service.MovieInterface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.innovationTeam.refactoring.utils.Constants.MovieConstants.MOVIE_ID_NOT_NULL_ERROR;

@Service
@Transactional
public class MovieService implements MovieInterface {
    @Autowired
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie saveMovie(MovieRequestDto movieRequest) {
        Movie movie = MovieMapper.INSTANCE.mapToMovie(movieRequest);
        return movieRepository.save(movie);
    }

    @Override
    public Optional<Movie> getMovieById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(MOVIE_ID_NOT_NULL_ERROR);
        }
        Optional<Movie> movie = movieRepository.findById(id);
        return movie;
    }

    @Override
    public List<MovieResponse> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(MovieMapper.INSTANCE::mapToMovieResponse)
                .collect(Collectors.toList());
    }
}
