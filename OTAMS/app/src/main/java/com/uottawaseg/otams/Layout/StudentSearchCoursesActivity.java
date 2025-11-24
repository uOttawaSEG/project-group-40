package com.uottawaseg.otams.Layout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Accounts.Account;
import com.uottawaseg.otams.Database.AvailabilityReader;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.Layout.support.StudentSearchCoursesAdapter;
import com.uottawaseg.otams.Requests.Availability;
import com.uottawaseg.otams.R;

import java.util.ArrayList;
import java.util.List;

public class StudentSearchCoursesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentSearchCoursesAdapter adapter;
    private List<Availability> availabilityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_search_courses);
        recyclerView= findViewById(R.id.availability_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button btnHome= findViewById(R.id.btn_home1);
        Button btnBack= findViewById(R.id.btn_back1);
        btnHome.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());
        loadAvailabilities();
    }

    private void loadAvailabilities() {
        Account currentAccount= LoginManager.getCurrentAccount();
        if (currentAccount==null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Availability> all= AvailabilityReader.GenerateAvailabilityFromAllTutors();
        availabilityList= new ArrayList<>();
        for (Availability a : all) {
            // hides all "booked" availabilities
            if (!a.isBooked()) {
                availabilityList.add(a);
            }
        }
        adapter= new StudentSearchCoursesAdapter(this, availabilityList);
        recyclerView.setAdapter(adapter);
    }
}


