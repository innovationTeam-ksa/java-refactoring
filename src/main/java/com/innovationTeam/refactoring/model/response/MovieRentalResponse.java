package com.innovationTeam.refactoring.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRentalResponse {

    @JsonProperty("movie")
    private MovieResponse movie;

    @JsonProperty("days")
    private int days;
}
