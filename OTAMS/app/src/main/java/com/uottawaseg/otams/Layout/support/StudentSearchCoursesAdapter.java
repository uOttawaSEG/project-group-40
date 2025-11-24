package com.uottawaseg.otams.Layout.support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Accounts.Student;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.Database.StudentAvailabilityWriter;
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
        public ViewHolder(View view) {
            super(view);
            text= view.findViewById(R.id.textView);
        }
        public TextView getText() {
            return text;
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
        Availability availability= dataset.get(position);
        String tutorFirst= availability.getTutorFirstName();
        String tutorLast= availability.getTutorLastName();
        String displayTime= availability.getStart().format(timeFormatter) + " - " + availability.getEnd().format(timeFormatter);
        String studentInfo= "";
        if (availability.isBooked() && availability.getStudentFirstName() != null && availability.getStudentLastName() != null) {
            studentInfo= "\nBooked by: " + availability.getStudentFirstName() + " " + availability.getStudentLastName();
        }
        String displayText= tutorFirst + " " + tutorLast + "\n" + "Date: " + availability.getDate().toString() + "\n" + "Time: " + displayTime + studentInfo;
        if (availability.isBooked()){
            displayText+= "\n(BOOKED)";
        }
        holder.getText().setText(displayText);
        holder.itemView.setOnClickListener(v -> {
            if (availability.isBooked()) {
                Toast.makeText(context, "This session is already booked.", Toast.LENGTH_SHORT).show();
                return;
            }
            Student currentStudent= (Student) LoginManager.getCurrentAccount();
            if (currentStudent==null) {
                Toast.makeText(context, "Error: No logged-in student.", Toast.LENGTH_SHORT).show();
                return;
            }
            String studentFirst= currentStudent.getFirstName();
            String studentLast= currentStudent.getLastName();
            String studentUsername= currentStudent.getUsername();
            availability.setStudentCredentials(studentFirst, studentLast, studentUsername);
            if (availability.getAutoApprove()) {
                StudentAvailabilityWriter.bookAvailabilityForStudent(availability, studentFirst, studentLast, studentUsername, (success, errorMessage) -> {
                    if (success) {
                        Toast.makeText(context, "Session booked successfully!", Toast.LENGTH_SHORT).show();
                        dataset.remove(position);
                        notifyItemRemoved(position);
                    } else {
                        Toast.makeText(context, "Could not process request: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                StudentAvailabilityWriter.sendPendingRequest(availability, studentFirst, studentLast, studentUsername, (success, error) -> {
                    if (success) {
                        Toast.makeText(context, "Request sent. Waiting for approval.", Toast.LENGTH_SHORT).show();
                        dataset.remove(position);
                        notifyItemRemoved(position);
                    } else {
                        Toast.makeText(context, "Could not send request: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    @Override
    public int getItemCount() {
        return dataset.size();
    }
}


