package com.innovationteam.task.exceptions;

public class MovieNotFound extends RuntimeException {
    public MovieNotFound(String movieId) {
        super("Movie with id " + movieId + " not found");
    }
}
