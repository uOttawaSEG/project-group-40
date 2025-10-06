package com.uottawaseg.otams.Courses;

import androidx.annotation.NonNull;

public enum Degree {
    NONE(0), HIGHSCHOOL(1), BACHELORS(2),
    MASTERS(3), DOCTORATE(4);

    private final int _value;
    Degree(int value) {
        _value = value;
    }
    public int getValue() {
        return _value;
    }

    @NonNull
    public String toString() {
        switch (getValue()) {
            case 0:
                return "NONE";
            case 1:
                return "HIGHSCHOOL";
            case 2:
                return "BACHELORS";
            case 3:
                return "MASTERS";
            case 4:
                return "DOCTORATE";
            default:
                return "UNKNOWN";
        }
    }
}