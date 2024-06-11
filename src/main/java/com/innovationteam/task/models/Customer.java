package com.innovationteam.task.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "customers")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    public Customer(String name, List<MovieRental> rentals) {
        this.name = name;
        this.rentals = rentals;
    }

    @Id
    private String id;
    private String name;
    private List<MovieRental> rentals;

}
