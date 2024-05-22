package com.innovationTeam.refactoring.service;

import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.model.Statement;

import java.util.List;

public interface StatementCalculationInterface {
    Statement calculateStatement(List<MovieRental> rentalList, String customerName);
}
