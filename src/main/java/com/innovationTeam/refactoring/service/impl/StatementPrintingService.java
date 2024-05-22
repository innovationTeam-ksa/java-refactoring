package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.model.RentalStatementInfo;
import com.innovationTeam.refactoring.model.Statement;
import com.innovationTeam.refactoring.service.StatementPrintingInterface;
import com.innovationTeam.refactoring.utils.Constants;
import org.springframework.stereotype.Service;

@Service
public class StatementPrintingService implements StatementPrintingInterface {
    @Override
    public String printStatement(Statement statement) {
        StringBuilder result = new StringBuilder();

        result.append(String.format(Constants.RENTAL_RECORD_HEADER_FORMAT, statement.getCustomerName()));

        for (RentalStatementInfo rentalInfo : statement.getRentalInfoList()) {
            result.append(String.format(Constants.RENTAL_INFO_FORMAT, rentalInfo.getMovieTitle(), rentalInfo.getAmount()));
        }

        result.append(String.format(Constants.AMOUNT_OWED_FORMAT, statement.getTotalAmount()));
        result.append(String.format(Constants.FREQUENT_POINTS_FORMAT, statement.getFrequentEnterPoints()));

        return result.toString();
    }
}
