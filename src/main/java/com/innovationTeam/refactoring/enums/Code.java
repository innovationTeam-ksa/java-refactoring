package com.innovationTeam.refactoring.enums;

public enum Code {
    REGULAR("regular"),
    NEW("new"),
    CHILDRENS("childrens");

    private final String name;

    Code(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
