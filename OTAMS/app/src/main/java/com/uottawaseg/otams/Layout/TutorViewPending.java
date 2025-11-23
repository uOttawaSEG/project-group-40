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

public class TutorViewPending extends AppCompatActivity {
    protected static TutorViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.tutor_view_pending);
        Button go_home = findViewById(R.id.btn_back1);
        go_home.setOnClickListener(view -> {
            startActivity(new Intent(TutorViewPending.this, TutorWeeklyViewActivity.class));
            finish();
        });

        var recycleView = (RecyclerView) findViewById(R.id.pending_recycler);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        var tut = (Tutor) LoginManager.getCurrentAccount();
        adapter = new TutorViewAdapter(TutorSessionRequestDisplayManager.GetRequests(
                SessionRequestManager.GenerateSessions(tut.getUsername()
                )));
        recycleView.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        var recycleView = (RecyclerView) findViewById(R.id.pending_recycler);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        if (adapter == null) {
            var tut = (Tutor) LoginManager.getCurrentAccount();
            adapter = new TutorViewAdapter(TutorSessionRequestDisplayManager.GetRequests(
                    SessionRequestManager.GeneratePendingRequests(tut.getUsername()
                    )));
        }
        recycleView.setAdapter(adapter);

    }

}
