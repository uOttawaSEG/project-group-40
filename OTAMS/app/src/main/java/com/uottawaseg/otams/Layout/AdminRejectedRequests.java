package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Layout.support.RecycleViewAdapter;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.AccountRequestDisplayManager;

public class AdminRejectedRequests extends AppCompatActivity {

    public static RecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.admin_rejected_requests);

        Button goto_pending = findViewById(R.id.button_goto_pending);
        goto_pending.setOnClickListener(view ->
                startActivity(new Intent(AdminRejectedRequests.this, AdminPendingRequests.class)));

        Button go_home = findViewById(R.id.btn_back1);
        go_home.setOnClickListener(view -> {
            startActivity(new Intent(AdminRejectedRequests.this, MainActivity.class));
        });

        var recycleView = (RecyclerView) findViewById(R.id.denied_recycler);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        if(adapter == null) {
            adapter = new RecycleViewAdapter(AccountRequestDisplayManager.GetRequestForDeniedPage());
        }
        recycleView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        var recycleView = (RecyclerView) findViewById(R.id.denied_recycler);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        if(adapter == null) {
            adapter = new RecycleViewAdapter(AccountRequestDisplayManager.GetRequestForDeniedPage());
        }
        recycleView.setAdapter(adapter);
    }

}
