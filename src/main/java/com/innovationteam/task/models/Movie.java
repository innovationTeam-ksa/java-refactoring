package com.innovationteam.task.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "movies")
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    public Movie(String title, String code) {
        this.title = title;
        this.code = code;
    }

    @Id
    private String id;
    private String title;
    private String code;

}
