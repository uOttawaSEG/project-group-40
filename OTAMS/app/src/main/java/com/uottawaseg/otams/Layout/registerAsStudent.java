package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.R;

public class registerAsStudent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Display student registration form
        setContentView(R.layout.student_registration);

        // Button "Apply" in tutor_registration
        Button welcome_returnToHP_button = findViewById(R.id.btnApply);
        welcome_returnToHP_button.setOnClickListener(view ->
                startActivity(new Intent(registerAsStudent.this, login.class)));

        // Button "Return to Homepage" in student_registration
        Button returnToHp = findViewById(R.id.btnHomepage);
        returnToHp.setOnClickListener(view ->
                startActivity(new Intent(registerAsStudent.this, MainActivity.class)));
    }
}
