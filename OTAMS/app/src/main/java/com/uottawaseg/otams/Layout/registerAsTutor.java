package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Courses.Degree;
import com.uottawaseg.otams.Courses.Field;
import com.uottawaseg.otams.Database.Database;
import com.uottawaseg.otams.R;

public class registerAsTutor extends AppCompatActivity {

    protected Degree degreeSelected = Degree.UNKNOWN;
    protected Field fieldOfStudy = Field.UNKNOWN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Display tutor registration form
        setContentView(R.layout.tutor_registration);
//
// Button "Apply" in tutor_registration

        // Filling in spinner values
        var degrees = (Spinner) findViewById(R.id.degree);
        var fields = (Spinner) findViewById(R.id.fields);

        // This setups up the two spinners
        // This took way too long to get working I'm not even going to lie.
        var degreeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Degree.getValues());
        degreeAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        degrees.setAdapter(degreeAdapter);

        var fieldAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Field.getValues());
        fieldAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        fields.setAdapter(fieldAdapter);

        Button tutor_apply = findViewById(R.id.btnApply);
        initListeners(tutor_apply);
//        tutor_apply.setOnClickListener(view ->
//                startActivity(new Intent(registerAsTutor.this, login.class)));

        // Button "Return to Homepage" in student_registration
        Button returnToHp = findViewById(R.id.btnHomepage);
        returnToHp.setOnClickListener(view ->
                startActivity(new Intent(registerAsTutor.this, MainActivity.class)));

    }


    private void initListeners(Button toApply) {

        // Should probably have cached these earlier but w/e
        var degreeSpinner = (Spinner) findViewById(R.id.degree);
        var fieldSpinner = (Spinner) findViewById(R.id.fields);

        degreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

            @Override
            public void onItemSelected(AdapterView parent, View v, int position, long id) {
                degreeSelected = Degree.fromString(parent.getItemAtPosition(position).toString());
            }
        });

        fieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

            @Override
            public void onItemSelected(AdapterView parent, View v, int position, long id) {
                fieldOfStudy = Field.fromString(parent.getItemAtPosition(position).toString());
            }
        });

        toApply.setOnClickListener(v -> {
            var fName = ((TextView) findViewById(R.id.firstName)).getText().toString().trim();
            var lName = ((TextView) findViewById(R.id.lastName)).getText().toString().trim();
            var email = ((TextView) findViewById(R.id.email)).getText().toString().trim();
            var username = ((TextView) findViewById(R.id.username)).getText().toString().trim();
            var password = ((TextView) findViewById(R.id.password)).getText().toString().trim();
            var phone = ((TextView) findViewById(R.id.phoneNumber)).getText().toString().trim();
            if(validateInput(fName, lName, email, username, password, phone, degreeSelected, fieldOfStudy)) {
                registerTutor(fName, lName, username, password, phone, email, degreeSelected, fieldOfStudy);
                startActivity(new Intent(registerAsTutor.this, welcome.class));
            }
        });
    }

    // DB has a function to validate usernames
    private boolean validateInput(String firstName, String lastName, String email,
                                  String username, String password, String phone,
                                  Degree deg, Field field) {
        if (firstName.isEmpty()) {
            Toast.makeText(this, "First name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (lastName.isEmpty()) {
            Toast.makeText(this, "Last name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username.isEmpty()) {
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!Database.CheckUsername(username)) {
            Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "Phone number required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(deg == Degree.UNKNOWN) {
            Toast.makeText(this, "Degree is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(field == Field.UNKNOWN) {
            Toast.makeText(this, "Field of study is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerTutor(String firstName, String lastName, String username, String password,
                               String phone, String email, Degree degree, Field fieldOfStudy) {
        Tutor tutor = (Tutor) Database.Register(firstName, lastName, username, password, phone, email, degree, fieldOfStudy);
    }
}
