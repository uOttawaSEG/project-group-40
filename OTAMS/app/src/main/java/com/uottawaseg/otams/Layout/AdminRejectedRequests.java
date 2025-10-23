package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uottawaseg.otams.R;

public class AdminRejectedRequests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.admin_rejected_requests);

        Button goto_pending = findViewById(R.id.button_goto_pending);
        goto_pending.setOnClickListener(view ->
                startActivity(new Intent(AdminRejectedRequests.this, AdminPendingRequests.class)));

        Button go_home = findViewById(R.id.btn_return);
        go_home.setOnClickListener(view ->
                startActivity(new Intent(AdminRejectedRequests.this, MainActivity.class)));

    }

}
