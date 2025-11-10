package com.uottawaseg.otams.Layout.support;

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
import com.uottawaseg.otams.R;

// Based off
// https://github.com/android/views-widgets-samples/blob/main/RecyclerView/Application/src/main/java/com/example/android/recyclerview/CustomAdapter.java
public class TutorViewAdapter extends RecyclerView.Adapter<TutorViewAdapter.ViewHolder> {
    protected String[] dataset;
    public TutorViewAdapter(String[] data) {
        dataset = data;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        public ViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.textView);
            text.setOnClickListener(v -> {

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
                .inflate(R.layout.do_not_delete_recycler_view_text, parent, false);
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
