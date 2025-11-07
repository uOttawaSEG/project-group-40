package com.uottawaseg.otams.Layout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddAvailability extends AppCompatActivity {

    //should store all the existing slots, database pls
    private List<AvailabilitySlot> existingSlots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.tutor_add_availability);

        // grab all the stuff from the layout
        EditText dateInput = findViewById(R.id.date_input);
        EditText startTimeInput = findViewById(R.id.startTime_input);
        EditText endTimeInput = findViewById(R.id.endTime_input);
        CheckBox autoApproveCheckbox = findViewById(R.id.checkBox2);
        Button addAvailabilityButton = findViewById(R.id.button2);
        Button viewCalendarButton = findViewById(R.id.button);

        // load existing slots from database pls

        // when add availability button is clicked
        addAvailabilityButton.setOnClickListener(view -> {
            String dateStr = dateInput.getText().toString().trim();
            String startTimeStr = startTimeInput.getText().toString().trim();
            String endTimeStr = endTimeInput.getText().toString().trim();
            boolean autoApprove = autoApproveCheckbox.isChecked();

            if (validateInput(dateStr, startTimeStr, endTimeStr)) {
                // make the new slot
                AvailabilitySlot newSlot = new AvailabilitySlot(dateStr, startTimeStr, endTimeStr, autoApprove);

                //save this in database i think

                Toast.makeText(this, "Availability added successfully!", Toast.LENGTH_SHORT).show();

                //go back to the previous page
                finish();
            }
        });

        //view calendar button, not implemented yet cause waiting Daniil
        viewCalendarButton.setOnClickListener(view -> {
            // make calendar view page
            Toast.makeText(this, "Calendar view coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInput(String dateStr, String startTimeStr, String endTimeStr) {
        //make sure nothing is empty
        if (dateStr.isEmpty()) {
            Toast.makeText(this, "Please enter a date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (startTimeStr.isEmpty()) {
            Toast.makeText(this, "Please enter a start time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (endTimeStr.isEmpty()) {
            Toast.makeText(this, "Please enter an end time", Toast.LENGTH_SHORT).show();
            return false;
        }

        //check if date is valid and not in the past
        if (!isValidDate(dateStr)) {
            return false;
        }

        //check time format cause sometimes it's weird
        if (!isValidTimeFormat(startTimeStr)) {
            Toast.makeText(this, "Start time must be in HH:MM format (e.g., 09:00)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidTimeFormat(endTimeStr)) {
            Toast.makeText(this, "End time must be in HH:MM format (e.g., 17:00)", Toast.LENGTH_SHORT).show();
            return false;
        }

        //times are in 30mins increment
        if (!isThirtyMinuteIncrement(startTimeStr)) {
            Toast.makeText(this, "Start time must be in 30-minute increments (e.g., 09:00, 09:30)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isThirtyMinuteIncrement(endTimeStr)) {
            Toast.makeText(this, "End time must be in 30-minute increments (e.g., 09:00, 09:30)", Toast.LENGTH_SHORT).show();
            return false;
        }

        //end time has to be after start time obv
        if (!isEndTimeAfterStartTime(startTimeStr, endTimeStr)) {
            Toast.makeText(this, "End time must be after start time", Toast.LENGTH_SHORT).show();
            return false;
        }

        //check if this overlaps with existing ones
        if (hasOverlap(dateStr, startTimeStr, endTimeStr)) {
            Toast.makeText(this, "This time slot overlaps with an existing availability", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.setLenient(false);

        try {
            Date inputDate = sdf.parse(dateStr);

            //get the date for tdy
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            //not allowed to go back in time
            if (inputDate.before(today.getTime())) {
                Toast.makeText(this, "Cannot select a date in the past", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Please use YYYY-MM-DD (e.g., 2024-12-25)", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isValidTimeFormat(String timeStr) {
        //check if it matches da format
        //split the : to get hours and minutes
        String[] parts = timeStr.split(":");

        //should have exactly 2 parts cause hours and minutes
        if (parts.length != 2) {
            return false;
        }

        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            //hours should be 0-23, minutes should be 0-59
            if (hours < 0 || hours > 23) {
                return false;
            }
            if (minutes < 0 || minutes > 59) {
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            //if it can't convert to numbers, it's not valid
            return false;
        }
    }


    private boolean isThirtyMinuteIncrement(String timeStr) {
        String[] parts = timeStr.split(":");
        int minutes = Integer.parseInt(parts[1]);

        //basically the minutes can only be 00 or 30 if that makes sense
        return minutes == 0 || minutes == 30;
    }

    private boolean isEndTimeAfterStartTime(String startTimeStr, String endTimeStr) {
        String[] startParts = startTimeStr.split(":");
        String[] endParts = endTimeStr.split(":");

        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);
        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);

        int startTotalMinutes = startHour * 60 + startMinute;
        int endTotalMinutes = endHour * 60 + endMinute;

        return endTotalMinutes > startTotalMinutes;
    }

    private boolean hasOverlap(String dateStr, String startTimeStr, String endTimeStr) {
        //check if new slot overlaps with ones on the same date
        for (AvailabilitySlot slot : existingSlots) {
            //skip if not the same date
            if (!slot.date.equals(dateStr)) {
                continue;
            }

            //convert the times to minutes to make the check easier
            int newStart = timeToMinutes(startTimeStr);
            int newEnd = timeToMinutes(endTimeStr);
            int existingStart = timeToMinutes(slot.startTime);
            int existingEnd = timeToMinutes(slot.endTime);

            //check for overlap
            //the times overlap if one starts before the other ends
            if (newStart < existingEnd && newEnd > existingStart) {
                return true;
            }
        }

        return false;
    }

    private int timeToMinutes(String timeStr) {
        String[] parts = timeStr.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    //holds the availability info
    //you guys are prob gonna have to replace w database stuff sorry
    public static class AvailabilitySlot {
        String date;
        String startTime;
        String endTime;
        boolean autoApprove;

        public AvailabilitySlot(String date, String startTime, String endTime, boolean autoApprove) {
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.autoApprove = autoApprove;
        }
    }
}

//does this even work..