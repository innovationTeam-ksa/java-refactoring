package com.innovationteam.task.exceptions;

public class CustomerNotFound extends RuntimeException {
    public CustomerNotFound(String id) {
        super("Customer with id " + id + " not found");
    }
}
