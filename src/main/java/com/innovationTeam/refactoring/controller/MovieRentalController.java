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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.innovationTeam.refactoring.utils.Constants.ERROR_OCCURED_MSG;
import static com.innovationTeam.refactoring.utils.Constants.FAILED_CREATE_ERROR;
import static com.innovationTeam.refactoring.utils.Constants.MovieRentalConstants.FAILED_TO_RENT_MOVIE_MSG;

@RestController
@RequestMapping("/v1/rentals")
public class MovieRentalController {
    private static final Logger logger = LoggerFactory.getLogger(MovieRentalController.class);

    @Autowired
    MovieRentalInterface movieRentalService;


    @PostMapping
    @Operation(summary = "Rent a movie", description = "Rent a movie for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully rented the movie"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponseModel<MovieRentalResponse>> rentMovie(@RequestBody MovieRentalRequestDto rentalRequest) {
         try {
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
        } catch (Exception e) {
            logger.error("Error renting movie for customer with ID: {}", rentalRequest.getCustomerId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseBuilder<MovieRentalResponse>()
                            .error(new ErrorResponse("Internal server error", "Failed to rent movie", "An error occurred while renting movie"))
                            .build());
        }
    }

    @GetMapping("/customers/{customerId}")
    @Operation(summary = "Get rentals by customer", description = "Get all rentals for a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all rentals for the customer"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponseModel<List<MovieRentalResponse>>> getRentalsByCustomer(@PathVariable long customerId) {
        logger.info("Fetching rentals for customer with ID: {}", customerId);
        try {
            List<MovieRentalResponse> movieRentalResponses = movieRentalService.getRentalsByCustomer(customerId);

            ApiResponseModel<List<MovieRentalResponse>> apiResponse = new ApiResponseBuilder<List<MovieRentalResponse>>()
                    .success()
                    .addData(movieRentalResponses)
                    .build();

            logger.info("Fetched {} rentals for customer with ID: {}", movieRentalResponses.size(), customerId);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            logger.error("Error fetching rentals for customer with ID: {}", customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseBuilder<List<MovieRentalResponse>>()
                            .error(new ErrorResponse("Internal server error", "Failed to fetch rentals", "An error occurred while fetching rentals"))
                            .build());
        }
    }

}
