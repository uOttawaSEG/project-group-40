package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.R;

public class registerAsTutor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Display tutor registration form
        setContentView(R.layout.tutor_registration);

        // Button "Apply" in tutor_registration
        Button tutor_apply = findViewById(R.id.btnApply);
        tutor_apply.setOnClickListener(view ->
                startActivity(new Intent(registerAsTutor.this, login.class)));

        // Button "Return to Homepage" in student_registration
        Button returnToHp = findViewById(R.id.btnHomepage);
        returnToHp.setOnClickListener(view ->
                startActivity(new Intent(registerAsTutor.this, MainActivity.class)));

    }
}
