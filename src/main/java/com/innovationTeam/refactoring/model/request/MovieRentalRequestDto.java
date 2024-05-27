package com.innovationTeam.refactoring.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRentalRequestDto {
    private Long customerId;
    private Long movieId;
    private Long days;
}
