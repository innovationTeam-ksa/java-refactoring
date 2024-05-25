package com.innovationTeam.refactoring.controller;

import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.model.request.MovieRequestDto;
import com.innovationTeam.refactoring.model.response.ApiResponseModel;
import com.innovationTeam.refactoring.model.response.MovieResponse;
import com.innovationTeam.refactoring.service.MovieInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class MovieControllerTest {

    @Mock
    private MovieInterface movieService;

    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllMoviesSuccess() {
        MovieResponse movieResponse = new MovieResponse();
        when(movieService.getAllMovies()).thenReturn(Flux.just(movieResponse));

        Mono<ResponseEntity<ApiResponseModel<List<MovieResponse>>>> response = movieController.getAllMovies();

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertEquals(Collections.singletonList(movieResponse), entity.getBody().getData());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testGetAllMoviesInternalServerError() {
        when(movieService.getAllMovies()).thenReturn(Flux.error(new RuntimeException("Internal error")));

        Mono<ResponseEntity<ApiResponseModel<List<MovieResponse>>>> response = movieController.getAllMovies();

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entity.getStatusCode());
                    assertEquals("Failed to fetch movies", entity.getBody().getError().getCode());
                    assertEquals("Failed to retrieve movies list", entity.getBody().getError().getMessage());
                    assertEquals("An error occurred while fetching movies", entity.getBody().getError().getDescription());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testCreateMovieSuccess() {
        MovieRequestDto movieRequest = new MovieRequestDto();
        when(movieService.saveMovie(any(MovieRequestDto.class))).thenReturn(Mono.just(new Movie()));

        Mono<ResponseEntity<ApiResponseModel<String>>> response = movieController.createMovie(movieRequest);

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertEquals("Movie saved successfully", entity.getBody().getData());
                    return true;
                })
                .verifyComplete();
    }


    @Test
    public void testCreateMovieNull() {
        MovieRequestDto movieRequest = new MovieRequestDto();
        when(movieService.saveMovie(any(MovieRequestDto.class))).thenReturn(Mono.empty());

        Mono<ResponseEntity<ApiResponseModel<String>>> response = movieController.createMovie(movieRequest);

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertEquals("FAILED_CREATE_ERROR", entity.getBody().getError().getCode());
                    assertEquals("Failed to save movie", entity.getBody().getError().getMessage());
                    assertEquals("An error occurred while saving movie", entity.getBody().getError().getDescription());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testCreateMovieInternalServerError() {
        MovieRequestDto movieRequest = new MovieRequestDto();
        when(movieService.saveMovie(any(MovieRequestDto.class))).thenReturn(Mono.error(new RuntimeException("Internal error")));

        Mono<ResponseEntity<ApiResponseModel<String>>> response = movieController.createMovie(movieRequest);

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entity.getStatusCode());
                    assertEquals("Failed to create movie", entity.getBody().getError().getCode());
                    assertEquals("Failed to save movie", entity.getBody().getError().getMessage());
                    assertEquals("An error occurred while saving movie", entity.getBody().getError().getDescription());
                    return true;
                })
                .verifyComplete();
    }
}
