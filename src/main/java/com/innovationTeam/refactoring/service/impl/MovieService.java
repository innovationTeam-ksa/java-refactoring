package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.mapper.MovieMapper;
import com.innovationTeam.refactoring.model.request.MovieRequestDto;
import com.innovationTeam.refactoring.model.response.MovieResponse;
import com.innovationTeam.refactoring.repository.MovieRepository;
import com.innovationTeam.refactoring.service.MovieInterface;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.innovationTeam.refactoring.utils.Constants.MovieConstants.MOVIE_ID_NOT_NULL_ERROR;

@Service
@Transactional
public class MovieService implements MovieInterface {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie saveMovie(MovieRequestDto movieRequest) {
        logger.info("Saving movie: {}", movieRequest);

        Movie movie = MovieMapper.INSTANCE.mapToMovie(movieRequest);
        Movie savedMovie = movieRepository.save(movie);

        logger.info("Saved movie: {}", savedMovie);
        return savedMovie;
    }

    @Override
    public Optional<Movie> getMovieById(Long id) {
        logger.info("Fetching movie by ID: {}", id);

        if (id == null) {
            throw new IllegalArgumentException(MOVIE_ID_NOT_NULL_ERROR);
        }

        Optional<Movie> movie = movieRepository.findById(id);

        logger.info("Fetched movie: {}", movie.orElse(null));
        return movie;
    }

    @Override
    public List<MovieResponse> getAllMovies() {
        logger.info("Fetching all movies");

        List<Movie> movies = movieRepository.findAll();
        List<MovieResponse> movieResponses = movies.stream()
                .map(MovieMapper.INSTANCE::mapToMovieResponse)
                .collect(Collectors.toList());

        logger.info("Fetched {} movies", movies.size());
        return movieResponses;
    }
}
