package com.uottawaseg.otams.Requests;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.Duration;
import java.time.LocalDate;

public class Availability {

    private boolean _autoApprove;
    private OffsetTime _startTime;
    private OffsetTime _endTime;
    private LocalDate _date; //From Daniil: Uses LocalDate _date instead of DayOfWeek _day

    // Constructor
    public Availability(boolean autoApprove, OffsetTime startTime, OffsetTime endTime, LocalDate date) {
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
        _date= date;//From Daniil: Uses LocalDate _date instead of DayOfWeek _day
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

    public LocalDate getDate() {//From Daniil: Uses LocalDate _date instead of DayOfWeek _day
        return _date;
    }

    // Note to seb: u always put override so i figure i would put it too
    // Also idk if u wanna put StringBuilder like u did with StudentAccountRequest & TutorAccountRequest
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
