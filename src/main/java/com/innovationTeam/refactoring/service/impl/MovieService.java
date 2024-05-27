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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.innovationTeam.refactoring.utils.Constants.MOVIE_REQUEST_DTO_NULL_ERROR;
import static com.innovationTeam.refactoring.utils.Constants.MovieConstants.MOVIE_ID_NOT_NULL_ERROR;

@Service
@Transactional
public class MovieService implements MovieInterface {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public Mono<Movie> saveMovie(MovieRequestDto movieRequest) {
        if (movieRequest == null)
            return Mono.error(new IllegalArgumentException(MOVIE_REQUEST_DTO_NULL_ERROR));

        Movie movie = MovieMapper.INSTANCE.mapToMovie(movieRequest);
        return Mono.fromCallable(() -> movieRepository.save(movie))
                .doOnSuccess(savedMovie -> logger.info("Saved movie: {}", savedMovie));
    }

    @Override
    public Mono<Movie> getMovieById(Long id) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException(MOVIE_ID_NOT_NULL_ERROR));
        }

        return Mono.defer(() -> {
            Optional<Movie> movieOptional = movieRepository.findById(id);
            logger.info("Fetched movie: {}", movieOptional.orElse(null));
            return Mono.justOrEmpty(movieOptional);
        });
    }

    @Override
    public Flux<MovieResponse> getAllMovies() {
        return Flux.defer(() -> {
            List<Movie> movies = movieRepository.findAll();
            List<MovieResponse> movieResponses = movies.stream()
                    .map(MovieMapper.INSTANCE::mapToMovieResponse)
                    .collect(Collectors.toList());
            logger.info("Fetched {} movies", movies.size());
            return Flux.fromIterable(movieResponses);
        });
    }
}
