package com.innovationTeam.refactoring.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieRentalResponse {

    @JsonProperty("movie")
    private MovieResponse movie;

    @JsonProperty("days")
    private int days;
}
