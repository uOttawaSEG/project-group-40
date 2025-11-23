package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.graphics.Color;
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
import java.util.Calendar;
import java.util.List;

public class TutorWeeklyViewActivity extends AppCompatActivity {

    private Button btnAddAvailability, btnViewUpcoming;
    private Button btnPrevMonth, btnNextMonth;
    private Button btnPrevWeek, btnNextWeek;
    private Button btnDailyView, btnViewAvailability, btnHomepage;
    private TextView monthLabel, weekLabel;

    private Calendar calendar;
    private int currentMonth;
    private int currentWeek;
    private CalendarEventManager eventManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_weekly_view);

        calendar= Calendar.getInstance();
        currentMonth= calendar.get(Calendar.MONTH) + 1;
        currentWeek= 1;
        eventManager= CalendarEventManager.getInstance();

        monthLabel= findViewById(R.id.monthLabel);
        weekLabel= findViewById(R.id.weekLabel);
        btnAddAvailability= findViewById(R.id.btnAddAvailability);
        btnViewUpcoming= findViewById(R.id.btnViewUpcoming);
        btnPrevMonth= findViewById(R.id.btnPrevMonth);
        btnNextMonth= findViewById(R.id.btnNextMonth);
        btnPrevWeek= findViewById(R.id.btnPrevWeek);
        btnNextWeek= findViewById(R.id.btnNextWeek);
        btnDailyView= findViewById(R.id.btnDailyView);
        btnViewAvailability= findViewById(R.id.btnViewAvailability);
        btnHomepage= findViewById(R.id.btnHomepage);

        updateHeaderLabels();
        updateCalendarView();

        btnAddAvailability.setOnClickListener(v ->
                startActivity(new Intent(this, AddAvailability.class)));
        btnViewUpcoming.setOnClickListener(v ->
                startActivity(new Intent(this, TutorViewUpcoming.class)));
        btnDailyView.setOnClickListener(v ->
                startActivity(new Intent(this, TutorDailyViewActivity.class)));
        btnViewAvailability.setOnClickListener(v ->
                startActivity(new Intent(this, TutorViewAvailability.class)));
        btnHomepage.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));
        btnNextMonth.setOnClickListener(v -> { incrMonth(); updateHeaderLabels(); updateCalendarView(); });
        btnPrevMonth.setOnClickListener(v -> { decrMonth(); updateHeaderLabels(); updateCalendarView(); });
        btnNextWeek.setOnClickListener(v -> { incrWeek(); updateHeaderLabels(); updateCalendarView(); });
        btnPrevWeek.setOnClickListener(v -> { decrWeek(); updateHeaderLabels(); updateCalendarView(); });
    }

    private void updateHeaderLabels() {
        monthLabel.setText(
                calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, java.util.Locale.getDefault()) + " " + calendar.get(Calendar.YEAR));
        weekLabel.setText("Week " + currentWeek);
    }

    private void incrWeek() {
        currentWeek++;
        if (currentWeek>4) {
            currentWeek= 1;
            incrMonth();
        }
    }

    private void decrWeek() {
        currentWeek--;
        if (currentWeek<1) {
            currentWeek= 4;
            decrMonth();
        }
    }

    private void incrMonth() {
        currentMonth++;
        if (currentMonth>12) {
            currentMonth= 1;
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        }
        calendar.set(Calendar.MONTH, currentMonth - 1);
    }

    private void decrMonth() {
        currentMonth--;
        if (currentMonth<1) {
            currentMonth= 12;
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        }
        calendar.set(Calendar.MONTH, currentMonth - 1);
    }

    private void updateCalendarView() {
        Tutor tutor= (Tutor) LoginManager.getCurrentAccount();
        if (tutor==null || tutor.getAvailabilities()==null) return;

        List<Availability> availabilities= tutor.getAvailabilities();

        // clears slots
        for (int hour= 0; hour<24; hour++) {
            for (int day= 0; day<7; day++) {
                int id= getResources().getIdentifier("slot_" + hour + "_" + day, "id", getPackageName());
                FrameLayout frame= findViewById(id);
                if (frame != null) frame.removeAllViews();
            }
        }

        // week range within month: week1–4
        LocalDate firstOfMonth= LocalDate.of(calendar.get(Calendar.YEAR), currentMonth, 1);

        LocalDate weekStart= firstOfMonth.plusDays((currentWeek - 1) * 7);

        LocalDate weekEnd= weekStart.plusDays(6);

        for (Availability slot : availabilities) {
            LocalDate date= slot.getDate();
            if (date==null) continue;

            if (date.isBefore(weekStart) || date.isAfter(weekEnd)) continue;

            int dayIndex= date.getDayOfWeek().getValue() - 1; // monday=0
            int hour= slot.getStart().getHour();

            int resId= getResources().getIdentifier("slot_" + hour + "_" + dayIndex, "id", getPackageName());
            FrameLayout frame = findViewById(resId);
            if (frame==null) continue;

            TextView tv= new TextView(this);
            tv.setText("●");
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundResource(R.drawable.grid_cell_border);

            frame.addView(tv);
        }
    }
}













