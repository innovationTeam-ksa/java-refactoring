package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.exception.CustomerNotFoundException;
import com.innovationTeam.refactoring.mapper.CustomerMapper;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

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

    public Flux<CustomerResponse> getAllCustomers() {
        logger.info("Fetching all customers");

        List<Customer> customers = customerRepository.findAll();
        logger.debug("Found {} customers", customers.size());

        return Flux.fromIterable(customers)
                .doOnNext(customer -> logger.debug("Processing customer: {}", customer))
                .map(CustomerMapper.INSTANCE::mapToCustomerResponse);
    }


    @Override
    public Mono<Customer> getCustomerById(Long id) {
        logger.info("Fetching customer with id: {}", id);
        if (id == null) {
            logger.error("Customer id is null");
            return Mono.error(new IllegalArgumentException(CUSTOMER_ID_NULL_ERROR));
        }

        return Mono.defer(() -> {
            Optional<Customer> customer = customerRepository.findById(id);
            if (customer.isEmpty()) {
                logger.warn("Customer with id {} not found", id);
                return Mono.empty();
            } else {
                return Mono.just(customer.get());
            }
        });
    }

    public Mono<String> generateCustomerStatement(Long customerId) {
        return Mono.just(customerRepository.findById(customerId))
                .flatMap(customer -> {
                    if (customer.isEmpty()) {
                        return Mono.error(new CustomerNotFoundException(String.format("Customer with id %d not found", customerId)));
                    }
                    return statementCalculationService.calculateStatement(customer.get().getRentals(), customer.get().getName())
                            .map(statement -> {
                                String printedStatement = statementPrintingService.printStatement(statement);
                                logger.debug("Generated statement: {}", printedStatement);
                                return printedStatement;
                            });
                });
    }

    @Override
    public Mono<Customer> createCustomer(CustomerRequestDto customerRequestDto) {
        validateCustomerRequest(customerRequestDto);
        logger.info("Creating new customer: {}", customerRequestDto.getName());

        Customer customer = CustomerMapper.INSTANCE.mapToCustomer(customerRequestDto);
        return Mono.just(customerRepository.save(customer))
                .doOnNext(savedCustomer -> logger.info("Created new customer with id: {}", savedCustomer.getId()));
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
