// Imports
package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Courses.Degree;
import com.uottawaseg.otams.Courses.Field;
import com.uottawaseg.otams.Database.AvailabilityReader;
import com.uottawaseg.otams.Database.Database;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.Availability;
import com.uottawaseg.otams.Requests.RequestStatus;
import com.uottawaseg.otams.Requests.SessionRequest;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.other_main_page);
        Database.Database.StartDB();
        LoginManager.LogOut();
        // LoginManager.RegisterAdmin();
        // Main page "Login" button

        Button main_page_login_button = findViewById(R.id.main_login);
        main_page_login_button.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Login.class));

            this.finish();
        });

        // Main page "Sign-up" button
        Button main_page_register_button = findViewById(R.id.main_signup);
        main_page_register_button.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Register.class));
            this.finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        var avails = new ArrayList<Availability>();
        avails.add(new Availability(false, OffsetTime.of(1, 30, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(2, 30, 0, 0, ZoneOffset.UTC), DayOfWeek.MONDAY));
        var tutor = new Tutor(
                "first", "last", "user", "pass", "12345", "no@no.com", Degree.BACHELORS, Field.ENGINEERING,
                avails, null);
        var sessionRequest = new SessionRequest("bananas", "user", OffsetTime.of(1, 30, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(2, 30, 0, 0, ZoneOffset.UTC), 10, 11, 2005);
        sessionRequest.setStatus(RequestStatus.ACCEPTED);
        tutor.AddSession(sessionRequest);

        var secondRequest = new SessionRequest("bananas", "user", OffsetTime.of(1, 30, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(2, 30, 0, 0, ZoneOffset.UTC), 2, 1, 2026);

        tutor.AddSession(secondRequest);
        var third = new SessionRequest("bananas", "user", OffsetTime.of(1, 30, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(2, 30, 0, 0, ZoneOffset.UTC), 3, 1, 2026);
        tutor.AddSession(third);
        Database.Database.WriteAccount(LoginManager.ACCOUNTS + "/user", tutor);

    }
}