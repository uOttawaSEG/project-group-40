package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.Requests.Availability;
import com.uottawaseg.otams.R;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

public class AddAvailability extends AppCompatActivity {

    private DayOfWeek selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tutor_add_availability);

        // grab all the stuff from the layout
        EditText startTimeInput = findViewById(R.id.startTime_input);
        EditText endTimeInput = findViewById(R.id.endTime_input);

        CheckBox autoApproveCheckbox = findViewById(R.id.checkBox2);

        Spinner dateInput = findViewById(R.id.date_input);

        var dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DayOfWeek.values());
        dateAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        dateInput.setAdapter(dateAdapter);


        Button addAvailabilityButton = findViewById(R.id.button2);
        Button viewCalendarButton = findViewById(R.id.button);

        // load existing slots from database pls
        // when add availability button is clicked
        addAvailabilityButton.setOnClickListener(view -> {
            String startTimeStr = startTimeInput.getText().toString().trim();
            String endTimeStr = endTimeInput.getText().toString().trim();
            boolean autoApprove = autoApproveCheckbox.isChecked();

            if (validateInput(startTimeStr, endTimeStr)) {
                // make the new slot;
                var tutor = (Tutor) LoginManager.getCurrentAccount();
                OffsetTime startTime = getTime(startTimeStr);
                OffsetTime endTime = getTime(endTimeStr);
                Availability avail= new Availability(autoApprove, startTime, endTime, selectedDay, tutor.getUsername());

                tutor.AddAvailability(avail);

                Toast.makeText(this, "Availability added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewCalendarButton.setOnClickListener(view ->
                startActivity(new Intent(this, TutorWeeklyViewActivity.class)));

        dateInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

            @Override
            public void onItemSelected(AdapterView parent, View v, int position, long id) {
                selectedDay = DayOfWeek.valueOf(parent.getItemAtPosition(position).toString());
            }
        });
    }

    //From Daniil: rewrote this method
    private OffsetTime getTime(String timeStr) {
        String[] split= timeStr.split(":");
        int hour= Integer.parseInt(split[0]);
        int minute= Integer.parseInt(split[1]);
        return OffsetTime.of(hour, minute, 0, 0, ZonedDateTime.now().getOffset());
    }

    private boolean validateInput(String startTimeStr, String endTimeStr) {
        // make sure nothing is empty
        if (selectedDay == null) {
            Toast.makeText(this, "Please enter a date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
            Toast.makeText(this, "Please enter start and end times", Toast.LENGTH_SHORT).show();
            return false;
        }
//
//        LocalDate date;
//        try {
//            date = LocalDate.parse(dateStr);
//        } catch (Exception e) {
//            Toast.makeText(this, "Invalid date format (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        if (!date.isAfter(LocalDate.now())) {
//            Toast.makeText(this, "Cannot select a past date", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        // check time format cause sometimes it's weird
        if (isInvalidTimeFormat(startTimeStr)) {
            Toast.makeText(this, "Start time must be in HH:MM format (ex., 09:00)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isInvalidTimeFormat(endTimeStr)) {
            Toast.makeText(this, "End time must be in HH:MM format (ex., 17:00)", Toast.LENGTH_SHORT).show();
            return false;
        }

        // times are in 30mins increment
        if (isInvalidIncrement(startTimeStr)) {
            Toast.makeText(this, "Start time must be in 30-minute increments (ex., 09:00, 09:30)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isInvalidIncrement(endTimeStr)) {
            Toast.makeText(this, "End time must be in 30-minute increments (ex., 09:00, 09:30)", Toast.LENGTH_SHORT).show();
            return false;
        }

        // end time has to be after start time obv
        if (!isEndTimeAfterStartTime(startTimeStr, endTimeStr)) {
            Toast.makeText(this, "End time must be after start time", Toast.LENGTH_SHORT).show();
            return false;
        }

        // check if this overlaps with existing ones
        if (hasOverlap(selectedDay, startTimeStr, endTimeStr)) {
            Toast.makeText(this, "This time slot overlaps with an existing availability", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private int timeToMinutes(String timeStr) {
        String[] parts = timeStr.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    private int timeToMinutes(OffsetTime time) {
        return time.getHour() * 60 + time.getMinute();
    }

    private boolean isInvalidTimeFormat(String timeStr) {
        String[] parts = timeStr.split(":");
        if (parts.length != 2) return true;
        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            if (hours < 0 || hours > 23) return true;
            return minutes < 0 || minutes > 59;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private boolean isInvalidIncrement(String timeStr) {
        String[] parts = timeStr.split(":");
        int minutes = Integer.parseInt(parts[1]);
        return minutes != 0 && minutes != 30;
    }

    private boolean isEndTimeAfterStartTime(String startTimeStr, String endTimeStr) {
        int startTotalMinutes = timeToMinutes(startTimeStr);
        int endTotalMinutes = timeToMinutes(endTimeStr);
        return endTotalMinutes > startTotalMinutes;
    }


    private boolean hasOverlap(DayOfWeek date, String startTimeStr, String endTimeStr) {
        var availabilities = ((Tutor) LoginManager.getCurrentAccount()).getAvailabilities();
        int newStart = timeToMinutes(getTime(startTimeStr));
        int newEnd = timeToMinutes(getTime(endTimeStr));

        for (Availability slot : availabilities) {
            if (!slot.getDay().equals(date)) continue;

            int existingStart = timeToMinutes(slot.getStart());
            int existingEnd = timeToMinutes(slot.getEnd());

            if (newStart < existingEnd && newEnd > existingStart) return true;
        }
        return false;
    }

//    private LocalDate getDateFromStr(String s) {
//        return LocalDate.parse(s);
//    }
}