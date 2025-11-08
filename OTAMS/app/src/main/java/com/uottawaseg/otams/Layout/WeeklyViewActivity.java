package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.uottawaseg.otams.R;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeeklyViewActivity extends AppCompatActivity {

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
                startActivity(new Intent(this, DailyViewActivity.class)));

        btnViewAvailability.setOnClickListener(v ->
                startActivity(new Intent(this, ViewAvailability.class)));

        btnHomepage.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        //Month btn
        btnNextMonth.setOnClickListener(v -> {
            currentMonth++;
            if (currentMonth > 12) currentMonth= 1;
            updateHeaderLabels();
            updateCalendarView();
        });

        btnPrevMonth.setOnClickListener(v -> {
            currentMonth--;
            if (currentMonth < 1) currentMonth= 12;
            updateHeaderLabels();
            updateCalendarView();
        });

        //Week btns
        btnNextWeek.setOnClickListener(v -> {
            currentWeek++;
            if (currentWeek > 4) {
                currentWeek= 1;
                currentMonth++;
                if (currentMonth > 12) currentMonth= 1;
            }
            updateHeaderLabels();
            updateCalendarView();
        });

        btnPrevWeek.setOnClickListener(v -> {
            currentWeek--;
            if (currentWeek < 1) {
                currentWeek= 4;
                currentMonth--;
                if (currentMonth < 1) currentMonth = 12;
            }
            updateHeaderLabels();
            updateCalendarView();
        });
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

        for (int hour = 0; hour < 24; hour++) {
            for (int day = 0; day < 7; day++) {
                String viewId= "event_" + hour + "_" + day;
                int resId= getResources().getIdentifier(viewId, "id", getPackageName());
                TextView slotView= findViewById(resId);

                if (slotView != null) {
                    Calendar slotTime= (Calendar) startOfWeek.clone();
                    slotTime.add(Calendar.DAY_OF_MONTH, day);
                    slotTime.set(Calendar.HOUR_OF_DAY, hour);

                    String key= new SimpleDateFormat("yyyy-MM-dd-HH", Locale.getDefault())
                            .format(slotTime.getTime());
                    CalendarEventManager.Event event= eventManager.getEvent(key);

                    if (event != null) {
                        slotView.setText(event.symbol); //Only displays a symbol in Weekly view
                        slotView.setTextColor(Color.RED);
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
}
