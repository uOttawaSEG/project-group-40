package com.uottawaseg.otams.Layout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Accounts.Student;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.Database.AvailabilityReader;
import com.uottawaseg.otams.Layout.support.StudentReadonlyAvailabilityAdapter;
import com.uottawaseg.otams.Requests.Availability;
import com.uottawaseg.otams.R;

import java.util.ArrayList;
import java.util.List;

public class StudentPendingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentReadonlyAvailabilityAdapter adapter;
    private List<Availability> pendingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_pending);
        recyclerView= findViewById(R.id.pending_availability_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button btnHome= findViewById(R.id.btn_home2);
        Button btnBack= findViewById(R.id.btn_back2);
        btnHome.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());
        loadPending();
    }

    private void loadPending() {
        Student currentStudent= (Student) LoginManager.getCurrentAccount();
        if (currentStudent==null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String studentUsername= currentStudent.getUsername();
        List<Availability> all= AvailabilityReader.GenerateAvailabilityFromAllTutors();
        pendingList= new ArrayList<>();

        for (Availability a : all) {
            if (!a.isBooked() && studentUsername.equals(a.getStudentUsername())) {
                pendingList.add(a);
            }
        }
        adapter= new StudentReadonlyAvailabilityAdapter(this, pendingList);
        recyclerView.setAdapter(adapter);
    }
}



