package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.exception.CustomerNotFoundException;
import com.innovationTeam.refactoring.mapper.CustomerMapper;
import com.innovationTeam.refactoring.model.Statement;
import com.innovationTeam.refactoring.model.request.CustomerRequestDto;
import com.innovationTeam.refactoring.model.response.CustomerResponse;
import com.innovationTeam.refactoring.repository.CustomerRepository;
import com.innovationTeam.refactoring.service.CustomerInterface;
import com.innovationTeam.refactoring.service.StatementCalculationInterface;
import com.innovationTeam.refactoring.service.StatementPrintingInterface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.innovationTeam.refactoring.utils.Constants.UserConstants.*;

@Service
@Transactional
public class CustomerService implements CustomerInterface {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    StatementCalculationInterface statementCalculationService;
    @Autowired
    StatementPrintingInterface statementPrintingService;

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(CustomerMapper.INSTANCE::mapToCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(CUSTOMER_ID_NULL_ERROR);
        }
        Optional<Customer> customer = customerRepository.findById(id);
        return customer;
    }

    @Override
    public String generateCustomerStatement(Long customerId) {
        return getCustomerById(customerId)
                .map(customer -> {
                    Statement statement = statementCalculationService.calculateStatement(customer.getRentals(), customer.getName());
                    return statementPrintingService.printStatement(statement);
                })
                .orElseThrow(() -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND_ERROR, customerId)));
    }

    @Override
    public Customer createCustomer(CustomerRequestDto customerRequestDto) {
        validateCustomerRequest(customerRequestDto);
        Customer customer = CustomerMapper.INSTANCE.mapToCustomer(customerRequestDto);
        return customerRepository.save(customer);
    }

    private void validateCustomerRequest(CustomerRequestDto customerRequestDto) {
        if (customerRequestDto == null) {
            throw new IllegalArgumentException(CUSTOMER_REQUEST_NULL_ERROR);
        }
        if (customerRequestDto.getName() == null || customerRequestDto.getName().isEmpty()) {
            throw new IllegalArgumentException(CUSTOMER_NAME_NOT_EMPTY);
        }
    }

}
