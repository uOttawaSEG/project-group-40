package com.uottawaseg.otams.Requests;

import androidx.annotation.Nullable;

import java.time.OffsetDateTime;
import java.time.OffsetTime;

public class SessionRequest implements Request {

    private final String _student;
    private final String _tutor;
    private final OffsetTime _startTime;
    private final OffsetTime _endTime;
    private final int _day;
    private final int _month;
    private final int _year;
    private RequestStatus _status;
    private final RequestType _type;

    // Constructor

    /**
     * @param student Students Username
     * @param tutor Tutors username
     * @param startTime The starting time
     * @param endTime The ending time
     * @param day The day of the month
     * @param month The month of the year
     * @param year The year
     */
    public SessionRequest(String student, String tutor, OffsetTime startTime, OffsetTime endTime, int day, int month, int year) {
        if (student == null || tutor == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("Do not leave anything null. :(");
        }

        _student = student;
        _tutor = tutor;
        _startTime = startTime;
        _endTime = endTime;
        _status = RequestStatus.PENDING;
        _type = RequestType.TutorSessionRequest;

        _day = day;
        _month = month;
        _year = year;

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
                ", Date: " + getDate() +
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
    //This one is needed for the DB
    public RequestStatus getStatus() {
        return _status;
    }
    public OffsetTime getStartTime() {
        return _startTime;
    }

    public OffsetTime getEndTime() {
        return _endTime;
    }

    public OffsetDateTime getDate() {
        return OffsetDateTime.of(_year, _month, _day, 0, 0, 0, 0, _startTime.getOffset());
    }

    public void setStatus(RequestStatus requestStatus) {
        _status = requestStatus;
    }

    public boolean equals(SessionRequest other) {
        return         _student.equals(other._student) && _tutor.equals(other._tutor)
                &&  getDate().isEqual(other.getDate()) &&   _status == other._status;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
