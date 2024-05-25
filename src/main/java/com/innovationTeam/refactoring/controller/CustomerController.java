package com.innovationTeam.refactoring.controller;

import com.innovationTeam.refactoring.exception.CustomerNotFoundException;
import com.innovationTeam.refactoring.model.request.CustomerRequestDto;
import com.innovationTeam.refactoring.model.response.ApiResponseBuilder;
import com.innovationTeam.refactoring.model.response.ApiResponseModel;
import com.innovationTeam.refactoring.model.response.CustomerResponse;
import com.innovationTeam.refactoring.model.response.ErrorResponse;
import com.innovationTeam.refactoring.service.impl.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.innovationTeam.refactoring.utils.Constants.ERROR_OCCURED_MSG;

@RestController
@RequestMapping("/v1/customers")
@Slf4j
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);


    @GetMapping
    @Operation(summary = "Retrieve all customers", description = "Fetches a list of all customers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of customers"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<ApiResponseModel<List<CustomerResponse>>>> getAllCustomers() {
        return customerService.getAllCustomers()
                .collectList()
                .map(customerResponses -> ResponseEntity.ok(
                        new ApiResponseBuilder<List<CustomerResponse>>()
                                .success()
                                .addData(customerResponses)
                                .build()
                ))
                .onErrorResume(e -> {
                    logger.error("Failed to fetch all customers", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiResponseBuilder<List<CustomerResponse>>()
                                    .error(new ErrorResponse("Failed to fetch customers", "Failed to retrieve customers list", ERROR_OCCURED_MSG))
                                    .build()));
                });
    }

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Creates a new customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added a new customer"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<ApiResponseModel<String>>> createCustomer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Customer object to be created", required = true,
                    content = @Content(schema = @Schema(implementation = CustomerRequestDto.class)))
            @RequestBody CustomerRequestDto customerRequest) {
        return customerService.createCustomer(customerRequest)
                .flatMap(createdCustomer -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseBuilder<String>()
                        .success()
                        .addData("User added successfully")
                        .build())))
                .switchIfEmpty(Mono.just(ResponseEntity.ok(new ApiResponseBuilder<String>()
                        .error(new ErrorResponse("FAILED_CREATE_ERROR", "FAILED_TO_CREATE_USER_MSG", "ERROR_OCCURED_MSG"))
                        .build())))
                .onErrorResume(e -> {
                    logger.error("Failed to create customer", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiResponseBuilder<String>()
                                    .error(new ErrorResponse("Failed to create customer", "Failed to create new customer", "ERROR_OCCURED_MSG"))
                                    .build()));
                });
    }

    @GetMapping("/{customerId}/statement")
    public Mono<ResponseEntity<ApiResponseModel<String>>> getCustomerStatement(
            @PathVariable("customerId") Long customerId) {

        return customerService.generateCustomerStatement(customerId)
                .map(statement -> ResponseEntity.ok(new ApiResponseBuilder<String>()
                        .success()
                        .addData(statement)
                        .build()))
                .onErrorResume(CustomerNotFoundException.class, e -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponseBuilder<String>()
                                    .error(new ErrorResponse("Customer not found", "Customer with specified id not found", ERROR_OCCURED_MSG))
                                    .build()));
                })
                .onErrorResume(Exception.class, e -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiResponseBuilder<String>()
                                    .error(new ErrorResponse("Failed to fetch customer statement", "Failed to retrieve customer statement", ERROR_OCCURED_MSG))
                                    .build()));
                });
    }
}
