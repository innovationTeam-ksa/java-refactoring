package com.innovationteam.task.services.rentalstrategies;

import com.innovationteam.task.models.MovieRental;
import org.springframework.stereotype.Component;

@Component("regular")
public class RegularMovieRentalStrategy implements RentalStatementStrategy {

    @Override
    public double calculateAmount(MovieRental rental) {
        double amount = 2;
        if (rental.getDays() > 2) {
            amount += (rental.getDays() - 2) * 1.5;
        }
        return amount;
    }

    @Override
    public int calculateFrequentRenterPoints(MovieRental rental) {
        return 1;
    }
}
