package com.uottawaseg.otams.Layout.support;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Courses.Course;
import com.uottawaseg.otams.Database.StudentSessionManager;
import com.uottawaseg.otams.Layout.AdminPendingRequests;
import com.uottawaseg.otams.Layout.Login;
import com.uottawaseg.otams.Layout.StudentBooking;
import com.uottawaseg.otams.Layout.StudentSearchCoursesActivity;
import com.uottawaseg.otams.Requests.Availability;
import com.uottawaseg.otams.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentSearchCoursesAdapter extends RecyclerView.Adapter<StudentSearchCoursesAdapter.ViewHolder> {

    private final List<Availability> dataset;
    private final Context context;
    private final DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("HH:mm");


    public StudentSearchCoursesAdapter(Context context, List<Availability> data) {
        this.context= context;
        this.dataset= data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text;
        public static Course SelectedCourse;
        private Availability avail;
        private View view;
        public ViewHolder(View view) {
            super(view);

            text = view.findViewById(R.id.textView);
            this.view = view;

        }
        public TextView getText() {
            return text;
        }
        void SetAvailability(Availability a) {
            avail = a;
            text.setOnClickListener(v -> {

                StudentBooking.SetupBooking(text.getText().toString(), avail, SelectedCourse);
                v.getContext().startActivity(new Intent(v.getContext(), StudentBooking.class));
            });
        }
    }

    @NonNull
    @Override
    public StudentSearchCoursesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.do_not_delete_recycler_view_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentSearchCoursesAdapter.ViewHolder holder, int position) {
        var avail = dataset.get(position);
        var tutorUsername = avail.getTutor();
        var start = avail.getStart();
        var end = avail.getEnd();
        var weekday = avail.getDay();
        holder.SetAvailability(avail);
        var str = tutorUsername + " has availability on " + weekday.toString() +
                " from: " + start.toLocalTime().format(timeFormatter) + " - " + end.toLocalTime().format(timeFormatter);
        holder.getText().setText(str);
    }
    @Override
    public int getItemCount() {
        return dataset.size();
    }

}


