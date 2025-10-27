package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Database.PendingRequestManager;
import com.uottawaseg.otams.Layout.support.RecycleViewAdapter;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.AccountCreationRequest;
import com.uottawaseg.otams.Requests.RequestDisplayManager;

import java.util.List;

public class AdminPendingRequests extends AppCompatActivity {
    public static RecycleViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.admin_pending_requests);

        Button goto_rejected = findViewById(R.id.button_goto_pending);
        goto_rejected.setOnClickListener(view ->
                startActivity(new Intent(AdminPendingRequests.this, AdminRejectedRequests.class)));

        Button go_home = findViewById(R.id.btn_return);
        go_home.setOnClickListener(view -> {
                startActivity(new Intent(AdminPendingRequests.this, MainActivity.class));
                finish();
                });

        var recycleView = (RecyclerView) findViewById(R.id.pending_recycler);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        if(adapter == null) {
            adapter = new RecycleViewAdapter(RequestDisplayManager.GetRequestForPendingPage());
        }
        recycleView.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        var recycleView = (RecyclerView) findViewById(R.id.pending_recycler);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        if(adapter == null) {
            adapter = new RecycleViewAdapter(RequestDisplayManager.GetRequestForDeniedPage());
        }
        recycleView.setAdapter(adapter);
    }

}
