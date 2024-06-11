package com.innovationteam.task.services;

import com.innovationteam.task.exceptions.MovieNotFound;
import com.innovationteam.task.models.Customer;
import com.innovationteam.task.models.Movie;
import com.innovationteam.task.models.MovieRental;
import com.innovationteam.task.repos.MovieRepository;
import com.innovationteam.task.services.rentalstrategies.RentalStatementStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RentalStatementGenerator {

    private final MovieRepository movieRepository;
    private final ApplicationContext applicationContext;

    public Mono<String> generateStatement(Customer customer) {
        return Flux.fromIterable(customer.getRentals())
                .flatMap(this::calculateRentalAmount)
                .collectList()
                .flatMap(rentals -> {
                    double totalAmount = rentals.stream().mapToDouble(RentalAmount::amount).sum();
                    int frequentEnterPoints = rentals.stream().mapToInt(RentalAmount::frequentEnterPoints).sum();
                    return formatTheStatement(customer.getName(), rentals, totalAmount, frequentEnterPoints);
                });
    }

    private static Mono<String> formatTheStatement(String customerName, List<RentalAmount> rentals, double totalAmount, int frequentEnterPoints) {
        StringBuilder result = new StringBuilder("Rental Record for " + customerName + "\n");
        rentals.forEach(rentalRecord -> result.append("\t")
                .append(rentalRecord.movie().getTitle()).append("\t")
                .append(rentalRecord.amount()).append("\n"));
        result.append("Amount owed is ").append(totalAmount).append("\n");
        result.append("You earned ").append(frequentEnterPoints).append(" frequent points\n");
        return Mono.just(result.toString());
    }

    private Mono<RentalAmount> calculateRentalAmount(MovieRental rental) {
        return movieRepository.findById(rental.getMovieId())
                .switchIfEmpty(Mono.error(new MovieNotFound(rental.getMovieId())))
                .map(movie -> {
                    RentalStatementStrategy strategy = applicationContext.getBean(movie.getCode(), RentalStatementStrategy.class);
                    return new RentalAmount(movie, strategy.calculateAmount(rental), strategy.calculateFrequentRenterPoints(rental));
                });
    }

    private record RentalAmount(Movie movie, double amount, int frequentEnterPoints) {
    }
}
