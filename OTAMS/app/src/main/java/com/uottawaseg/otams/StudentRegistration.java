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
            String username= binding.username.getText().toString().trim();
            String email= binding.email.getText().toString().trim();
            String password= binding.password.getText().toString().trim();
            String phone= binding.phoneNumber.getText().toString().trim();
            String firstName= binding.firstName.getText().toString().trim();
            String lastName= binding.lastName.getText().toString().trim();
            String program= "";// binding.program.getText().toString().trim();

            if (validateInput(firstName, lastName, username, email, password, phone, program)) {
                registerStudent(firstName, lastName, username, email, password, phone, program);
            }
        });
    }

    private boolean validateInput(String firstName, String lastName, String username, String email, String password, String phone, String program) {
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
            return false;
        }
        if (email.isEmpty()) {
            binding.email.setError("email is required");
            binding.email.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.setError("please enter a valid email");
            binding.email.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            binding.password.setError("password is required");
            binding.password.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            binding.password.setError("password must be at least 6 characters");
            binding.password.requestFocus();
            return false;
        }
        if (phone.isEmpty()) {
            binding.phoneNumber.setError("phone number is required");
            binding.phoneNumber.requestFocus();
            return false;
        }/*
        if (program.isEmpty()) {
            binding.program.setError("program is required");
            binding.program.requestFocus();
            return false;
        }*/

        return true;
    }

    private void registerStudent(String firstName, String lastName, String email, String username, String password, String phone, String studentID) {
        Student student = (Student) Database.Register(firstName, lastName, username, password, phone, email, studentID);
    }
}