package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.Layout.support.TutorViewPendingAdapter;
import com.uottawaseg.otams.Requests.Availability;
import com.uottawaseg.otams.R;

import java.util.ArrayList;
import java.util.List;

public class TutorViewPending extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TutorViewPendingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tutor_view_pending);
        Button btnBack= findViewById(R.id.btn_back1);
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(TutorViewPending.this, TutorWeeklyViewActivity.class));
            finish();
        });
        recyclerView= findViewById(R.id.pending_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Tutor currentTutor= (Tutor) LoginManager.getCurrentAccount();
        if (currentTutor != null) {
            loadPendingRequests(currentTutor);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tutor currentTutor= (Tutor) LoginManager.getCurrentAccount();
        if (currentTutor != null && adapter != null) {
            loadPendingRequests(currentTutor);
        }
    }

    private void loadPendingRequests(Tutor tutor) {
        List<Availability> pendingRequests = getPendingAvailabilities(tutor);
        if (adapter==null) {
            adapter= new TutorViewPendingAdapter(pendingRequests);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateDataset(pendingRequests);
        }
    }

    private List<Availability> getPendingAvailabilities(Tutor tutor) {
        List<Availability> all= tutor.getAvailabilities();
        List<Availability> pending= new ArrayList<>();
        if (all != null) {
            for (Availability a : all) {
                if (!a.isBooked() && a.getStudentUsername() != null) {
                    pending.add(a);
                }
            }
        }
        return pending;
    }
}



