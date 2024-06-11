package com.innovationteam.task.services.rentalstrategies;


import com.innovationteam.task.models.MovieRental;

public interface RentalStatementStrategy {
    double calculateAmount(MovieRental rental);

    int calculateFrequentRenterPoints(MovieRental rental);
}
