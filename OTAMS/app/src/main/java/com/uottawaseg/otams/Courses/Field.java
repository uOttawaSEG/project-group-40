// Feel free to add to this, just no sub-categories
// ie, mechanical engineering vs civil engineering
// This is only for degrees, not for students
package com.uottawaseg.otams.Courses;

import org.jetbrains.annotations.NotNull;

public enum Field {
    // STEM
    SCIENCE(0), ENGINEERING(1), MATHEMATICS(2),

    // MEDICINE
    MEDICINE(3),

    // ARTS
    // Do art PhDs exist?
    ARTS(4),

    // BUSINESS
    // FAKE DEGREES
    BUSINESS(5),

    // This is just for if there's an error somewhere
    UNKNOWN(-1);

    private final int _value;
    Field(int value) {
        _value = value;
    }

    public static String[] getValues() {
        return new String[] { "Science", "Engineering", "Mathematics", "Medicine", "Arts", "Business" };
    }

    public int getValue() {
        return _value;
    }
    @NotNull
    public String toString() {
        switch (_value) {
            case 0:
                return "Science";
            case 1:
                return "Engineering";
            case 2:
                return "Mathematics";
            case 3:
                return "Medicine";
            case 4:
                return "Art";
            case 5:
                return "Business";
            default:
                return "UNKNOWN";
        }
    }
    public static Field fromString(String s) {
        switch(s.toUpperCase()) {
            case "SCIENCE":
                return SCIENCE;
            case "ENGINEERING":
                return ENGINEERING;
            case "MATHEMATICS":
                return MATHEMATICS;
            case "MEDICINE":
                return MEDICINE;
            case "ART":
                return ARTS;
            case "BUSINESS":
                return BUSINESS;
            default:
                return UNKNOWN;
        }
    }
}