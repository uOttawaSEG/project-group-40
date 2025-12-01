package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.uottawaseg.otams.R;


public class StudentProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //shows the student profile page
        setContentView(R.layout.student_profile);

        //all the buttons from the layout
        Button cardBooked = findViewById(R.id.btnBooked);
        Button cardRequested = findViewById(R.id.btnRequested);
        Button cardSearch = findViewById(R.id.btnSearch);
        Button btnBack = findViewById(R.id.btn_back1);

        //the button to view booked sessions
        cardBooked.setOnClickListener(view -> {
            startActivity(new Intent(StudentProfile.this, StudentBookedActivity.class));
        });

        //From Daniil: disabled unless we will implement approval(in that case also remove comments from findViewById and from button itself in xml layout).
        //the button to view requested sessions
        cardRequested.setOnClickListener(view -> {
            startActivity(new Intent(StudentProfile.this, StudentPendingActivity.class));
        });

        //the buttoon to search for courses
        cardSearch.setOnClickListener(view -> {
            startActivity(new Intent(StudentProfile.this, StudentSearchCoursesActivity.class));
        });

        //the home button to go back to main page
        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(StudentProfile.this, MainActivity.class));
            finish();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the page when coming back from other activities
    }
}
