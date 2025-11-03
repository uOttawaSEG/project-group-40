package com.uottawaseg.otams.Requests;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.Duration;

public class Availability {

    private boolean _autoApprove;
    private OffsetTime _startTime;
    private OffsetTime _endTime;
    private DayOfWeek _day;

    // Constructor
    public Availability(boolean autoApprove, OffsetTime startTime, OffsetTime endTime, DayOfWeek day) {
        if (startTime == null || endTime == null || day == null) {
            throw new IllegalArgumentException("Do not leave anything null. :(");
        }

        // I'm getting an error with this part "Call requires API level 26 (current min is 24)"
        // Looked online and saw these getting used w/ OffsetTime
        // Internet suggested modifying gradle to fix this but tbh its sketchy asf
        Duration duration = Duration.between(startTime, endTime);
        if (duration.toMinutes() < 30) {
            throw new IllegalArgumentException("Availability must have at least a 30-minute gap between start and end.");
        }

        _autoApprove = autoApprove;
        _startTime = startTime;
        _endTime = endTime;
        _day = day;
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

    public DayOfWeek getDay() {
        return _day;
    }

    // Note to seb: u always put override so i figure i would put it too
    // Also idk if u wanna put StringBuilder like u did with StudentAccountRequest & TutorAccountRequest
    @Override
    public String toString() {
        return "Availability{" +
                "Day: " + _day +
                ", Start time: " + _startTime +
                ", End time: " + _endTime +
                ", Auto approve: " + _autoApprove +
                "}";
    }

}
