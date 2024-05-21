package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.mapper.CustomerMapper;
import com.innovationTeam.refactoring.model.CustomerRequestDto;
import com.innovationTeam.refactoring.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(CustomerRequestDto customerRequestDto) {
        Customer customer = CustomerMapper.INSTANCE.mapToCustomer(customerRequestDto);
        return customerRepository.save(customer);
    }
}
