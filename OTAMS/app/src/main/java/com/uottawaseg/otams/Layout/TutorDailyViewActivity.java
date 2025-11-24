package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.Availability;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class TutorDailyViewActivity extends AppCompatActivity {

    private Button btnAddAvailability, btnViewUpcoming;
    private Button btnPrevMonth, btnNextMonth;
    private Button btnPrevDay, btnNextDay;
    private Button btnWeeklyView, btnViewAvailability, btnHomepage;
    private TextView monthText, dayText;

    private Calendar calendar;
    private CalendarEventManager eventManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_daily_view);

        calendar= Calendar.getInstance();
        eventManager= CalendarEventManager.getInstance();

        //btns
        btnAddAvailability= findViewById(R.id.btnAddAvailability);
        btnViewUpcoming= findViewById(R.id.btnViewUpcoming);
        btnPrevMonth= findViewById(R.id.btnPrevMonth);
        btnNextMonth= findViewById(R.id.btnNextMonth);
        btnPrevDay= findViewById(R.id.btnPrevDay);
        btnNextDay= findViewById(R.id.btnNextDay);
        btnWeeklyView= findViewById(R.id.btnWeeklyView);
        btnViewAvailability= findViewById(R.id.btnViewAvailability);
        btnHomepage= findViewById(R.id.btnHomepage);

        monthText= findViewById(R.id.monthText);
        dayText= findViewById(R.id.dayText);

        updateHeader();
        updateEvents();

        // btn listeners
        btnAddAvailability.setOnClickListener(v ->
                startActivity(new Intent(this, AddAvailability.class)));
        btnViewUpcoming.setOnClickListener(v ->
                startActivity(new Intent(this, TutorViewUpcoming.class)));
        btnPrevMonth.setOnClickListener(v -> changeMonth(-1));
        btnNextMonth.setOnClickListener(v -> changeMonth(1));
        btnPrevDay.setOnClickListener(v -> changeDay(-1));
        btnNextDay.setOnClickListener(v -> changeDay(1));
        btnWeeklyView.setOnClickListener(v ->
                startActivity(new Intent(this, TutorWeeklyViewActivity.class)));
        btnViewAvailability.setOnClickListener(v ->
                startActivity(new Intent(this, TutorViewAvailability.class)));
        btnHomepage.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));
    }

    private void updateHeader() {
        java.text.SimpleDateFormat monthFormat= new java.text.SimpleDateFormat("MMMM yyyy");
        monthText.setText(monthFormat.format(calendar.getTime()));
        int day= calendar.get(Calendar.DAY_OF_MONTH);
        java.text.SimpleDateFormat weekdayFormat= new java.text.SimpleDateFormat("EEEE");
        dayText.setText(weekdayFormat.format(calendar.getTime()) + ", " + getDayWithSuffix(day));
    }

    private void changeMonth(int offset) {
        calendar.add(Calendar.MONTH, offset);
        int maxDay= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (calendar.get(Calendar.DAY_OF_MONTH) > maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, maxDay);
        }
        updateHeader();
        updateEvents();
    }

    private void changeDay(int offset) {
        calendar.add(Calendar.DAY_OF_MONTH, offset);
        updateHeader();
        updateEvents();
    }

    private String getDayWithSuffix(int day) {
        if (day>=11 && day<=13) return day + "th";
        switch (day % 10) {
            case 1: return day + "st";
            case 2: return day + "nd";
            case 3: return day + "rd";
            default: return day + "th";
        }
    }

    private void updateEvents() {
        DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("HH:mm");
        LocalDate currentDate= LocalDate.of(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

        Tutor tutor= null;
        if (LoginManager.getCurrentAccount() instanceof Tutor) {
            tutor= (Tutor) LoginManager.getCurrentAccount();
        }

        if (tutor==null || tutor.getAvailabilities()==null) return;

        // clears previous daily slots
        for (int hour= 0; hour<24; hour++) {
            int slotResId= getResources().getIdentifier("slot_" + hour + "_daily", "id", getPackageName());
            FrameLayout slotView= findViewById(slotResId);
            if (slotView != null) slotView.removeAllViews();
        }

        // populates slots
        for (Availability slot : tutor.getAvailabilities()) {
            if (slot==null || slot.getDate()==null || slot.getStart()==null || slot.getEnd()==null) continue;
            if (!slot.getDate().equals(currentDate)) continue;

            int hour= slot.getStart().getHour();
            String slotKey= "slot_" + hour + "_daily";

            int slotResId= getResources().getIdentifier(slotKey, "id", getPackageName());
            FrameLayout slotView= findViewById(slotResId);

            if (slotView != null) {
                TextView tv= new TextView(this);
                String startTime= slot.getStart().format(timeFormatter);
                String endTime= slot.getEnd().format(timeFormatter);

                String displayText = startTime + " - " + endTime;

                // appends student info if booked
                if (slot.isBooked()) {
                    if (slot.getStudentFirstName() != null && slot.getStudentLastName() != null) {
                        displayText+= " (Booked by: " + slot.getStudentFirstName() + " " + slot.getStudentLastName() + ")";
                    } else {
                        displayText+= " (BOOKED)";
                    }
                }
                tv.setText(displayText);
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(8, 8, 8, 8);
                tv.setBackgroundResource(R.drawable.grid_cell_border);
                slotView.addView(tv);
                eventManager.addEvent(slotKey, new CalendarEventManager.Event("●",
                        "Availability " + displayText));
            }
        }
    }
}


















