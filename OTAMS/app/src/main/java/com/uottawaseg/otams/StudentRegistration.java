package com.uottawaseg.otams;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Accounts.Student;
import com.uottawaseg.otams.Database.Database;
import com.uottawaseg.otams.databinding.StudentRegistrationBinding;

public class StudentRegistration extends AppCompatActivity {

    // UI team, Im new with binding since its not core java feature so sry if something is incorrect.
    // If everything is done right it should allow us to skip manual UI initialization
    // overwise Ill return to my previous manual implementation findViewById(R.id.viewId)(please work)
    private StudentRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= StudentRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initListeners();
    }

    private void initListeners() {
        //Remove "//" once there is a class for main page
        //binding.btnHomepage.setOnClickListener(v -> {
        //Intent intent = new Intent(StudentRegistration.this, MainPage.class);
        //    startActivity(intent);
        //});
        binding.btnApply.setOnClickListener(v-> {
            String username= binding.etUsername.getText().toString().trim();
            String email= binding.etEmail.getText().toString().trim();
            String password= binding.etPassword.getText().toString().trim();
            String phone= binding.etPhone.getText().toString().trim();
            String firstName= binding.etFirstName.getText().toString().trim();
            String lastName= binding.etLastName.getText().toString().trim();
            String program= binding.etProgram.getText().toString().trim();

            if (validateInput(firstName, lastName, username, email, password, phone, program)) {
                registerStudent(firstName, lastName, username, email, password, phone, program);
            }
        });
    }

    private boolean validateInput(String firstName, String lastName, String username, String email, String password, String phone, String program) {
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
        if (username.isEmpty()) {
            binding.etUsername.setError("Username is required");
            binding.etUsername.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            binding.etEmail.setError("Email is required");
            binding.etEmail.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Please enter a valid email");
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
        if (program.isEmpty()) {
            binding.etProgram.setError("Program is required");
            binding.etProgram.requestFocus();
            return false;
        }

        return true;
    }

    private void registerStudent(String firstName, String lastName, String email, String username, String password, String phone, String studentID) {
        Student student = (Student) Database.Register(firstName, lastName, username, password, phone, email, studentID);
    }
}