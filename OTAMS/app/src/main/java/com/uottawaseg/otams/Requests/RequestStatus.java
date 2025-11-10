package com.uottawaseg.otams.Requests;

public enum RequestStatus {
    PENDING(1), DENIED(2), ACCEPTED(3), UNKNOWN(-1);
    private int _val;
    private RequestStatus(int value) {
        _val = value;
    }

    public int GetValue() { return _val; }

    /**
     * @return The string associated with the current request status
     */
    public String toString() {
        switch(_val) {
            case 1:
                return "PENDING";
            case 2:
                return "DENIED";
            case 3:
                return "ACCEPTED";
            default: return "Unknown";
        }
    }

    public static RequestStatus fromString(String s) {
        switch(s.toUpperCase()) {
            case "PENDING":
                return PENDING;
            case "DENIED":
                return DENIED;
            case "ACCEPTED":
                return ACCEPTED;
            default:
                return UNKNOWN;
        }
    }
}
