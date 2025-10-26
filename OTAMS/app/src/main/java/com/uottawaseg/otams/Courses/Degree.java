package com.uottawaseg.otams.Courses;

import androidx.annotation.NonNull;

public enum Degree {
    NONE(0), HIGHSCHOOL(1), BACHELORS(2),
    MASTERS(3), DOCTORATE(4), UNKNOWN(-1);

    private final int _value;
    Degree(int value) {
        _value = value;
    }
    public int getValue() {
        return _value;
    }

    /**
     * @return The string associated with the current Degree
     */
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

    /**
     * @param s The string to transform into a Degree
     * @return The Degree associated with the given string
     */
    public static Degree fromString(String s) {
        switch (s.toUpperCase()) {
            case "NONE":
                return NONE;
            case "HIGHSCHOOL":
                return HIGHSCHOOL;
            case "BACHELORS":
                return BACHELORS;
            case "MASTERS":
                return MASTERS;
            case "DOCTORATE":
                return DOCTORATE;

            default:
                return UNKNOWN;
        }
    }

    /**
     * @return The array of values, as strings.
     */
    public static String[] getValues() {
        return new String[] {"NONE", "HIGHSCHOOL", "BACHELORS", "MASTERS", "DOCTORATE"};
    }
}