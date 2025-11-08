package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.R;

public class TutorViewPastSessions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tutor_view_past_sessions);


        Button btn_back = findViewById(R.id.btn_back1);
        // Modify this as needed
        btn_back.setOnClickListener(view ->
                startActivity(new Intent(TutorViewPastSessions.this, TutorViewUpcoming.class)));

        Button home = findViewById(R.id.home2);
        // Modify this as needed
        home.setOnClickListener(view ->
                startActivity(new Intent(TutorViewPastSessions.this, TutorViewUpcoming.class)));
    }
}
