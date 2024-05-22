package com.innovationTeam.refactoring.controller;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.model.request.CustomerRequestDto;
import com.innovationTeam.refactoring.model.response.ApiResponseBuilder;
import com.innovationTeam.refactoring.model.response.ApiResponseModel;
import com.innovationTeam.refactoring.model.response.CustomerResponse;
import com.innovationTeam.refactoring.model.response.ErrorResponse;
import com.innovationTeam.refactoring.service.impl.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.innovationTeam.refactoring.utils.Constants.ERROR_OCCURED_MSG;
import static com.innovationTeam.refactoring.utils.Constants.FAILED_CREATE_ERROR;
import static com.innovationTeam.refactoring.utils.Constants.UserConstants.FAILED_TO_CREATE_USER_MSG;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping
    @Operation(summary = "Retrieve all customers", description = "Fetches a list of all customers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of customers"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponseModel<List<CustomerResponse>>> getAllCustomers() {
        List<CustomerResponse> customerResponses = customerService.getAllCustomers(); // Assuming this returns List<Customer>

        ApiResponseModel<List<CustomerResponse>> apiResponse = new ApiResponseBuilder<List<CustomerResponse>>()
                .success()
                .addData(customerResponses)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Creates a new customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added a new customer"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponseModel<String>> createCustomer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Customer object to be created", required = true,
                    content = @Content(schema = @Schema(implementation = CustomerRequestDto.class)))
            @RequestBody CustomerRequestDto customerRequest) {
        Customer createdCustomer = customerService.createCustomer(customerRequest);

        if (createdCustomer == null) {
            return ResponseEntity.ok(new ApiResponseBuilder<String>()
                    .error(new ErrorResponse(FAILED_CREATE_ERROR, FAILED_TO_CREATE_USER_MSG, ERROR_OCCURED_MSG))
                    .build());
        }

        return ResponseEntity.ok(new ApiResponseBuilder<String>()
                .success()
                .addData("User added successfully")
                .build());
    }


    @GetMapping("/{customerId}/statement")
    @Operation(summary = "Retrieve Customer Statement", description = "Retrieves the rental statement for a specific customer")
    public ResponseEntity<ApiResponseModel<String>> getCustomerStatement(
            @Parameter(description = "ID of the customer for whom statement is to be retrieved", required = true)
            @PathVariable("customerId") Long customerId) {

        String statement = customerService.generateCustomerStatement(customerId);
        return ResponseEntity.ok(new ApiResponseBuilder<String>()
                .success()
                .addData(statement)
                .build());
    }
}
