package com.uottawaseg.otams.Layout.support;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.R;

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
