package com.innovationTeam.refactoring.controller;

import com.innovationTeam.refactoring.model.request.MovieRequestDto;
import com.innovationTeam.refactoring.model.response.ApiResponseBuilder;
import com.innovationTeam.refactoring.model.response.ApiResponseModel;
import com.innovationTeam.refactoring.model.response.ErrorResponse;
import com.innovationTeam.refactoring.model.response.MovieResponse;
import com.innovationTeam.refactoring.service.MovieInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.innovationTeam.refactoring.utils.Constants.FAILED_CREATE_ERROR;

@RestController
@RequestMapping("/v1/movies")
public class MovieController {
    @Autowired
    MovieInterface movieService;
    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @GetMapping
    @Operation(summary = "Retrieve all movies", description = "Fetches a list of all movies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of movies"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<ApiResponseModel<List<MovieResponse>>>> getAllMovies() {
        return movieService.getAllMovies()
                .collectList()
                .map(movieResponses -> ResponseEntity.ok(new ApiResponseBuilder<List<MovieResponse>>()
                        .success()
                        .addData(movieResponses)
                        .build()))
                .onErrorResume(e -> {
                    logger.error("Failed to fetch all movies", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiResponseBuilder<List<MovieResponse>>()
                                    .error(new ErrorResponse("Failed to fetch movies", "Failed to retrieve movies list", "An error occurred while fetching movies"))
                                    .build()));
                });
    }

    @PostMapping
    @Operation(summary = "Create a new movie", description = "Creates a new movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<ApiResponseModel<String>>> createMovie(@RequestBody MovieRequestDto movieRequest) {
        return movieService.saveMovie(movieRequest)
                .map(savedMovie -> {
                    if (savedMovie == null) {
                        return ResponseEntity.ok(new ApiResponseBuilder<String>()
                                .error(new ErrorResponse(FAILED_CREATE_ERROR, "Failed to save movie", "An error occurred while saving movie"))
                                .build());
                    } else {
                        return ResponseEntity.ok(new ApiResponseBuilder<String>()
                                .success()
                                .addData("Movie saved successfully")
                                .build());
                    }
                })
                .onErrorResume(e -> {
                    logger.error("Failed to create movie", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiResponseBuilder<String>()
                                    .error(new ErrorResponse("Failed to create movie", "Failed to save movie", "An error occurred while saving movie"))
                                    .build()));
                });
    }

}
