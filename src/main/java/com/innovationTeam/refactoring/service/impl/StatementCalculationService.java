package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.model.RentalStatementInfo;
import com.innovationTeam.refactoring.model.Statement;
import com.innovationTeam.refactoring.repository.MovieRentalRepository;
import com.innovationTeam.refactoring.service.StatementCalculationInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class StatementCalculationService implements StatementCalculationInterface {

    private static final Logger logger = LoggerFactory.getLogger(StatementCalculationService.class);

    @Autowired
    private  MovieRentalRepository movieRentalRepository;

    @Override
    public Mono<Statement> calculateStatement(List<MovieRental> rentalList, String customerName) {
        logger.info("Calculating statement for customer: {}", customerName);

        return Flux.fromIterable(rentalList)
                .flatMap(this::calculateRentalAmountAsync)
                .collectList()
                .map(rentalStatementInfoList -> {
                    double totalAmount = rentalStatementInfoList.stream()
                            .mapToDouble(RentalStatementInfo::getAmount)
                            .sum();
                    int frequentEnterPoints = rentalStatementInfoList.size(); // Initialize with size for demonstration

                    Statement statement = new Statement(rentalStatementInfoList, totalAmount, frequentEnterPoints, customerName);
                    logger.info("Statement calculated: {}", statement);
                    return statement;
                });
    }

    private Mono<RentalStatementInfo> calculateRentalAmountAsync(MovieRental rental) {
        return Mono.fromCallable(() -> {
            double thisAmount = calculateRentalAmount(rental);
            return new RentalStatementInfo(rental.getMovie().getTitle(), thisAmount);
        });
    }

    private double calculateRentalAmount(MovieRental rental) {
        double thisAmount = 0;
        switch (rental.getMovie().getCode()) {
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
        logger.debug("Rental amount calculated for movie '{}' with code '{}': {}", rental.getMovie().getTitle(), rental.getMovie().getCode(), thisAmount);
        return thisAmount;
    }
}
