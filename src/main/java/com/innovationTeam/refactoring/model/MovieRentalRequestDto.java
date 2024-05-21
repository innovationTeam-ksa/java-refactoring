package com.innovationTeam.refactoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRentalRequestDto {
    private int customerId;
    private int movieId;
    private int days;
}
