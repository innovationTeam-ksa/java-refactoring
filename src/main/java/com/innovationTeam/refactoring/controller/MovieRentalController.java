package com.innovationTeam.refactoring.controller;

import com.innovationTeam.refactoring.model.request.MovieRentalRequestDto;
import com.innovationTeam.refactoring.model.response.ApiResponseBuilder;
import com.innovationTeam.refactoring.model.response.ApiResponseModel;
import com.innovationTeam.refactoring.model.response.ErrorResponse;
import com.innovationTeam.refactoring.model.response.MovieRentalResponse;
import com.innovationTeam.refactoring.service.MovieRentalInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.innovationTeam.refactoring.utils.Constants.ERROR_OCCURED_MSG;
import static com.innovationTeam.refactoring.utils.Constants.FAILED_CREATE_ERROR;
import static com.innovationTeam.refactoring.utils.Constants.MovieRentalConstants.FAILED_TO_RENT_MOVIE_MSG;

@RestController
@RequestMapping("/v1/rentals")
public class MovieRentalController {

    @Autowired
    MovieRentalInterface movieRentalService;

    @Operation(summary = "Rent a movie", description = "Rent a movie for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully rented the movie"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ApiResponseModel<MovieRentalResponse>> rentMovie(@RequestBody MovieRentalRequestDto rentalRequest) {
        MovieRentalResponse movieRentalResponse = movieRentalService.rentMovie(rentalRequest);

        if (movieRentalResponse == null) {
            return ResponseEntity.ok(new ApiResponseBuilder<MovieRentalResponse>()
                    .error(new ErrorResponse(FAILED_CREATE_ERROR, FAILED_TO_RENT_MOVIE_MSG, ERROR_OCCURED_MSG))
                    .build());
        }

        return ResponseEntity.ok(new ApiResponseBuilder<MovieRentalResponse>()
                .success()
                .addData(movieRentalResponse)
                .build());
    }

    @Operation(summary = "Get rentals by customer", description = "Get all rentals for a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all rentals for the customer"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<ApiResponseModel<List<MovieRentalResponse>>> getRentalsByCustomer(@PathVariable long customerId) {
        List<MovieRentalResponse> movieRentalResponses = movieRentalService.getRentalsByCustomer(customerId);

        ApiResponseModel<List<MovieRentalResponse>> apiResponse = new ApiResponseBuilder<List<MovieRentalResponse>>()
                .success()
                .addData(movieRentalResponses)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
