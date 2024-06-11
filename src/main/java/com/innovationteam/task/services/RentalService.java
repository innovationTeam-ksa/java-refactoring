package com.innovationteam.task.services;

import com.innovationteam.task.exceptions.CustomerNotFound;
import com.innovationteam.task.models.Customer;
import com.innovationteam.task.repos.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final CustomerRepository customerRepository;
    private final RentalStatementGenerator rentalStatementGenerator;

    public Mono<Customer> getCustomerById(String id) {
        return customerRepository.findById(id).switchIfEmpty(Mono.error(new CustomerNotFound(id)));
    }

    public Mono<String> generateCustomerStatement(String customerId) {
        return getCustomerById(customerId)
                .flatMap(rentalStatementGenerator::generateStatement);
    }
}
