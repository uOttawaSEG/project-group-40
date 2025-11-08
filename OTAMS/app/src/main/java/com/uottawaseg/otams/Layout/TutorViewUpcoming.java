package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.R;

public class TutorViewUpcoming extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tutor_view_upcoming);


        Button btn_back = findViewById(R.id.btn_back);
        // Modify this as needed
        btn_back.setOnClickListener(view ->
                startActivity(new Intent(TutorViewUpcoming.this, TutorViewUpcoming.class)));


        Button home = findViewById(R.id.home2);
        // Modify this as needed
        home.setOnClickListener(view ->
                startActivity(new Intent(TutorViewUpcoming.this, TutorViewUpcoming.class)));
    }
}
