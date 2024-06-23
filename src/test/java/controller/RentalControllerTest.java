package controller;

import entity.Customer;
import handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import response.RentalResponse;
import service.RentalInfoService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RentalController.class)
public class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalInfoService rentalInfoService;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Test
    void testGenerateRentalStatement() throws Exception {
        Customer customer = new Customer("Test Customer", Collections.singletonList(new MovieRental("F001", 3)));
        RentalResponse expectedResponse = new RentalResponse("Rental Record for Test Customer\n\tYou've Got Mail\t3.5\nAmount owed is 3.5\nYou earned 1 frequent points\n");

        when(rentalInfoService.generateRentalStatement(any(Customer.class))).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/rental/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Customer\", \"rentals\": [{\"movieId\": \"F001\", \"days\": 3}]}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statement").value(expectedResponse.getStatement()));
    }

    // Add more tests for exception handling scenarios
}
