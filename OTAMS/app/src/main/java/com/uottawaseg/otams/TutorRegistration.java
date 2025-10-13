package com.uottawaseg.otams;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Courses.*;
import com.uottawaseg.otams.Database.Database;
import com.uottawaseg.otams.databinding.TutorRegistrationBinding;

public class TutorRegistration extends AppCompatActivity {
    private TutorRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TutorRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initListeners();
    }

    private void initListeners() {
        binding.btnApply.setOnClickListener(v -> {
            // TODO: add username
            String username = binding.username.getText().toString().trim();
            String email = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            String phone = binding.phoneNumber.getText().toString().trim();
            String firstName = binding.firstName.getText().toString().trim();
            String lastName = binding.lastName.getText().toString().trim();
            String degree = ((TextView) binding.degree.getSelectedView()).getText().toString();
            // TODO: Change this to field of study
            // Ideally we have a dropdown menu
            String field = "MATHEMATICS";//((TextView) binding.fieldOfStudy.getSelectedView()).getText().toString();
            var fieldOfStudy = Field.ENGINEERING.toString();
            if (validateInput(firstName, lastName, email, username, password, phone)) {
                registerTutor(firstName, lastName, email, username, password, phone, Degree.fromString(degree), Field.fromString(fieldOfStudy));
            }
        });
    }

    // DB has a function to validate usernames
    private boolean validateInput(String firstName, String lastName, String email, String username, String password, String phone) {
        if (firstName.isEmpty()) {
            binding.firstName.setError("First name is required");
            binding.firstName.requestFocus();
            return false;
        }
        if (lastName.isEmpty()) {
            binding.lastName.setError("Last name is required");
            binding.lastName.requestFocus();
            return false;
        }
        if (username.isEmpty()) {
            binding.username.setError("Username is required");
            binding.username.requestFocus();
            return Database.CheckUsername(username);
        }
        if (email.isEmpty()) {
            binding.email.setError("Email is required");
            binding.email.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.setError("Enter a valid email");
            binding.email.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            binding.password.setError("Password is required");
            binding.password.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            binding.password.setError("Password must be at least 6 characters");
            binding.password.requestFocus();
            return false;
        }
        if (phone.isEmpty()) {
            binding.phoneNumber.setError("Phone number is required");
            binding.phoneNumber.requestFocus();
            return false;
        }
        return true;
    }

    private void registerTutor(String firstName, String lastName, String email, String username, String password, String phone, Degree degree, Field fieldOfStudy) {
        Tutor tutor = (Tutor) Database.Register(firstName, lastName, username, password, email, phone, degree, fieldOfStudy);
    }
}