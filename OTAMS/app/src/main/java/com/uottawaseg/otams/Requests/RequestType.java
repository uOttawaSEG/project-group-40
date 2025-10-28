package com.uottawaseg.otams.Requests;

public enum RequestType {
    Unknown(-1),
    StudentAccountCreation(0),
    TutorAccountCreation(1);
    private int _val;
    private RequestType(int value) {
        _val = value;
    }

    public int getValue() { return _val; }

    /**
     * @return A string for the given request type.
     */
    public String toString() {

        switch(_val) {
            case 0: return "Request Type: Student Account Creation";
            case 1: return "Request Type: Tutor Account Creation";
            default: return "UNKNOWN REQUEST TYPE";
        }
    }

    /**
     * @param str the string to transform into a RequestType
     * @return The RequestType associated with the string.
     */
    public static RequestType fromString(String str) {
        switch(str.toUpperCase()) {
            case "TUTORACCOUNTCREATION": return TutorAccountCreation;
            case "STUDENTACCOUNTCREATION": return StudentAccountCreation;
            default: return Unknown;
        }
    }

}
