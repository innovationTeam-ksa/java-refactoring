package repository;

import entity.Movie;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

@Repository
public interface MovieRepository extends ReactiveMongoRepository<Movie, String> {

}
