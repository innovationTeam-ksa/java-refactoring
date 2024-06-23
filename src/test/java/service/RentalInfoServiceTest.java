package service;

import entity.Customer;
import entity.MovieRental;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RentalInfoServiceTest {

    @Mock
    private Customer customer;

    @InjectMocks
    private RentalInfoService rentalInfoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateRentalStatement() {
        // Mock customer and rental data
        when(customer.getName()).thenReturn("Test Customer");
        when(customer.getRentals()).thenReturn(Collections.singletonList(new MovieRental("F001", 3)));

        String expected = "Rental Record for Test Customer\n" +
                "\tYou've Got Mail\t3.5\n" +
                "Amount owed is 3.5\n" +
                "You earned 1 frequent points\n";

        String actual = rentalInfoService.generateRentalStatement(customer);

        assertEquals(expected, actual);
    }
}
