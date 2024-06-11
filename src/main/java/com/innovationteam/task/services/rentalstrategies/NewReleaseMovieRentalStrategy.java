package com.innovationteam.task.services.rentalstrategies;

import com.innovationteam.task.models.MovieRental;
import org.springframework.stereotype.Component;

@Component("new")
public class NewReleaseMovieRentalStrategy implements RentalStatementStrategy {

    @Override
    public double calculateAmount(MovieRental rental) {
        return rental.getDays() * 3;
    }

    @Override
    public int calculateFrequentRenterPoints(MovieRental rental) {
        return rental.getDays() > 2 ? 2 : 1;
    }
}
