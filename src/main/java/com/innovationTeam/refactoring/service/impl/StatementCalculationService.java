package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.model.RentalStatementInfo;
import com.innovationTeam.refactoring.model.Statement;
import com.innovationTeam.refactoring.service.StatementCalculationInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.innovationTeam.refactoring.enums.Code.NEW;

@Service
public class StatementCalculationService implements StatementCalculationInterface {

    private static final Logger logger = LoggerFactory.getLogger(StatementCalculationService.class);

    @Override
    public Statement calculateStatement(List<MovieRental> rentalList, String customerName) {
        logger.info("Calculating statement for customer: {}", customerName);

        double totalAmount = 0;
        int frequentEnterPoints = 0;

        ArrayList<RentalStatementInfo> rentalStatementInfoList = new ArrayList<>();
        for (MovieRental rental : rentalList) {
            double thisAmount = calculateRentalAmount(rental);
            frequentEnterPoints++;

            if (rental.getMovie().getCode().equals(NEW) && rental.getDays() > 2) {
                frequentEnterPoints++;
            }
            rentalStatementInfoList.add(new RentalStatementInfo(rental.getMovie().getTitle(), thisAmount));
            totalAmount += thisAmount;
        }

        Statement statement = new Statement(rentalStatementInfoList, totalAmount, frequentEnterPoints, customerName);
        logger.info("Statement calculated: {}", statement);

        return statement;
    }

    private double calculateRentalAmount(MovieRental rental) {
        Movie movie = rental.getMovie();
        double thisAmount = 0;

        switch (movie.getCode()) {
            case REGULAR:
                thisAmount = 2;
                if (rental.getDays() > 2) {
                    thisAmount += (rental.getDays() - 2) * 1.5;
                }
                break;
            case NEW:
                thisAmount = rental.getDays() * 3;
                break;
            case CHILDRENS:
                thisAmount = 1.5;
                if (rental.getDays() > 3) {
                    thisAmount += (rental.getDays() - 3) * 1.5;
                }
                break;
            default:
                break;
        }

        logger.debug("Rental amount calculated for movie '{}' with code '{}': {}", movie.getTitle(), movie.getCode(), thisAmount);
        return thisAmount;
    }

}
