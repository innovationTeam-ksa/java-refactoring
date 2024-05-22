package com.innovationTeam.refactoring.repository;

import com.innovationTeam.refactoring.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
