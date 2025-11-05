package com.example.tutoring;


import java.time.DayOfWeek;
import java.time.Duration;
import java.time.OffsetTime;
import java.util.Objects;


/**
* Represents a tutor's recurring weekly availability window (same day-of-week each week).
*/
public final class Availability {
private final boolean _autoApprove;
private final OffsetTime _startTime;
private final OffsetTime _endTime;
private final DayOfWeek _day;


/**
* ctor(start, end, day, autoApprove)
* Initializes our class variables and validates that the window is at least 30 minutes.
*
* @throws IllegalArgumentException if start/end/day are null, end is not after start,
* or the gap is < 30 minutes.
*/
public Availability(OffsetTime start, OffsetTime end, DayOfWeek day, boolean autoApprove) {
this._startTime = Objects.requireNonNull(start, "start cannot be null");
this._endTime = Objects.requireNonNull(end, "end cannot be null");
this._day = Objects.requireNonNull(day, "day cannot be null");
this._autoApprove = autoApprove;


long minutes = Duration.between(_startTime, _endTime).toMinutes();
if (minutes < 30) {
throw new IllegalArgumentException("Availability must be at least 30 minutes and end must be after start");
}
}


public boolean getAutoApprove() { return _autoApprove; }
public OffsetTime getStart() { return _startTime; }
public OffsetTime getEnd() { return _endTime; }
public DayOfWeek getDay() { return _day; }


@Override
public String toString() {
return "Availability{" +
"day=" + _day +
", start=" + _startTime +
", end=" + _endTime +
", autoApprove=" + _autoApprove +
'}';
}


@Override
public boolean equals(Object o) {
if (this == o) return true;
if (!(o instanceof Availability)) return false;
Availability that = (Availability) o;
}}
