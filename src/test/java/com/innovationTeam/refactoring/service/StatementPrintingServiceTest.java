package com.innovationTeam.refactoring.service.impl;

import com.innovationTeam.refactoring.model.RentalStatementInfo;
import com.innovationTeam.refactoring.model.Statement;
import com.innovationTeam.refactoring.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class StatementPrintingServiceTest {

    @InjectMocks
    private StatementPrintingService statementPrintingService;


    @Test
    public void testPrintStatementWithMultipleRentals() {
        RentalStatementInfo rental1 = new RentalStatementInfo("Movie 1", 3.0);
        RentalStatementInfo rental2 = new RentalStatementInfo("Movie 2", 5.0);
        Statement statement = new Statement(Arrays.asList(rental1, rental2), 8.0, 2, "John Doe");

        String result = statementPrintingService.printStatement(statement);

        String expected = String.format(Constants.RENTAL_RECORD_HEADER_FORMAT, "John Doe") +
                String.format(Constants.RENTAL_INFO_FORMAT, "Movie 1", 3.0) +
                String.format(Constants.RENTAL_INFO_FORMAT, "Movie 2", 5.0) +
                String.format(Constants.AMOUNT_OWED_FORMAT, 8.0) +
                String.format(Constants.FREQUENT_POINTS_FORMAT, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testPrintStatementWithNoRentals() {
        Statement statement = new Statement(Collections.emptyList(), 0.0, 0, "John Doe");

        String result = statementPrintingService.printStatement(statement);


        String expected = String.format(Constants.RENTAL_RECORD_HEADER_FORMAT, "John Doe") +
                String.format(Constants.AMOUNT_OWED_FORMAT, 0.0) +
                String.format(Constants.FREQUENT_POINTS_FORMAT, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testPrintStatementWithSingleRental() {
        RentalStatementInfo rental = new RentalStatementInfo("Movie 1", 3.0);
        Statement statement = new Statement(Collections.singletonList(rental), 3.0, 1, "John Doe");

        String result = statementPrintingService.printStatement(statement);

        String expected = String.format(Constants.RENTAL_RECORD_HEADER_FORMAT, "John Doe") +
                String.format(Constants.RENTAL_INFO_FORMAT, "Movie 1", 3.0) +
                String.format(Constants.AMOUNT_OWED_FORMAT, 3.0) +
                String.format(Constants.FREQUENT_POINTS_FORMAT, 1);
        assertEquals(expected, result);
    }


}
