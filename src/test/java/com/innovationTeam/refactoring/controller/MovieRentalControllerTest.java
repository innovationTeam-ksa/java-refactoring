package com.innovationTeam.refactoring.controller;


import com.innovationTeam.refactoring.model.request.MovieRentalRequestDto;
import com.innovationTeam.refactoring.model.response.ApiResponseModel;
import com.innovationTeam.refactoring.model.response.ErrorResponse;
import com.innovationTeam.refactoring.model.response.MovieRentalResponse;
import com.innovationTeam.refactoring.service.MovieRentalInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static com.innovationTeam.refactoring.utils.Constants.ERROR_OCCURED_MSG;
import static com.innovationTeam.refactoring.utils.Constants.FAILED_CREATE_ERROR;
import static com.innovationTeam.refactoring.utils.Constants.MovieRentalConstants.FAILED_TO_RENT_MOVIE_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieRentalControllerTest {

    @Mock
    private MovieRentalInterface movieRentalService;

    @InjectMocks
    private MovieRentalController movieRentalController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRentMovieSuccess() {
        MovieRentalRequestDto rentalRequest = new MovieRentalRequestDto();
        MovieRentalResponse rentalResponse = new MovieRentalResponse();
        when(movieRentalService.rentMovie(any(MovieRentalRequestDto.class))).thenReturn(Mono.just(rentalResponse));

        Mono<ResponseEntity<ApiResponseModel<MovieRentalResponse>>> response = movieRentalController.rentMovie(rentalRequest);

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertEquals(rentalResponse, entity.getBody().getData());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testRentMovie_withError() {
        MovieRentalRequestDto rentalRequest = new MovieRentalRequestDto();
        when(movieRentalService.rentMovie(rentalRequest)).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ResponseEntity<ApiResponseModel<MovieRentalResponse>>> responseMono =
                movieRentalController.rentMovie(rentalRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(responseEntity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
                    assertNotNull(responseEntity.getBody());
                    ErrorResponse errorResponse = responseEntity.getBody().getError();
                    assertEquals(FAILED_CREATE_ERROR, errorResponse.getCode());
                    assertEquals(FAILED_TO_RENT_MOVIE_MSG, errorResponse.getMessage());
                    assertEquals(ERROR_OCCURED_MSG, errorResponse.getDescription());
                    return true;
                })
                .verifyComplete();

    }

    @Test
    public void testGetRentalsByCustomerSuccess() {
        long customerId = 1L;
        MovieRentalResponse rentalResponse = new MovieRentalResponse();
        when(movieRentalService.getRentalsByCustomer(customerId)).thenReturn(Flux.just(rentalResponse));

        Mono<ResponseEntity<ApiResponseModel<List<MovieRentalResponse>>>> response = movieRentalController.getRentalsByCustomer(customerId);

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.OK, entity.getStatusCode());
                    assertEquals(Collections.singletonList(rentalResponse), entity.getBody().getData());
                    return true;
                })
                .verifyComplete();
    }


    @Test
    public void testGetRentalsByCustomerInternalServerError() {
        long customerId = 1L;
        when(movieRentalService.getRentalsByCustomer(customerId)).thenReturn(Flux.error(new RuntimeException("Internal error")));

        Mono<ResponseEntity<ApiResponseModel<List<MovieRentalResponse>>>> response = movieRentalController.getRentalsByCustomer(customerId);

        StepVerifier.create(response)
                .expectNextMatches(entity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entity.getStatusCode());
                    assertEquals("Internal server error", entity.getBody().getError().getCode());
                    assertEquals("Failed to fetch rentals", entity.getBody().getError().getMessage());
                    assertEquals("An error occurred while fetching rentals", entity.getBody().getError().getDescription());
                    return true;
                })
                .verifyComplete();
    }

}
