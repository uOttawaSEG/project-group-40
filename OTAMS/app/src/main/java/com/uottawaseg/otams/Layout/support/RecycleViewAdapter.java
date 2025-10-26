package com.uottawaseg.otams.Layout.support;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Database.DeniedRequestManager;
import com.uottawaseg.otams.Database.PendingRequestManager;
import com.uottawaseg.otams.Layout.AdminClientInfo;
import com.uottawaseg.otams.Layout.AdminPendingRequests;
import com.uottawaseg.otams.Layout.Login;
import com.uottawaseg.otams.R;

import java.util.Arrays;

// Based off
// https://github.com/android/views-widgets-samples/blob/main/RecyclerView/Application/src/main/java/com/example/android/recyclerview/CustomAdapter.java
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    protected String[] dataset;
    public RecycleViewAdapter(String[] data) {
        dataset = data;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        public ViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.textView);
            text.setOnClickListener(v -> {
                /*
                * Set clicked request to current
                    * We need a function to extra the username from the text
                * Change page to admin_client_info
                * */

                /*
                * Layout for the text:
                * First name Last name \n
                * Username: <username>
                * Role: <role>

                * This will become
                * name                  -> 0
                * Username: <username>  -> 1
                * Role: <role>          -> 2
                * */
                var text = getText().getText().toString().split("\n");

                /*
                * This should end up as
                * Username -> 0
                * <username> -> 1
                * */
                var usernameText = text[1].split(": ")[1].trim();
                // Now that we have the username we can find the request very quickly.

                if(PendingRequestManager.SelectRequest(usernameText)
                        || DeniedRequestManager.SelectRequest(usernameText)) {
                    v.getContext().startActivity(new Intent(v.getContext(), AdminClientInfo.class));
                } else {
                    Toast.makeText(v.getContext(), "Error getting the selected request", Toast.LENGTH_LONG).show();
                }
            });
        }

        public void setText(TextView t) {
            text = t;
        }
        public TextView getText() {
            return text;
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        var view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getText().setText(dataset[position]);
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }
}
