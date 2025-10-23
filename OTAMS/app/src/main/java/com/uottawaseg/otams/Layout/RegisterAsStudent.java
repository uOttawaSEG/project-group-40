package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Accounts.Student;
import com.uottawaseg.otams.Database.Database;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.R;

public class registerAsStudent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Display student registration form
        setContentView(R.layout.student_registration);

        // Button "Apply" in tutor_registration
        Button applyButton = findViewById(R.id.btnApply);
        applyButton.setOnClickListener(view -> {
            initListeners(applyButton);
        });

        // Button "Return to Homepage" in student_registration
        Button returnToHp = findViewById(R.id.btnHomepage);
        returnToHp.setOnClickListener(view ->
                startActivity(new Intent(registerAsStudent.this, MainActivity.class)));
    }


    private void initListeners(Button toApply) {
        toApply.setOnClickListener(v -> {
            var fName = ((TextView) findViewById(R.id.firstName)).getText().toString().trim();
            var lName = ((TextView) findViewById(R.id.lastName)).getText().toString().trim();
            var email = ((TextView) findViewById(R.id.email)).getText().toString().trim();
            var username = ((TextView) findViewById(R.id.username)).getText().toString().trim();
            var password = ((TextView) findViewById(R.id.password)).getText().toString().trim();
            var phone = ((TextView) findViewById(R.id.phoneNumber)).getText().toString().trim();
            var id = "101";
            if(validateInput(fName, lName, email, username, password, phone, id)) {
                registerStudent(fName, lName, email, username, password, phone, id);
                if(LoginManager.getCurrentAccount() == null)
                    Toast.makeText(this, "Cannot create account, please try again later", Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(registerAsStudent.this, welcome.class));
            }
        });
    }

    // DB has a function to validate usernames
    private boolean validateInput(String firstName, String lastName, String email,
                                  String username, String password, String phone,
                                  String ID) {
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
        if(!LoginManager.CheckUsername(username)) {
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
        if(ID.isEmpty()) {
            Toast.makeText(this, "Student number is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void registerStudent(String firstName, String lastName, String email, String username,
                                 String password, String phone, String ID) {
        LoginManager.Register(firstName, lastName, username, password, phone, email, ID);
    }
}
