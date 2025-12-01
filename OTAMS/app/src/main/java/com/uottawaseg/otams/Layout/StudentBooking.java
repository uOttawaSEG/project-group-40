package com.uottawaseg.otams.Layout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Courses.Course;
import com.uottawaseg.otams.Database.StudentSessionManager;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.Availability;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

public class StudentBooking extends AppCompatActivity {
    private static String infoStr = "";
    private static Availability tutAvail;
    private static OffsetDateTime day;
    private static Course course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_booking);

        var startTime = (TextView) findViewById(R.id.start_time);
        var endTime = (TextView) findViewById(R.id.end_time);
        var info = (TextView) findViewById(R.id.info_text_session_booking);
        var req = (Button) findViewById(R.id.request_button);
        info.setText(infoStr);

        req.setOnClickListener(v -> {
            if(ValidateInput(startTime, endTime)) {
                StudentSessionManager.RequestSession(
                        GetTime(startTime.getText().toString()), GetTime(endTime.getText().toString()), tutAvail, day, course);
            }
        });
    }

    private boolean ValidateInput(TextView startTime, TextView endTime) {
        var start = GetTime(startTime.getText().toString());
        if(start == null) {
            Toast.makeText(this, "Invalid start time format, please use HH:MM", Toast.LENGTH_SHORT).show();
            return false;
        }

        var end = GetTime(endTime.getText().toString());
        if(end == null) {
            Toast.makeText(this, "Invalid end time format, please use HH:MM", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(start.isAfter(end)){
            Toast.makeText(this, "Requested end time cannot be before the start time.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(start.isBefore(tutAvail.getStart())){
            Toast.makeText(this, "Requested start time cannot be before the start of the tutors availability", Toast.LENGTH_LONG).show();
            return false;
        }

        if(end.isAfter(tutAvail.getEnd())){
            Toast.makeText(this, "Requested end time cannot be after the end of the tutors availability", Toast.LENGTH_LONG).show();
            return false;
        }

        var duration = Duration.between(start, end);
        // Minimum 30 minutes, 30 minutes * 60 seconds/minute = 30 * 60 seconds
        if(duration.getSeconds() < 30 * 60 ) {
            Toast.makeText(this, "The time difference between the start and the end must be at least 30 minutes", Toast.LENGTH_LONG).show();
            return false;
        }
        // Start hour has to be less than end Hour

        return true;
    }

    private OffsetTime GetTime(String str) {
        var splitStr = str.split(":");
        if(str.length() > 2)
            return null;
        int hour, mins;
        try {
            mins = Integer.parseInt(splitStr[1]);
            hour = Integer.parseInt(splitStr[0]);
        } catch (Exception ignored) {
            return null;
        }
        return OffsetTime.of(hour, mins, 0, 0, OffsetTime.now().getOffset());

    }

    public static void SetupBooking(String infoString, Availability tutorAvail, Course course) {
        infoStr = infoString;
        tutAvail = tutorAvail;
        StudentBooking.course = course;
        day = generateDate();
    }

    private static OffsetDateTime generateDate() {
        var dayOfWeek = tutAvail.getDay();
        // We need to calculate what day the next day of week falls on
        var today = DayOfWeek.from(OffsetTime.now());
        if(dayOfWeek == today) {
            // Plan out a week in advanced
            return OffsetDateTime.now().plusHours(24L * 7);
        }


        int difference;
        var values = DayOfWeek.values();

        var todayNum = 0;
        var finalNum = 0;
        // Two scenarios
        // finalNum < todayNum
        // 1 2 3 4 5 6 7
        //     f   t
        // Diff = 7 - t + f
        // or
        // finalNum > todayNum
        // 1 2 3 4 5 6 7
        //   t     f
        // diff = f - t
        for(int i = 0; i < values.length; i++) {
            if(values[i] == dayOfWeek) {
                finalNum = i;
            } else if (values[i] == today) {
                todayNum = i;
            }
        }

        if(finalNum < todayNum) {
            difference = 7 - todayNum + finalNum;
        } else {
            difference = finalNum - todayNum;
        }

        return OffsetDateTime.now().plusHours(difference * 24L);
    }
}

