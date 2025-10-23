package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Database.Database;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.databinding.StudentRegistrationBinding;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.login);


        Button login_login_button = findViewById(R.id.login_button);
        login_login_button.setOnClickListener(view ->
                {
                    var usrView = (TextView) findViewById(R.id.email_input);
                    var passView = (TextView) findViewById(R.id.password_input);
                    var username = ((TextView) findViewById(R.id.email_input)).getText().toString();
                    var password = ((TextView) findViewById(R.id.password_input)).getText().toString();
                    var acc = LoginManager.Login(username, password);
                    System.out.println(acc);
                    if (acc != null) {
                        System.out.println("Login successful");
                        startActivity(new Intent(login.this, welcome.class));
                    } else {
                        System.out.println("Unable to login");
                        Toast.makeText(this.getApplicationContext(), "Unable to login", Toast.LENGTH_LONG);
                    }
                });


        Button login_returnToHP_button = findViewById(R.id.returnToHomepage_button);
        login_returnToHP_button.setOnClickListener(view ->
                startActivity(new Intent(login.this, MainActivity.class)));



    }
}
