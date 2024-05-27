package com.innovationTeam.refactoring.mapper;

import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.enums.Code;
import com.innovationTeam.refactoring.model.request.MovieRequestDto;
import com.innovationTeam.refactoring.model.response.MovieRentalResponse;
import com.innovationTeam.refactoring.model.response.MovieResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)

public class MovieMapperTest {

    private final MovieMapper mapper = Mappers.getMapper(MovieMapper.class);

    @Test
    void shouldMapMovieRequestDtoToMovie() {
        MovieRequestDto dto = new MovieRequestDto("Movie Title", Code.REGULAR);
        Movie movie = mapper.mapToMovie(dto);

        assertEquals(dto.getTitle(), movie.getTitle());
        assertEquals(dto.getCode(), movie.getCode());
    }

    @Test
    void shouldMapMovieToMovieResponse() {
        Movie movie = new Movie(1L, "Movie Title", Code.REGULAR);

        MovieResponse response = mapper.mapToMovieResponse(movie);


        assertEquals(movie.getTitle(), response.getTitle());
        assertEquals(movie.getCode().name(), response.getCode());
    }

    @Test
    void shouldMapMovieRentalToMovieRentalResponse() {

        Movie movie = new Movie(1L, "Movie Title", Code.REGULAR);
        MovieRental rental = new MovieRental(1L, movie, null, 5L);

        MovieRentalResponse response = mapper.mapToMovieRentalResponse(rental);

        assertEquals(movie.getTitle(), response.getMovie().getTitle());
        assertEquals(rental.getDays().intValue(), response.getDays());
    }

    @Test
    void shouldReturnNullWhenMappingNullMovieRequestDto() {
        Movie movie = mapper.mapToMovie(null);

        assertNull(movie);
    }

    @Test
    void shouldReturnNullWhenMappingNullMovie() {
        MovieResponse response = mapper.mapToMovieResponse(null);

        assertNull(response);
    }

    @Test
    void shouldReturnNullWhenMappingNullMovieRental() {
        MovieRentalResponse response = mapper.mapToMovieRentalResponse(null);

        assertNull(response);
    }
}
