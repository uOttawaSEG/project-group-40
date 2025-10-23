package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.R;

public class Status extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.status);
        var text = (TextView) findViewById(R.id.status_text);
        System.out.println(text);
        String toDisplay = LoginManager.WasPending() ?

                ("You are logged in as " + LoginManager.getCurrentAccount().getName()
                        + ". \nYour application for " + LoginManager.getCurrentAccount().getRole()
                        + "is still under review, we'll notify you via email when we make our decision") :
                ("You are logged in as " + LoginManager.getCurrentAccount().getName()
                        + ". \n Your role is: " + LoginManager.getCurrentAccount().getRole());

        text.setText(toDisplay);

        // Return to home page from status page
        Button welcome_returnToHP_button = findViewById(R.id.btn_return);
        welcome_returnToHP_button.setOnClickListener(view ->
                startActivity(new Intent(Status.this, MainActivity.class)));
    }
}
