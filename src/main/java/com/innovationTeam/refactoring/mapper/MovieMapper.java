package com.innovationTeam.refactoring.mapper;

import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.model.request.MovieRequestDto;
import com.innovationTeam.refactoring.model.response.MovieRentalResponse;
import com.innovationTeam.refactoring.model.response.MovieResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    Movie mapToMovie(MovieRequestDto movieRequestDto);

    MovieResponse mapToMovieResponse(Movie movie);

    @Mappings({
            @Mapping(source = "movie.title", target = "movie.title"),
            @Mapping(source = "movie.code", target = "movie.code"),
            @Mapping(source = "days", target = "days")
    })
    MovieRentalResponse mapToMovieRentalResponse(MovieRental movieRental);

}
