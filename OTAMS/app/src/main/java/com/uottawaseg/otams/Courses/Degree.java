package com.uottawaseg.otams.Courses

public enum Degree {
    NONE(0), HIGHSCHOOL(1), BACHELORS(2),
    MASTERS(3), DOCTORATE(4);

    private int _value;
    Degree(int value) {
        _value = value;
    }
    public int getValue() {
        return _value;
    }

    public String toString() {
        return switch(getValue()) {
            case 0 -> "NONE";
            case 1 -> "HIGHSCHOOL";
            case 2 -> "BACHELORS";
            case 3 -> "MASTERS";
            case 4 -> "DOCTORATE";
            default -> "UNKNOWN";
        }
    }
}