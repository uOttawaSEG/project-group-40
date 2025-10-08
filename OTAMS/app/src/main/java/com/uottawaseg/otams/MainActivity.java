package com.uottawaseg.otams;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uottawaseg.otams.Courses.Degree;
import com.uottawaseg.otams.Courses.Field;
import com.uottawaseg.otams.Database.Database;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Database.StartDB();

        // Testing register
        Database.Register("Sebastien", "Gosselin", "sgoss040@uottawaca",
                "Pass1", "email", "phone num", "101");
        Database.Register("Sebastien", "Gosselin", "bananas", "pass2",
                "10490214", "emailtxt", Degree.HIGHSCHOOL, Field.ENGINEERING);
        // Testing login
        var acc = Database.Login("bananas", "pass2");
        System.out.println(acc);
    }
}