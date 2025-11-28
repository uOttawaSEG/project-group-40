package com.uottawaseg.otams.Requests;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.Duration;
import java.util.List;

public class Availability {
    private final boolean _autoApprove;
    private final OffsetTime _startTime;
    private final OffsetTime _endTime;
    private final DayOfWeek _date;
    private List<SessionRequest> sessions;

    // Constructor
    public Availability(boolean autoApprove, OffsetTime startTime, OffsetTime endTime, DayOfWeek date) {
        if (startTime == null || endTime == null || date == null) {
            throw new IllegalArgumentException("Do not leave anything null. :(");
        }

        Duration duration = Duration.between(startTime, endTime);
        if (duration.toMinutes() < 30) {
            throw new IllegalArgumentException("Availability must have at least a 30-minute gap between start and end.");
        }

        _autoApprove = autoApprove;
        _startTime = startTime;
        _endTime = endTime;
        _date= date;
    }

    public boolean getAutoApprove() {
        return _autoApprove;
    }

    public OffsetTime getStart() {
        return _startTime;
    }

    public OffsetTime getEnd() {
        return _endTime;
    }

    public DayOfWeek getDate() {//From Daniil: Uses LocalDate _date instead of DayOfWeek _day
        return _date;
    }


    @Override
    public String toString() {
        return "Availability{" +
                "Date: " + _date +
                ", Start time: " + _startTime +
                ", End time: " + _endTime +
                ", Auto approve: " + _autoApprove +
                "}";
    }

}
