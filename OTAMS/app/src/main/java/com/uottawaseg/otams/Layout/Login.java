package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Accounts.Account;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.other_login);


        Button login_login_button = findViewById(R.id.login_button);
        login_login_button.setOnClickListener(view ->
                {
                    var username = ((TextView) findViewById(R.id.email_input)).getText().toString();
                    var password = ((TextView) findViewById(R.id.password_input)).getText().toString();
                    var acc = LoginManager.Login(username, password);
                    if (acc != null) {
                        System.out.println("Login successful");

                        if (acc.getRole() == Account.Role.ADMIN) {
                            startActivity(new Intent(Login.this, AdminPendingRequests.class));
                            this.finish();
                        } else {
                            startActivity(new Intent(Login.this, Status.class));
                            this.finish();
                        }

                    } else {
                        System.out.println("Unable to login");
                        Toast.makeText(this.getApplicationContext(), "Unable to login", Toast.LENGTH_LONG).show();
                    }
                });


        Button login_returnToHP_button = findViewById(R.id.returnToHomepage_button);
        login_returnToHP_button.setOnClickListener(view -> {
            startActivity(new Intent(Login.this, MainActivity.class));
            this.finish();
        });
    }
}
