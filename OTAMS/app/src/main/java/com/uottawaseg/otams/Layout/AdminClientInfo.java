package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Database.PendingRequestManager;
import com.uottawaseg.otams.Layout.support.RecycleViewAdapter;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.AccountRequestDisplayManager;

public class AdminClientInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.admin_client_info);
        var selected = PendingRequestManager.GetSelectedRequest();

        var fName = (TextView) findViewById(R.id.first_name);
        var lName = (TextView) findViewById(R.id.last_name);
        var usrnm = (TextView) findViewById(R.id.username);
        var email = (TextView) findViewById(R.id.email);
        // Other is either a student# or their degree + field of study.
        var other = (TextView) findViewById(R.id.other);

        var strings = AccountRequestDisplayManager.PrepareRequestToDisplay(selected);

        fName.setText(strings[AccountRequestDisplayManager.FIRST_NAME.second]);
        lName.setText(strings[AccountRequestDisplayManager.LAST_NAME.second]);
        usrnm.setText(strings[AccountRequestDisplayManager.USERNAME.second]);
        email.setText(strings[AccountRequestDisplayManager.EMAIL.second]);
        other.setText(strings[AccountRequestDisplayManager.OTHER.second]);

        var acceptButton = (Button) findViewById(R.id.accept_button);
        var denyButton = (Button) findViewById(R.id.decline_button);
        var pendingButton = (Button) findViewById(R.id.button_pending);
        var deniedButton = (Button) findViewById(R.id.button_rejected);
        var returnButton = (Button) findViewById(R.id.btn_back1);

        acceptButton.setOnClickListener(v -> {
            PendingRequestManager.AcceptRequest(selected);
            Toast.makeText(this,
                    "Accepted request from: " + selected.getAccount().getName() +" for " + selected.getAccount().getRole(),
                    Toast.LENGTH_LONG).show();
            AdminPendingRequests.adapter = new RecycleViewAdapter(AccountRequestDisplayManager.GetRequestForPendingPage());
            AdminRejectedRequests.adapter = new RecycleViewAdapter(AccountRequestDisplayManager.GetRequestForDeniedPage());
            finish();
        });

        denyButton.setOnClickListener(v -> {
            PendingRequestManager.DeclineRequest(selected);
            Toast.makeText(this,
                    "Denied request from: " + selected.getAccount().getName() +" for " + selected.getAccount().getRole(),
                    Toast.LENGTH_LONG).show();

            AdminPendingRequests.adapter = new RecycleViewAdapter(AccountRequestDisplayManager.GetRequestForPendingPage());
            AdminRejectedRequests.adapter = new RecycleViewAdapter(AccountRequestDisplayManager.GetRequestForDeniedPage());
            finish();
        });

        pendingButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminClientInfo.this, AdminPendingRequests.class));
            finish();
        });

        deniedButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminClientInfo.this, AdminRejectedRequests.class));
            finish();
        });
        returnButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminClientInfo.this, AdminPendingRequests.class));
            finish();
        });


    }
}
