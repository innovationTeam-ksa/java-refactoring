package com.innovationteam.task;

import com.innovationteam.task.services.RentalService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/rentals")
@RequiredArgsConstructor
public class RentalStatementBoundary {

    private final RentalService rentalService;


    @GetMapping("/{customerId}")
    @ApiResponse(responseCode = "200", description = "Rental information retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Customer with the given ID not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "500", description = "Application is not available at the moment")
    public Mono<String> getRentalInfo(@PathVariable String customerId) {
        return rentalService.generateCustomerStatement(customerId);
    }

}
