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
import com.uottawaseg.otams.Layout.TutorSessionInfo;
import com.uottawaseg.otams.R;

import java.time.format.DateTimeFormatter;

public class TutorViewPendingAdapter extends RecyclerView.Adapter<TutorViewPendingAdapter.ViewHolder> {

    private String[] dataset;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public TutorViewPendingAdapter(String[] data) {
        this.dataset= data;
    }
    public void updateDataset(String[] newData) {
        this.dataset= newData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text;
        public ViewHolder(View view) {
            super(view);
            text= view.findViewById(R.id.textView);
            text.setOnClickListener( v -> {
                var strs = text.getText().toString().split("\n");
                var studentUser = strs[1];
                var date = strs[2];

                if(SessionRequestManager.Select(studentUser, date)) {
                    v.getContext().startActivity(new Intent(v.getContext(), TutorSessionInfo.class));
                } else {
                    Toast.makeText(v.getContext(), "Error getting the selected request", Toast.LENGTH_LONG).show();
                }

            });
        }
        public TextView getText() {
            return text;
        }
    }

    @NonNull
    @Override
    public TutorViewPendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.do_not_delete_recycler_view_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorViewPendingAdapter.ViewHolder holder, int position) {

        holder.getText().setText(dataset[position]);
        /*Availability availability= dataset.get(position);
        String tutorName= availability.getTutorFirstName() + " " + availability.getTutorLastName();
        String time= availability.getStart().format(timeFormatter) + " - " + availability.getEnd().format(timeFormatter);
        String studentInfo= "";
        if (availability.getStudentFirstName() != null && availability.getStudentLastName() != null) {
            studentInfo= "\nRequested by: " + availability.getStudentFirstName() + " " + availability.getStudentLastName();
        }

        String displayText= tutorName + "\nDate: " + availability.getDate() + "\nTime: " + time + studentInfo;
        holder.getText().setText(displayText);

        holder.itemView.setOnClickListener(v -> {
            if (availability.isBooked()) {
                Toast.makeText(v.getContext(), "This session is already booked.", Toast.LENGTH_SHORT).show();
                return;
            }
            StudentAvailabilityWriter.bookAvailabilityForStudent(availability, availability.getStudentFirstName(), availability.getStudentLastName(), availability.getStudentUsername(), (success, errorMessage) -> {
                if (success) {
                    Toast.makeText(v.getContext(), "Request approved and session booked!", Toast.LENGTH_SHORT).show();
                    dataset.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Toast.makeText(v.getContext(), "Failed to approve request: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });*/
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }
}


