package repository;

import entity.MovieRental;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

@Repository
public interface MovieRentalRepository extends ReactiveMongoRepository<MovieRental, String> {

}
