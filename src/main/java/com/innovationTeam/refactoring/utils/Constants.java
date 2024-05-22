package com.innovationTeam.refactoring.utils;

public final class Constants {

    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }

    public static final String RENTAL_RECORD_HEADER_FORMAT = "Rental Record for %s\n";
    public static final String RENTAL_INFO_FORMAT = "\t%s\t%s\n";
    public static final String AMOUNT_OWED_FORMAT = "Amount owed is %s\n";
    public static final String FREQUENT_POINTS_FORMAT = "You earned %s frequent points\n";

}
