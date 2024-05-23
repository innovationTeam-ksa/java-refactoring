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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.innovationTeam.refactoring.utils.Constants.UserConstants.*;

@Service
@Transactional
public class CustomerService implements CustomerInterface {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    StatementCalculationInterface statementCalculationService;
    @Autowired
    StatementPrintingInterface statementPrintingService;

    @Override
    public List<CustomerResponse> getAllCustomers() {
        logger.info("Fetching all customers");
        List<Customer> customers = customerRepository.findAll();
        logger.debug("Found {} customers", customers.size());
        return customers.stream()
                .map(CustomerMapper.INSTANCE::mapToCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        logger.info("Fetching customer with id: {}", id);
        if (id == null) {
            logger.error("Customer id is null");
            throw new IllegalArgumentException(CUSTOMER_ID_NULL_ERROR);
        }
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            logger.warn("Customer with id {} not found", id);
        }
        return customer;
    }

    @Override
    public String generateCustomerStatement(Long customerId) {
        logger.info("Generating statement for customer with id: {}", customerId);
        return getCustomerById(customerId)
                .map(customer -> {
                    Statement statement = statementCalculationService.calculateStatement(customer.getRentals(), customer.getName());
                    String printedStatement = statementPrintingService.printStatement(statement);
                    logger.debug("Generated statement: {}", printedStatement);
                    return printedStatement;
                })
                .orElseThrow(() -> {
                    logger.error("Customer with id {} not found", customerId);
                    return new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND_ERROR, customerId));
                });
    }

    @Override
    public Customer createCustomer(CustomerRequestDto customerRequestDto) {
        logger.info("Creating new customer: {}", customerRequestDto.getName());
        validateCustomerRequest(customerRequestDto);
        Customer customer = CustomerMapper.INSTANCE.mapToCustomer(customerRequestDto);
        Customer savedCustomer = customerRepository.save(customer);
        logger.info("Created new customer with id: {}", savedCustomer.getId());
        return savedCustomer;
    }

    private void validateCustomerRequest(CustomerRequestDto customerRequestDto) {
        if (customerRequestDto == null) {
            logger.error("Customer request is null");
            throw new IllegalArgumentException(CUSTOMER_REQUEST_NULL_ERROR);
        }
        if (customerRequestDto.getName() == null || customerRequestDto.getName().isEmpty()) {
            logger.error("Customer name is empty");
            throw new IllegalArgumentException(CUSTOMER_NAME_NOT_EMPTY);
        }
    }

}
