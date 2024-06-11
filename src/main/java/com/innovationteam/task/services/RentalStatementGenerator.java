package com.innovationteam.task.services;

import com.innovationteam.task.exceptions.MovieNotFound;
import com.innovationteam.task.models.Customer;
import com.innovationteam.task.models.Movie;
import com.innovationteam.task.models.MovieRental;
import com.innovationteam.task.repos.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RentalStatementGenerator {

    private final MovieRepository movieRepository;

    public Mono<String> generateStatement(Customer customer) {
        return Flux.fromIterable(customer.getRentals())
                .flatMap(this::calculateRentalAmount)
                .collectList()
                .flatMap(rentals -> {
                    double totalAmount = rentals.stream().mapToDouble(RentalAmount::amount).sum();
                    int frequentEnterPoints = rentals.stream().mapToInt(RentalAmount::frequentEnterPoints).sum();
                    StringBuilder result = new StringBuilder("Rental Record for " + customer.getName() + "\n");
                    rentals.forEach(rentalRecord -> result.append("\t")
                            .append(rentalRecord.movie().getTitle()).append("\t")
                            .append(rentalRecord.amount()).append("\n"));
                    result.append("Amount owed is ").append(totalAmount).append("\n");
                    result.append("You earned ").append(frequentEnterPoints).append(" frequent points\n");
                    return Mono.just(result.toString());
                });
    }

    private Mono<RentalAmount> calculateRentalAmount(MovieRental rental) {
        return movieRepository.findById(rental.getMovieId())
                .switchIfEmpty(Mono.error(new MovieNotFound(rental.getMovieId())))
                .map(movie -> {
                    double thisAmount = 0;
                    int frequentEnterPoints = 1;

                    switch (movie.getCode()) {
                        case "regular":
                            thisAmount = 2;
                            if (rental.getDays() > 2) {
                                thisAmount += (rental.getDays() - 2) * 1.5;
                            }
                            break;
                        case "new":
                            thisAmount = rental.getDays() * 3;
                            if (rental.getDays() > 2) {
                                frequentEnterPoints++;
                            }
                            break;
                        case "children":
                            thisAmount = 1.5;
                            if (rental.getDays() > 3) {
                                thisAmount += (rental.getDays() - 3) * 1.5;
                            }
                            break;
                    }

                    return new RentalAmount(movie, thisAmount, frequentEnterPoints);
                });
    }

    private record RentalAmount(Movie movie, double amount, int frequentEnterPoints) {
    }
}
