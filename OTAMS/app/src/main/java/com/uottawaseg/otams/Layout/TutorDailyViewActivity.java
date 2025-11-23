package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.R;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Locale;

public class TutorDailyViewActivity extends AppCompatActivity {

    //UI elements
    private Button btnAddAvailability, btnViewUpcoming;
    private Button btnPrevMonth, btnNextMonth;
    private Button btnPrevDay, btnNextDay;
    private Button btnWeeklyView, btnViewAvailability, btnHomepage;
    private TextView monthText, dayText;

    //Calendar state
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_daily_view);

        //Initialize calendar
        calendar= Calendar.getInstance();

        //Initialize UI elements
        btnAddAvailability= findViewById(R.id.btnAddAvailability);
        btnViewUpcoming= findViewById(R.id.btnViewUpcoming);
        btnPrevMonth= findViewById(R.id.btnPrevMonth);
        btnNextMonth= findViewById(R.id.btnNextMonth);
        monthText= findViewById(R.id.monthText);
        btnPrevDay= findViewById(R.id.btnPrevDay);
        btnNextDay= findViewById(R.id.btnNextDay);
        dayText= findViewById(R.id.dayText);
        btnWeeklyView= findViewById(R.id.btnWeeklyView);
        btnViewAvailability= findViewById(R.id.btnViewAvailability);
        btnHomepage= findViewById(R.id.btnHomepage);

        //Update header(month/year + day with suffix)
        updateHeader();

        //Button listeners
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

        //Update events for the current day
        updateEvents();
    }

    //Updates header(month/year + day with suffix)
    private void updateHeader() {
        SimpleDateFormat monthFormat= new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthText.setText(monthFormat.format(calendar.getTime()));

        int day= calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat weekdayFormat= new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayName = weekdayFormat.format(calendar.getTime());

        dayText.setText(dayName +", "+ getDayWithSuffix(day));
    }

    //Changes month with last-day(31st->30th etc) adjustment
    private void changeMonth(int offset) {
        calendar.add(Calendar.MONTH, offset);
        int maxDay= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (calendar.get(Calendar.DAY_OF_MONTH) > maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, maxDay);
        }
        updateHeader();
        updateEvents();  //Updates the events after month change
    }

    //Changes day with automatic month rollover
    private void changeDay(int offset) {
        calendar.add(Calendar.DAY_OF_MONTH, offset);
        updateHeader();
        updateEvents();  //Updates the events after day change
    }

    //day suffix
    private String getDayWithSuffix(int day) {
        if (day >= 11 && day <= 13) return day + "th";
        switch (day % 10) {
            case 1: return day + "st";
            case 2: return day + "nd";
            case 3: return day + "rd";
            default: return day + "th";
        }
    }

    //Updates events for the current day
    private void updateEvents() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDayKey = dateFormat.format(calendar.getTime());

        //Gets the instance of CalendarEventManager
        CalendarEventManager eventManager = CalendarEventManager.getInstance();
        GenerateEvents();
        //Loops through 24 hours to check if there are any events for each hour
        for (int hour = 0; hour < 24; hour++) {
            //Build key for the event at this hour
            String key = "event_" + hour + "_daily";
            CalendarEventManager.Event event = eventManager.getEvent(key);

            //Gets the corresponding FrameLayout(slot) for this hour
            int slotResId= getResources().getIdentifier("slot_" + hour + "_daily", "id", getPackageName());
            FrameLayout slotView = findViewById(slotResId);

            if (slotView != null) {
                //Clears any previous content inside the slot
                slotView.removeAllViews();

                //If there's an event displays the event's note/message in the slot
                if (event != null) {
                    TextView eventNoteTextView = getTextView(event);
                    //Adds the note to the slot view
                    slotView.addView(eventNoteTextView);
                }
            }
        }
    }

    @NonNull
    private TextView getTextView(CalendarEventManager.Event event) {
        TextView eventNoteTextView= new TextView(this); //TextView to display the event note
        eventNoteTextView.setText(event.symbol); // Displays the event's actual note
        eventNoteTextView.setTextSize(14); //Adjusts the text size to fit within the slot
        eventNoteTextView.setGravity(Gravity.CENTER);
        eventNoteTextView.setPadding(8, 8, 8, 8);//Padding for better spacing

        //Background/border for the event note
        eventNoteTextView.setBackgroundResource(R.drawable.grid_cell_border);
        return eventNoteTextView;
    }

    //this is for time format
    private String getTimeForHour(int hour) {
        return String.format(Locale.getDefault(), "%02d:00", hour);
    }

    private void GenerateEvents() {
        var tut = (Tutor) LoginManager.getCurrentAccount();
        var accepted = tut.getSessions();
        var currentDay = calendar.get(Calendar.DAY_OF_YEAR);
        var offsetDate = OffsetDateTime.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0, 0,
                OffsetDateTime.now().getOffset()
        );
        var eventManager = CalendarEventManager.getInstance();
        for(var s : accepted) {
            if(s.getDate().getDayOfYear() != currentDay || s.getDate().compareTo(offsetDate) < 0) continue;
            System.out.println(s);
            var endH = s.getEndTime().getHour();
            var startH = s.getStartTime().getHour();

            for(int i = startH; i <= endH; i++) {
                var eventID = "event_" + i + "_daily";
                eventManager.addEvent(eventID, new CalendarEventManager.Event("Tutoring " + s.getStudent(), ""));
            }
        }
    }
}






