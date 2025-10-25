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

    public String toString() {

        switch(_val) {
            case 0: return "Request Type: Student Account Creation";
            case 1: return "Request Type: Tutor Account Creation";
            default: return "UNKNOWN REQUEST TYPE";
        }
    }

    public static RequestType fromString(String str) {
        switch(str.toUpperCase()) {
            case "TUTORACCOUNTCREATION": return TutorAccountCreation;
            case "STUDENTACCOUNTCREATION": return StudentAccountCreation;
            default: return Unknown;
        }
    }

}
