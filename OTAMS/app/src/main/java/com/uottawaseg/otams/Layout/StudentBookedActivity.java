package com.uottawaseg.otams.Layout;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Accounts.Student;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.Database.StudentSessionManager;
import com.uottawaseg.otams.Layout.support.StudentReadonlySessionAdapter;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.Availability;
import com.uottawaseg.otams.Requests.RequestStatus;

import java.util.ArrayList;
import java.util.List;

public class StudentBookedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentReadonlySessionAdapter adapter;
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
        var acc = (Student) LoginManager.getCurrentAccount();
        var sessions = StudentSessionManager.GetSessions(acc);
        var booked = new ArrayList<String>(sessions.size());
        for(var sess : sessions) {
            if (sess.getStatus().equals(RequestStatus.ACCEPTED)) {
                booked.add(StudentSessionManager.PrepareForDisplay(sess));
            }
        }
        adapter = new StudentReadonlySessionAdapter(this, booked);
        recyclerView.setAdapter(adapter);


//        String studentUsername= acc.getUsername();
//        List<Availability> all= AvailabilityReader.GenerateAvailabilityFromAllTutors();
//        bookedList= new ArrayList<>();
//        for (Availability a : all) {
//            // books if isBooked=true
//            if (a.isBooked() && a.getStudentUsername() != null && a.getStudentUsername().equals(studentUsername)) {
//                bookedList.add(a);
//            }
//        }
//        adapter= new StudentReadonlyAvailabilityAdapter(this, bookedList);
//        recyclerView.setAdapter(adapter);
    }
}

