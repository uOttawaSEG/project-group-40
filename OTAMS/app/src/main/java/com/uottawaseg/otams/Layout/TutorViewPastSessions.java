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
import com.uottawaseg.otams.Database.SessionRequestManager;
import com.uottawaseg.otams.Layout.support.TutorViewAdapter;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.TutorSessionRequestDisplayManager;

public class TutorViewPastSessions extends AppCompatActivity {
    public static TutorViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tutor_view_past_sessions);


        Button btn_back = findViewById(R.id.btn_back1);
        // Modify this as needed
        btn_back.setOnClickListener(view ->
                startActivity(new Intent(TutorViewPastSessions.this, TutorViewUpcoming.class)));

        Button home = findViewById(R.id.home2);
        // Modify this as needed
        home.setOnClickListener(view ->
                startActivity(new Intent(TutorViewPastSessions.this, TutorViewUpcoming.class)));

        var recycleView = (RecyclerView) findViewById(R.id.availability_recycler);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        if(adapter == null) {
            var tut = (Tutor) LoginManager.getCurrentAccount();
            adapter = new TutorViewAdapter(TutorSessionRequestDisplayManager.GetPastSessions(
                    SessionRequestManager.GenerateSessions(tut.getUsername()
                    )));
        }
        recycleView.setAdapter(adapter);
    }
}
