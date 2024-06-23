package controller;


import entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.MovieRepository;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping
    public Flux<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Movie> getMovieById(@PathVariable String id) {
        return movieRepository.findById(id);
    }

    @PostMapping
    public Mono<Movie> createMovie(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    @PutMapping("/{id}")
    public Mono<Movie> updateMovie(@PathVariable String id, @RequestBody Movie movie) {
        movie.setId(id); // Ensure the ID is set
        return movieRepository.save(movie);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMovie(@PathVariable String id) {
        return movieRepository.deleteById(id);
    }
}
