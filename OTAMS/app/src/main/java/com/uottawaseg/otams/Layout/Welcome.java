package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.R;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.welcome);
        var text = (TextView) findViewById(R.id.loginConfirmation);
        String toDisplay = LoginManager.WasPending() ?

                ("You are logged in as " + LoginManager.getCurrentAccount().getName()
                        + ". \nYour application for " + LoginManager.getCurrentAccount().getRole()
                        + "is still under review, we'll notify you via email when we make our decision") :
                ("You are logged in as " + LoginManager.getCurrentAccount().getName()
                        + ". \n Your role is: " + LoginManager.getCurrentAccount().getRole());

        text.setText(toDisplay);

        Button welcome_returnToHP_button = findViewById(R.id.login_button);
        welcome_returnToHP_button.setOnClickListener(view ->
                startActivity(new Intent(Welcome.this, MainActivity.class)));
    }
}
