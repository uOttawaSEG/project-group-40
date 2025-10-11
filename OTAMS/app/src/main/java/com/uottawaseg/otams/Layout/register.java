package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uottawaseg.otams.R;

public class register extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Display registration_options page/activity
        setContentView(R.layout.registration_options);

        // Button on registration_options activity "Tutor"
        Button register_tutor_btn = findViewById(R.id.register_tutor_button);
        register_tutor_btn.setOnClickListener(view ->
                startActivity(new Intent(register.this, registerAsTutor.class)));

        // Button on registration_options activity "Student"
        Button register_student_btn = findViewById(R.id.register_student_button);
        register_student_btn.setOnClickListener(view ->
                startActivity(new Intent(register.this, registerAsStudent.class)));

        // Button on registration_options activity "Return to Homepage"
        Button return_to_HP = findViewById(R.id.returnToHP_button);
        return_to_HP.setOnClickListener(view ->
                startActivity(new Intent(register.this, MainActivity.class)));
    }

}
