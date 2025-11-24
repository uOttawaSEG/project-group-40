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
import com.uottawaseg.otams.Layout.support.StudentReadonlyAvailabilityAdapter;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.Availability;

import java.util.ArrayList;
import java.util.List;

public class StudentBookedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentReadonlyAvailabilityAdapter adapter;
    private List<Availability> bookedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_booked);
        recyclerView= findViewById(R.id.booked_availability_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button btnHome= findViewById(R.id.btn_home3);
        Button btnBack= findViewById(R.id.btn_back3);
        btnHome.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());
        loadBooked();
    }

    private void loadBooked() {
        Account acc= LoginManager.getCurrentAccount();
        if (acc==null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }
        String studentUsername= acc.getUsername();
        List<Availability> all= AvailabilityReader.GenerateAvailabilityFromAllTutors();
        bookedList= new ArrayList<>();
        for (Availability a : all) {
            // books if isBooked=true
            if (a.isBooked() && a.getStudentUsername() != null && a.getStudentUsername().equals(studentUsername)) {
                bookedList.add(a);
            }
        }
        adapter= new StudentReadonlyAvailabilityAdapter(this, bookedList);
        recyclerView.setAdapter(adapter);
    }
}

