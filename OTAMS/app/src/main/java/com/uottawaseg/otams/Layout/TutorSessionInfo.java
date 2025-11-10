package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Database.PendingRequestManager;
import com.uottawaseg.otams.Database.SessionRequestManager;
import com.uottawaseg.otams.Layout.support.RecycleViewAdapter;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.AccountRequestDisplayManager;
import com.uottawaseg.otams.Requests.TutorSessionRequestDisplayManager;

public class TutorSessionInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.admin_client_info);
        var selected = SessionRequestManager.GetSelected();

        var intro = (TextView) findViewById(R.id.first_name);
        var name = (TextView) findViewById(R.id.last_name);
        var date = (TextView) findViewById(R.id.username);
        var starts = (TextView) findViewById(R.id.email);
        var ends = (TextView) findViewById(R.id.other);

        var strings = TutorSessionRequestDisplayManager.Display(selected);

        intro.setText(strings[TutorSessionRequestDisplayManager.INTRO]);
        name.setText(strings[TutorSessionRequestDisplayManager.NAME]);
        date.setText(strings[TutorSessionRequestDisplayManager.DATE]);
        starts.setText(strings[TutorSessionRequestDisplayManager.STARTS_AT]);
        ends.setText(strings[TutorSessionRequestDisplayManager.ENDS_AT]);

        var acceptButton = (Button) findViewById(R.id.accept_button);
        var denyButton = (Button) findViewById(R.id.decline_button);
        var returnButton = (Button) findViewById(R.id.btn_back1);

        acceptButton.setOnClickListener(v -> {
            SessionRequestManager.Accept(selected);
            Toast.makeText(this,
                    "Accepted request from: " + selected.getStudent() +" for " + selected.getDate(),
                    Toast.LENGTH_LONG).show();
            finish();
        });

        denyButton.setOnClickListener(v -> {
            SessionRequestManager.Decline(selected);
            Toast.makeText(this,
                    "Denied request from: " + selected.getStudent() +" for " + selected.getDate(),
                    Toast.LENGTH_LONG).show();
            finish();
        });
        returnButton.setOnClickListener(v -> {
            startActivity(new Intent(TutorSessionInfo.this, TutorViewPending.class));
            finish();
        });


    }
}
