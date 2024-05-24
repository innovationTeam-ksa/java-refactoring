package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.enums.Code;
import com.innovationTeam.refactoring.model.request.MovieRequestDto;
import com.innovationTeam.refactoring.model.response.MovieResponse;
import com.innovationTeam.refactoring.repository.MovieRepository;
import com.innovationTeam.refactoring.service.impl.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void saveMovie_ValidRequest_ReturnsSavedMovie() {
         MovieRequestDto requestDto = new MovieRequestDto("Movie Title", Code.REGULAR);

        Movie savedMovie = new Movie(1L,"Movie Title",Code.REGULAR);
        when(movieRepository.save(any())).thenReturn(savedMovie);

         Mono<Movie> savedMovieMono = movieService.saveMovie(requestDto);

         StepVerifier.create(savedMovieMono)
                .expectNextMatches(movie -> {
                    assertThat(movie.getId()).isEqualTo(1L);
                    assertThat(movie.getTitle()).isEqualTo("Movie Title");
                    assertThat(movie.getCode()).isEqualTo(Code.REGULAR);
                    return true;
                })
                .verifyComplete();

        verify(movieRepository, times(1)).save(any());
    }

    @Test
    void saveMovie_NullRequest_ThrowsIllegalArgumentException() {
         MovieRequestDto nullRequestDto = null;

         Mono<Movie> savedMovieMono = movieService.saveMovie(nullRequestDto);

         StepVerifier.create(savedMovieMono)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(movieRepository, never()).save(any());
    }
    @Test
    void getMovieById_ValidId_ReturnsMovie() {
         long movieId = 1L;
        Movie movie = new Movie(movieId, "Movie Title", Code.REGULAR);
        when(movieRepository.findById(movieId)).thenReturn(java.util.Optional.of(movie));

         Mono<Movie> movieMono = movieService.getMovieById(movieId);

         StepVerifier.create(movieMono)
                .expectNextMatches(savedMovie -> {
                    assertThat(savedMovie.getId()).isEqualTo(movieId);
                    assertThat(savedMovie.getTitle()).isEqualTo("Movie Title");
                    assertThat(savedMovie.getCode()).isEqualTo(Code.REGULAR);
                     return true;
                })
                .verifyComplete();

        verify(movieRepository, times(1)).findById(movieId);
    }


    @Test
    void getMovieById_NullId_ThrowsIllegalArgumentException() {
        Long nullId = null;

         Mono<Movie> movieMono = movieService.getMovieById(nullId);

         StepVerifier.create(movieMono)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(movieRepository, never()).save(any());
    }

    @Test
    void getMovieById_InvalidId_ReturnsEmptyMono() {
         long invalidMovieId = 100L;
        when(movieRepository.findById(invalidMovieId)).thenReturn(java.util.Optional.empty());

         Mono<Movie> movieMono = movieService.getMovieById(invalidMovieId);

         StepVerifier.create(movieMono)
                .expectNextCount(0)
                .verifyComplete();

        verify(movieRepository, times(1)).findById(invalidMovieId);
    }
     @Test
    void getAllMovies_ReturnsAllMovies() {
         List<Movie> movies = Arrays.asList(
                new Movie(1L, "Movie 1", Code.REGULAR),
                new Movie(2L, "Movie 2", Code.NEW),
                new Movie(3L, "Movie 3", Code.CHILDRENS)
        );
        when(movieRepository.findAll()).thenReturn(movies);

         Flux<MovieResponse> movieResponseFlux = movieService.getAllMovies();

         StepVerifier.create(movieResponseFlux)
                .expectNextCount(3)
                .verifyComplete();

        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void getAllMovies_EmptyList_ReturnsEmptyFlux() {
         List<Movie> emptyList = new ArrayList<>();
        when(movieRepository.findAll()).thenReturn(emptyList);

         Flux<MovieResponse> movieResponseFlux = movieService.getAllMovies();

         StepVerifier.create(movieResponseFlux)
                .expectNextCount(0)
                .verifyComplete();

        verify(movieRepository, times(1)).findAll();
    }
}
