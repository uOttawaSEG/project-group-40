package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.R;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.login);


        Button login_login_button = findViewById(R.id.login_button);
        login_login_button.setOnClickListener(view ->
                startActivity(new Intent(login.this, welcome.class)));

        Button login_returnToHP_button = findViewById(R.id.returnToHomepage_button);
        login_returnToHP_button.setOnClickListener(view ->
                startActivity(new Intent(login.this, MainActivity.class)));



    }
}
