package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Database.PendingRequestManager;
import com.uottawaseg.otams.EmailManager;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.RequestDisplayManager;

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

        /*
         * This string should look like this:
         *                                    newIndex
         * Title (Request name)                      0
         * Account Details:                          1
         * First name                                2
         * Last name                                 3
         * Username                                  4
         * Phone Number                              5
         * Email                                     6
         * If it's a tutor this is where we'd get their
         * Field of Study                        7
         * Highest degree of study               8

         * Otherwise it's a student account
         * Student ID                            7

         * If not we're cooked
         * */
        var strings = RequestDisplayManager.PrepareRequestToDisplay(selected);

        fName.setText(strings[RequestDisplayManager.FIRST_NAME.second]);
        lName.setText(strings[RequestDisplayManager.LAST_NAME.second]);
        usrnm.setText(strings[RequestDisplayManager.USERNAME.second]);
        email.setText(strings[RequestDisplayManager.EMAIL.second]);
        other.setText(strings[RequestDisplayManager.OTHER.second]);

        var acceptButton = (Button) findViewById(R.id.accept_button);
        var denyButton = (Button) findViewById(R.id.decline_button);
        var pendingButton = (Button) findViewById(R.id.button_pending);
        var deniedButton = (Button) findViewById(R.id.button_rejected);
        var returnButton = (Button) findViewById(R.id.btn_return);

        EmailManager emailManager = new EmailManager(this);

        acceptButton.setOnClickListener(v -> {
            PendingRequestManager.AcceptRequest(selected);
            emailManager.sendAcceptanceEmail(
                    selected.getAccount().getEmail(),
                    selected.getAccount().getName()
            );
            Toast.makeText(this,
                    "Accepted request from: " + selected.getAccount().getName() +" for " + selected.getAccount().getRole(),
                    Toast.LENGTH_LONG);
            finish();
        });

        denyButton.setOnClickListener(v -> {
            PendingRequestManager.DeclineRequest(selected);
            emailManager.sendRejectionEmail(
                    selected.getAccount().getEmail(),
                    selected.getAccount().getName()
            );
            Toast.makeText(this,
                    "Denied request from: " + selected.getAccount().getName() +" for " + selected.getAccount().getRole(),
                    Toast.LENGTH_LONG);
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
