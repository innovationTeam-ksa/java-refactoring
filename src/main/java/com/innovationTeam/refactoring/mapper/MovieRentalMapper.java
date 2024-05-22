package com.innovationTeam.refactoring.mapper;

import com.innovationTeam.refactoring.entity.MovieRental;
import com.innovationTeam.refactoring.model.request.MovieRentalRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MovieRentalMapper {
    MovieRentalMapper INSTANCE = Mappers.getMapper(MovieRentalMapper.class);

    MovieRental mapToMovieRental(MovieRentalRequestDto movieRentalRequestDto);
}
