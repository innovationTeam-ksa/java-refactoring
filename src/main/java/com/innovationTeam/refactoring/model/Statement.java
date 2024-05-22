package com.innovationTeam.refactoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statement {
    private List<RentalStatementInfo> rentalInfoList;
    private double totalAmount;
    private int frequentEnterPoints;
    private String customerName;
}
