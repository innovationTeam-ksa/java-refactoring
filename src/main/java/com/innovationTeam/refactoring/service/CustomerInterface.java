package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.model.request.CustomerRequestDto;
import com.innovationTeam.refactoring.model.response.CustomerResponse;

import java.util.List;
import java.util.Optional;

public interface CustomerInterface {
    List<CustomerResponse> getAllCustomers();

    Customer createCustomer(CustomerRequestDto name);

    Optional<Customer> getCustomerById(Long id);

    String generateCustomerStatement(Long customerId);
}
