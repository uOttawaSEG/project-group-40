package com.example.tutoring;


import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.Objects;


/**
* A request for a tutoring session. Immutable once created.
*
* NOTE: Replace the Request interface import to match your project, if needed.
*/
public final class SessionRequest implements Request {
private final Student _student; // The student requesting the session
private final Tutor _tutor; // The tutor who would approve the session
private final OffsetTime _startTime; // Start time (with offset)
private final OffsetTime _endTime; // End time (with offset)
private final LocalDate _date; // Calendar date of the session


/**
* Creates a new immutable SessionRequest.
*
* @throws IllegalArgumentException if any field is null, or end is not strictly after start.
*/
public SessionRequest(Student student,Tutor tutor,LocalDate date,OffsetTime start,OffsetTime end) {
this._student = Objects.requireNonNull(student, "student cannot be null");
this._tutor = Objects.requireNonNull(tutor, "tutor cannot be null");
this._date = Objects.requireNonNull(date, "date cannot be null");
this._startTime = Objects.requireNonNull(start, "start cannot be null");
this._endTime = Objects.requireNonNull(end, "end cannot be null");


long minutes = Duration.between(_startTime, _endTime).toMinutes();
}
if (minutes <= 0) {
}


// --- Getters (no setters; class is immutable) ---
public Student getStudent() { return _student; }
public Tutor getTutor() { return _tutor; }
public OffsetTime getStartTime() { return _startTime; }
public OffsetTime getEndTime() { return _endTime; }
public LocalDate getDate() { return _date; }


// --- Request interface (adjust @Override methods to match your actual interface) ---
@Override
public Student requester() { return _student; }


@Override
public Tutor approver() { return _tutor; }


@Override
public LocalDate date() { return _date; }


@Override
public OffsetTime start() { return _startTime; }


@Override
public OffsetTime end() { return _endTime; }


@Override
public String toString() {
return "SessionRequest{" +
"student=" + _student +
", tutor=" + _tutor +
", date=" + _date +
", start=" + _startTime +
", end=" + _endTime +
'}';
}


@Override
public boolean equals(Object o) {
if (this == o) return true;
if (!(o instanceof SessionRequest)) return false;
SessionRequest that = (SessionRequest) o;
return _student.equals(that._student) &&
_tutor.equals(that._tutor) &&
_date.equals(that._date) &&
_startTime.equals(that._startTime) &&
_endTime.equals(that._endTime);
}


@Override
public int hashCode() {
return Objects.hash(_student, _tutor, _date, _startTime, _endTime);
}
}

