package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.R;

public class Register extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Display registration_options page/activity
        setContentView(R.layout.registration_options);

        // Button on registration_options activity "Tutor"
        Button register_tutor_btn = findViewById(R.id.tutor_btn);
        register_tutor_btn.setOnClickListener(view ->
                startActivity(new Intent(Register.this, RegisterAsTutor.class)));

        // Button on registration_options activity "Student"
        Button register_student_btn = findViewById(R.id.student_btn);
        register_student_btn.setOnClickListener(view ->
                startActivity(new Intent(Register.this, RegisterAsStudent.class)));

        // Button on registration_options activity "Return to Homepage"
        Button return_to_HP = findViewById(R.id.hp_btn);
        return_to_HP.setOnClickListener(view ->
                startActivity(new Intent(Register.this, MainActivity.class)));
    }

}
