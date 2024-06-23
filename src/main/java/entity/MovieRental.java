package entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "movie_rentals")
public class MovieRental {

    @Id
    private String id;
    private String customerId; // Assuming String type for simplicity
    private String movieId;    // Assuming String type for simplicity
    private LocalDate rentalDate;
}
