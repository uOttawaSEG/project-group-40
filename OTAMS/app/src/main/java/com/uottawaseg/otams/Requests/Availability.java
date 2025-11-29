package com.uottawaseg.otams.Requests;

import androidx.annotation.NonNull;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.Duration;
import java.util.HashMap;

public class Availability {
    private final boolean _autoApprove;
    private final OffsetTime _startTime;
    private final OffsetTime _endTime;
    private final DayOfWeek _day;
    private final String _tutor;

    // Constructor
    public Availability(boolean autoApprove, OffsetTime startTime, OffsetTime endTime, DayOfWeek date, String tutorName) {
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
        _day = date;
        _tutor = tutorName;
    }

    public boolean getAutoApprove() {
        return _autoApprove;
    }

    public String getTutor() { return _tutor; }

    public OffsetTime getStart() {
        return _startTime;
    }

    public OffsetTime getEnd() {
        return _endTime;
    }
    public DayOfWeek getDay() {
        return _day;
    }

//    public HashMap<String, Object> JSONify() {
//        var map = new HashMap<String, Object>();
//        map.put("autoApprove", _autoApprove);
//        map.put("tutor", _tutor);
//        map.put("day", _day);
//        map.put("start", _startTime);
//        map.put("end", _endTime);
//        return map;
//    }

    @NonNull
    @Override
    public String toString() {
        return "Availability{" +
                "Day of week: " + _day +
                ", Start time: " + _startTime +
                ", End time: " + _endTime +
                ", Auto approve: " + _autoApprove +
                ", Tutor username: " + _tutor +
                "}";
    }

}
