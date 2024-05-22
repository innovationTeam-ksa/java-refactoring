package com.innovationTeam.refactoring.controller;

import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.model.request.MovieRequestDto;
import com.innovationTeam.refactoring.model.response.ApiResponseBuilder;
import com.innovationTeam.refactoring.model.response.ApiResponseModel;
import com.innovationTeam.refactoring.model.response.ErrorResponse;
import com.innovationTeam.refactoring.model.response.MovieResponse;
import com.innovationTeam.refactoring.service.MovieInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/movies")
public class MovieController {
    @Autowired
    MovieInterface movieService;


    @GetMapping
    @Operation(summary = "Retrieve all movies", description = "Fetches a list of all movies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of movies"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponseModel<List<MovieResponse>>> getAllCustomers() {
        List<MovieResponse> movieResponses = movieService.getAllMovies();

        ApiResponseModel<List<MovieResponse>> apiResponse = new ApiResponseBuilder<List<MovieResponse>>()
                .success()
                .addData(movieResponses)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Create a new movie", description = "Creates a new movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponseModel<String>> createMovie(@RequestBody MovieRequestDto movieRequest) {
        Movie savedMovie = movieService.saveMovie(movieRequest);

        if (savedMovie == null) {
            return ResponseEntity.ok(new ApiResponseBuilder<String>()
                    .error(new ErrorResponse("CREATE_FAILED", "Failed to save movie", "An error occurred while saving movie"))
                    .build());
        }

        return ResponseEntity.ok(new ApiResponseBuilder<String>()
                .success()
                .addData("movie saved successfly")
                .build());
    }
}
