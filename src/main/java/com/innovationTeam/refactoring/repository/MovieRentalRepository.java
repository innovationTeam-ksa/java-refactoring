package com.innovationTeam.refactoring.repository;

import com.innovationTeam.refactoring.entity.MovieRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRentalRepository extends JpaRepository<MovieRental, Long> {
}
