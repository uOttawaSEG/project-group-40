package com.uottawaseg.otams.Layout;

import static com.uottawaseg.otams.Layout.TutorViewPending.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.Database.SessionRequestManager;
import com.uottawaseg.otams.Layout.support.TutorViewAdapter;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.TutorSessionRequestDisplayManager;

public class TutorViewUpcoming extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tutor_view_upcoming);


        Button btn_back = findViewById(R.id.btn_back);
        // Modify this as needed
        btn_back.setOnClickListener(view ->
                finish());

        Button btn_past_sessions= findViewById(R.id.pastSessions);
        btn_past_sessions.setOnClickListener(v ->
                startActivity(new Intent(TutorViewUpcoming.this, TutorViewPastSessions.class)));


        Button home = findViewById(R.id.home2);
        // Modify this as needed
        home.setOnClickListener(view ->
                startActivity(new Intent(TutorViewUpcoming.this, TutorWeeklyViewActivity.class)));

        Button viewPending = findViewById(R.id.viewPending);
        viewPending.setOnClickListener(view -> startActivity(
                new Intent(TutorViewUpcoming.this, TutorViewPending.class)));

        var recycler = (RecyclerView)findViewById(R.id.availability_recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        var tut = (Tutor) LoginManager.getCurrentAccount();
        adapter = new TutorViewAdapter(TutorSessionRequestDisplayManager.GetUpcomingRequests(
                SessionRequestManager.GenerateSessions(
                        tut.getUsername()
                )
        ));
        recycler.setAdapter(adapter);
    }
}
