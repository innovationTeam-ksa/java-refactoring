package com.innovationTeam.refactoring.mapper;

import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.model.request.MovieRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    Movie mapToMovie(MovieRequestDto movieRequestDto);
}
