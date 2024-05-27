package com.innovationTeam.refactoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalStatementInfo {
    String movieTitle;
    double amount;
}
