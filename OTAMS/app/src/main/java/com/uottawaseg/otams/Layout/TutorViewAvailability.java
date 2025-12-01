package com.uottawaseg.otams.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.R;
import com.uottawaseg.otams.Requests.Availability;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TutorViewAvailability extends AppCompatActivity {

    private List<Availability> availabilityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_view_availability);

        // Get the recycler view ready
        RecyclerView recyclerView = findViewById(R.id.availability_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Availability slots from database
        // Empty list cause no database
        availabilityList = ((Tutor) LoginManager.getCurrentAccount()).getAvailabilities();

        // Set up adapter
        var adapter = new AvailabilityAdapter(availabilityList);
        recyclerView.setAdapter(adapter);

        // Back button goes to calendar
        Button backButton = findViewById(R.id.btn_back1);
        backButton.setOnClickListener(v -> finish());

        Button HomePage= findViewById(R.id.home2);
        HomePage.setOnClickListener(v ->
                startActivity(new Intent(TutorViewAvailability.this, MainActivity.class)));
    }

    // Adapter for showing the list of availability slots
    private class AvailabilityAdapter extends RecyclerView.Adapter<AvailabilityAdapter.ViewHolder> {

        private final List<Availability> slots;

        //from Daniil: My changes start here
        private final DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("HH:mm"); //from Daniil: I need this for hour slot

        public AvailabilityAdapter(List<Availability> slots) {
            this.slots = slots;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tutor_availability_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Availability slot = slots.get(position);

            //from Daniil: changed this to work with DateTimeFormatter
            holder.dateText.setText("Weekday: " + slot.getDay());
            holder.timeText.setText("Time: " + slot.getStart().format(timeFormatter) + " - " + slot.getEnd().format(timeFormatter));
            holder.approvalText.setText("Auto-approve: " + (slot.getAutoApprove() ? "Yes" : "No"));

            // When delete button is clicked
            holder.deleteButton.setOnClickListener(v -> {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    // Remove it from the list
                    var tut = ((Tutor) LoginManager.getCurrentAccount());
                    var canDelete = true;
                    var sessions = tut.getSessions();
                    for(var s : sessions) {
                        // If it's on the same day of the week
                        // And the start time is <= the session start time
                        // and the end time is >= the session end time
                        // We cannot delete it
                        if (DayOfWeek.from(s.getDate()) == slot.getDay() &&
                                (
                                    (
                                            slot.getStart().isBefore(s.getStartTime()) ||
                                            slot.getStart().isEqual(s.getStartTime())
                                    ) &&
                                    (
                                            slot.getEnd().isAfter(s.getEndTime()) ||
                                            slot.getEnd().isEqual(s.getEndTime())
                                    )
                                )
                            ) {
                            canDelete = false;
                        }
                    }
                    if(canDelete) {

                        // Update the display
                        notifyItemRemoved(currentPosition);
                        // Delete from database too maybe?
                        Toast.makeText(TutorViewAvailability.this,
                                "Availability deleted", Toast.LENGTH_SHORT).show();
                        tut.removeAvailability(slot);
                    } else {
                        Toast.makeText(TutorViewAvailability.this,
                                "Unable to delete availability due to there being a session during the given slot",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return slots.size();
        }

        // Hold the views for each slot item
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView dateText;
            TextView timeText;
            TextView approvalText;
            Button deleteButton;

            ViewHolder(View itemView) {
                super(itemView);
                dateText = itemView.findViewById(R.id.date_text);
                timeText = itemView.findViewById(R.id.time_text);
                approvalText = itemView.findViewById(R.id.approval_text);
                deleteButton = itemView.findViewById(R.id.delete_button);
            }
        }
    }
}
