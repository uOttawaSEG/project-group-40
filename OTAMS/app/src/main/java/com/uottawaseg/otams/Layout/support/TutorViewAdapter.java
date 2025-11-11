package com.uottawaseg.otams.Layout.support;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Database.SessionRequestManager;
import com.uottawaseg.otams.Layout.MainActivity;
import com.uottawaseg.otams.Layout.TutorSessionInfo;
import com.uottawaseg.otams.Layout.TutorViewPending;
import com.uottawaseg.otams.R;

import java.util.Arrays;

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
                var str = text.getText().toString().split("\n");
                var username = str[1];
                var dateStr = str[2];
                var found = SessionRequestManager.Select(username, dateStr);
                if(found) {
                    v.getContext().startActivity(new Intent(v.getContext(), TutorSessionInfo.class));
                }
                else {
                    Toast.makeText(v.getContext(), "Unable to select request", Toast.LENGTH_SHORT);
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
