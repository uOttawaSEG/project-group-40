package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.R;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.welcome);


        Button welcome_returnToHP_button = findViewById(R.id.login_button);
        welcome_returnToHP_button.setOnClickListener(view ->
                startActivity(new Intent(welcome.this, MainActivity.class)));



    }
}
