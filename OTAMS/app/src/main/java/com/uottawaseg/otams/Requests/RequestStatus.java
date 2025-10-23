package com.uottawaseg.otams.Requests;

public enum RequestStatus {
    PENDING(1), DENIED(2), ACCEPTED(3), UNKNOWN(-1);
    private int _val;
    private RequestStatus(int value) {
        _val = value;
    }

    public int GetValue() { return _val; }
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
}
