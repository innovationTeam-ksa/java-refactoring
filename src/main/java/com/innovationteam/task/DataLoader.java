package com.innovationteam.task;

import com.innovationteam.task.models.Customer;
import com.innovationteam.task.models.Movie;
import com.innovationteam.task.models.MovieRental;
import com.innovationteam.task.repos.CustomerRepository;
import com.innovationteam.task.repos.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.Arrays.asList;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {
        customerRepository.deleteAll().block();

        //include them in list:

        movieRepository.saveAll(asList(
                new Movie("F001", "You've Got Mail", "regular"),
                new Movie("F002", "Matrix", "regular"),
                new Movie("F003", "Cars", "children"),
                new Movie("F004", "Fast & Furious X", "new")
        )).blockLast();

        List<MovieRental> rentals1 = asList(
                new MovieRental("F001", 3),
                new MovieRental("F002", 1)
        );
        List<MovieRental> rentals2 = asList(
                new MovieRental("F003", 5),
                new MovieRental("F004", 2)
        );

        Customer customer1 = new Customer("C001", "John Doe", rentals1);
        Customer customer2 = new Customer("C002", "Jane Smith", rentals2);

        customerRepository.saveAll(asList(customer1, customer2)).blockLast();
    }
}
