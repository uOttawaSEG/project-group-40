package com.uottawaseg.otams;

import android.os.Bundle;

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
        binding.button.setOnClickListener(v -> {
            // TODO: add username
            String username = "";
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();
            String firstName = binding.etFirstName.getText().toString().trim();
            String lastName = binding.etLastName.getText().toString().trim();
            String degree = binding.etDegree.getText().toString().trim();
            // TODO: Change this to field of study
            // Ideally we have a dropdown menu
            String courses = binding.etCourses.getText().toString().trim();
            var fieldOfStudy = Field.ENGINEERING.toString();
            if (validateInput(firstName, lastName, email, username, password, phone, degree, courses)) {
                registerTutor(firstName, lastName, email, username, password, phone, Degree.fromString(degree), Field.fromString(fieldOfStudy));
            }
        });
    }

    // DB has a function to validate usernames
    private boolean validateInput(String firstName, String lastName, String email, String username, String password, String phone, String degree, String courses) {
        if (firstName.isEmpty()) {
            binding.etFirstName.setError("First name is required");
            binding.etFirstName.requestFocus();
            return false;
        }
        if (lastName.isEmpty()) {
            binding.etLastName.setError("Last name is required");
            binding.etLastName.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            binding.etEmail.setError("Email is required");
            binding.etEmail.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Enter a valid email");
            binding.etEmail.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            binding.etPassword.setError("Password is required");
            binding.etPassword.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            binding.etPassword.setError("Password must be at least 6 characters");
            binding.etPassword.requestFocus();
            return false;
        }
        if (phone.isEmpty()) {
            binding.etPhone.setError("Phone number is required");
            binding.etPhone.requestFocus();
            return false;
        }
        if (degree.isEmpty()) {
            binding.etDegree.setError("Degree is required");
            binding.etDegree.requestFocus();
            return false;
        }
        if (courses.isEmpty()) {
            binding.etCourses.setError("Courses offered are required");
            binding.etCourses.requestFocus();
            return false;
        }
        //TODO: Check username
        return true;
    }

    private void registerTutor(String firstName, String lastName, String email, String username, String password, String phone, Degree degree, Field fieldOfStudy) {
        Tutor tutor = (Tutor) Database.Register(firstName, lastName, username, password, email, phone, degree, fieldOfStudy);
    }
}