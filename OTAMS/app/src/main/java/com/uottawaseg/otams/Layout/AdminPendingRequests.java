package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.R;

public class AdminPendingRequests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.admin_pending_requests);

        Button goto_rejected = findViewById(R.id.button_goto_pending);
        goto_rejected.setOnClickListener(view ->
                startActivity(new Intent(AdminPendingRequests.this, AdminRejectedRequests.class)));

        Button go_home = findViewById(R.id.btn_return);
        go_home.setOnClickListener(view ->
                startActivity(new Intent(AdminPendingRequests.this, MainActivity.class)));

    }

}
