package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.model.Statement;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StatementCalculationInterface {
    Mono<Statement> calculateStatement(List<MovieRental> rentalList, String customerName);
}
