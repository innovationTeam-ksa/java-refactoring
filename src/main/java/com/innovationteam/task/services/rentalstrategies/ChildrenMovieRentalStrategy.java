package com.innovationteam.task.services.rentalstrategies;

import com.innovationteam.task.models.MovieRental;
import org.springframework.stereotype.Component;

@Component("children")
public class ChildrenMovieRentalStrategy implements RentalStatementStrategy {

    @Override
    public double calculateAmount(MovieRental rental) {
        double amount = 1.5;
        if (rental.getDays() > 3) {
            amount += (rental.getDays() - 3) * 1.5;
        }
        return amount;
    }

    @Override
    public int calculateFrequentRenterPoints(MovieRental rental) {
        return 1;
    }
}
