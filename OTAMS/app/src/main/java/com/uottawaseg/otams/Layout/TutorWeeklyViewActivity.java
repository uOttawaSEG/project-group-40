package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.RequestStatus;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Locale;

public class TutorWeeklyViewActivity extends AppCompatActivity {

    //UI Elements
    private Button btnAddAvailability, btnViewUpcoming;
    private Button btnPrevMonth, btnNextMonth;
    private Button btnPrevWeek, btnNextWeek;
    private Button btnDailyView, btnViewAvailability, btnHomepage;
    private TextView monthLabel, weekLabel;

    //Calendar State
    private Calendar calendar;
    private int currentMonth;
    private int currentWeek;

    //Event manager to get events
    private CalendarEventManager eventManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_weekly_view);

        //Initialization
        calendar= Calendar.getInstance();
        currentMonth= calendar.get(Calendar.MONTH) + 1;
        currentWeek= 1;

        //Event manager
        eventManager= CalendarEventManager.getInstance();

        //Top buttons
        btnAddAvailability= findViewById(R.id.btnAddAvailability);
        btnViewUpcoming= findViewById(R.id.btnViewUpcoming);

        //Month controls
        btnPrevMonth= findViewById(R.id.btnPrevMonth);
        btnNextMonth= findViewById(R.id.btnNextMonth);
        monthLabel= findViewById(R.id.monthLabel);

        //Week controls
        btnPrevWeek= findViewById(R.id.btnPrevWeek);
        btnNextWeek= findViewById(R.id.btnNextWeek);
        weekLabel= findViewById(R.id.weekLabel);

        //Bottom btns
        btnDailyView= findViewById(R.id.btnDailyView);
        btnViewAvailability= findViewById(R.id.btnViewAvailability);
        btnHomepage= findViewById(R.id.btnHomepage);

        //Set labels
        updateHeaderLabels();
        updateCalendarView();

        //Button listeners

        //Navigation between activities
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

        //Month btn
        btnNextMonth.setOnClickListener(v -> {
            incrMonth();
            updateHeaderLabels();
            updateCalendarView();
        });

        btnPrevMonth.setOnClickListener(v -> {
            decrMonth();
            updateHeaderLabels();
            updateCalendarView();
        });

        //Week btns
        btnNextWeek.setOnClickListener(v -> {
            currentWeek++;
            if (currentWeek > 4) {
                currentWeek = 1;
                incrMonth();
            }
            updateHeaderLabels();
            updateCalendarView();
        });

        btnPrevWeek.setOnClickListener(v -> {
            currentWeek--;
            if (currentWeek < 1) {
                currentWeek= 4;
                decrMonth();
            }
            updateHeaderLabels();
            updateCalendarView();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCalendarView();
        updateHeaderLabels();
    }

    private void incrMonth() {
        currentMonth++;
        if (currentMonth > 12) {
            currentMonth= 1;
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        }
    }

    private void decrMonth() {
        currentMonth--;
        if (currentMonth < 1) {
            currentMonth= 12;
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        }
    }
    //Updates header labels
    private void updateHeaderLabels() {
        String monthName= new SimpleDateFormat("MMMM", Locale.getDefault())
                .format(getMonthCalendar().getTime());
        monthLabel.setText(monthName + " " + calendar.get(Calendar.YEAR));
        weekLabel.setText("Week " + currentWeek);
    }

    //Refreshes grid with correct events
    private void updateCalendarView() {
        Calendar startOfWeek= getStartOfCurrentWeek();
        GenerateEvents();

        for (int hour = 0; hour < 24; hour++) {
            for (int day = 0; day < 7; day++) {
                String viewId= "event_" + hour + "_" + day;
                int resId= getResources().getIdentifier(viewId, "id", getPackageName());
                TextView slotView= findViewById(resId);

                if (slotView != null) {
                    Calendar slotTime= (Calendar) startOfWeek.clone();
                    slotTime.add(Calendar.DAY_OF_MONTH, day);
                    slotTime.set(Calendar.HOUR_OF_DAY, hour);

                    CalendarEventManager.Event event= eventManager.getEvent(viewId);

                    if (event != null) {
                        slotView.setText(event.symbol); //Only displays a symbol in Weekly view
                        slotView.setTextColor(Color.BLACK);
                    } else {
                        slotView.setText("");
                    }
                }
            }
        }
    }

    //Gets calendar for start of month
    private Calendar getMonthCalendar() {
        Calendar c= (Calendar) calendar.clone();
        c.set(Calendar.MONTH, currentMonth - 1);
        return c;
    }

    //Calculates Monday of current week within current month
    private Calendar getStartOfCurrentWeek() {
        Calendar c= getMonthCalendar();
        c.set(Calendar.DAY_OF_MONTH, 1); //Starts at first day of month

        //Finds first Monday
        int dayOfWeek= c.get(Calendar.DAY_OF_WEEK);
        int delta= (Calendar.MONDAY - dayOfWeek + 7) % 7;
        c.add(Calendar.DAY_OF_MONTH, delta);

        //Adds offset for currentWeek
        c.add(Calendar.DAY_OF_MONTH, (currentWeek - 1) * 7);

        return c;
    }

    private void GenerateEvents() {
        var tut = (Tutor) LoginManager.getCurrentAccount();
        var sessions = tut.getSessions();
        var currentDay = calendar.get(Calendar.DAY_OF_YEAR);
        var firstDayOfWeek = getStartOfWeek(calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.DAY_OF_MONTH));
        var offsetDate = OffsetDateTime.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                firstDayOfWeek, 0, 0, 0, 0,
                OffsetDateTime.now().getOffset()
        );
        // Excluding the current day, its 6. Not 7.
        var finalDayOfWeek = offsetDate.plusDays(6);
        for(var s : sessions) {
            if(!s.getStatus().equals(RequestStatus.ACCEPTED)) continue;

            if(s.getDate().compareTo(offsetDate) < 0 || s.getDate().compareTo(finalDayOfWeek) > 0) continue;
            var endH = s.getEndTime().getHour();
            var startH = s.getStartTime().getHour();

            // It starts at 1, we start at 0
            var date = s.getDate().getDayOfWeek().getValue() - 1;

            for(int i = startH; i <= endH; i++) {
                var eventID = "event_" + i + "_" + date;
                eventManager.addEvent(eventID, new CalendarEventManager.Event("Tutoring " + s.getStudent(), ""));
            }
        }
    }

    private int getStartOfWeek(int dayOfWeek, int dayOfYear) {
        // Monday == 1, we want monday to equal 0.
        return dayOfYear - dayOfWeek + 1;
    }
}
