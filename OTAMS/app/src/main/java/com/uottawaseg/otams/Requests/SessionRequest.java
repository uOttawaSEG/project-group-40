package com.uottawaseg.otams.Requests;

import java.time.OffsetDateTime;

public class SessionRequest implements Request {

    private final String _student;
    private final String _tutor;
    private final OffsetDateTime _startTime;
    private final OffsetDateTime _endTime;
    private final OffsetDateTime _date;
    private RequestStatus _status;
    private final RequestType _type;

    // Constructor
    public SessionRequest(String student, String tutor, OffsetDateTime startTime, OffsetDateTime endTime, OffsetDateTime date) {
        if (student == null || tutor == null || startTime == null || endTime == null || date == null) {
            throw new IllegalArgumentException("Do not leave anything null. :(");
        }

        _student = student;
        _tutor = tutor;
        _startTime = startTime;
        _endTime = endTime;
        _date = date;
        _status = RequestStatus.PENDING;
        _type = RequestType.Unknown;
    }

    // Interface methods
    // Again, I see that u add @Override so I did it
    @Override
    public RequestStatus GetRequestStatus() {
        return _status;
    }

    @Override
    public RequestType GetRequestType() {
        return _type;
    }

    @Override
    public void AcceptRequest() {
        _status = RequestStatus.ACCEPTED;
    }

    @Override
    public void DeclineRequest() {
        _status = RequestStatus.DENIED;
    }

    @Override
    public String toString() {
        return "SessionRequest{" +
                "Student: " + _student +
                ", Tutor: " + _tutor +
                ", Date: " + _date +
                ", Start time: " + _startTime +
                ", End time: " + _endTime +
                ", Status: " + _status +
                ", Type: " + _type +
                "}";
    }

    public String getStudent() {
        return _student;
    }

    public String getTutor() {
        return _tutor;
    }

    public OffsetDateTime getStartTime() {
        return _startTime;
    }

    public OffsetDateTime getEndTime() {
        return _endTime;
    }

    public OffsetDateTime getDate() {
        return _date;
    }

}
