package com.innovationTeam.refactoring.utils;

public final class Constants {

    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }

    public static final String RENTAL_RECORD_HEADER_FORMAT = "Rental Record for %s\n";
    public static final String RENTAL_INFO_FORMAT = "\t%s\t%s\n";
    public static final String AMOUNT_OWED_FORMAT = "Amount owed is %s\n";
    public static final String FREQUENT_POINTS_FORMAT = "You earned %s frequent points\n";

    public static class UserConstants {
        private UserConstants() {
            throw new AssertionError("UserConstants class should not be instantiated");
        }

        // Error messages
        public static final String CUSTOMER_ID_NULL_ERROR = "Customer ID must not be null";
        public static final String CUSTOMER_NOT_FOUND_ERROR = "Customer with ID %d not found";

        // Validation messages
        public static final String CUSTOMER_NAME_NOT_EMPTY = "Customer name must not be empty";
        public static final String CUSTOMER_REQUEST_NULL_ERROR = "Customer request must not be null";
    }


    public final class MovieConstants {
        private MovieConstants() {
            throw new AssertionError("MovieConstants class should not be instantiated");
        }

        // Error messages
        public static final String MOVIE_NOT_FOUND_ERROR = "Movie not found with id: %d";

        public static final String MOVIE_ID_NOT_NULL_ERROR = "Movie ID must not be null";
    }

}
