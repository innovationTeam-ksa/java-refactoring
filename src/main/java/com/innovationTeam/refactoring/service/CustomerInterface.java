package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.model.request.CustomerRequestDto;
import com.innovationTeam.refactoring.model.response.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerInterface {
    Flux<CustomerResponse> getAllCustomers();

    Mono<Customer> createCustomer(CustomerRequestDto name);

    Mono<Customer> getCustomerById(Long id);

    Mono<String> generateCustomerStatement(Long customerId);
}
