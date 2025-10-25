// Imports
package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uottawaseg.otams.Database.Database;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.R;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.main_activity);
        Database.Database.StartDB();
        LoginManager.LogOut();
        // LoginManager.RegisterAdmin();
        // Main page "Login" button

        Button main_page_login_button = findViewById(R.id.main_login);
        main_page_login_button.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, Login.class)));

        // Main page "Sign-up" button
        Button main_page_register_button = findViewById(R.id.main_signup);
        main_page_register_button.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, Register.class)));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}