package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.mapper.CustomerMapper;
import com.innovationTeam.refactoring.model.CustomerRequestDto;
import com.innovationTeam.refactoring.repository.CustomerRepository;
import com.innovationTeam.refactoring.service.CustomerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerInterface {

    @Autowired
    CustomerRepository customerRepository;
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }
    @Override
    public String generateCustomerStatement(Long customerId) {
        return null;
    }

    @Override
    public Customer createCustomer(CustomerRequestDto customerRequestDto) {
        Customer customer = CustomerMapper.INSTANCE.mapToCustomer(customerRequestDto);
        return customerRepository.save(customer);
    }
}
