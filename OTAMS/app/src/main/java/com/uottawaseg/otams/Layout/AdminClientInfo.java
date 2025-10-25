package com.uottawaseg.otams.Layout;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.Database.PendingRequestManager;
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
    }
}
