package controller;

import entity.MovieRental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.MovieRentalRepository;

@RestController
@RequestMapping("/api/movie-rentals")
public class MovieRentalController {

    @Autowired
    private MovieRentalRepository movieRentalRepository;

    @GetMapping
    public Flux<MovieRental> getAllMovieRentals() {
        return movieRentalRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<MovieRental> getMovieRentalById(@PathVariable String id) {
        return movieRentalRepository.findById(id);
    }

    @PostMapping
    public Mono<MovieRental> createMovieRental(@RequestBody MovieRental movieRental) {
        return movieRentalRepository.save(movieRental);
    }

    @PutMapping("/{id}")
    public Mono<MovieRental> updateMovieRental(@PathVariable String id, @RequestBody MovieRental movieRental) {
        movieRental.setId(id); // Ensure the ID is set
        return movieRentalRepository.save(movieRental);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMovieRental(@PathVariable String id) {
        return movieRentalRepository.deleteById(id);
    }
}
