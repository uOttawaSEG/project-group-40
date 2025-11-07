package com.uottawaseg.otams.Layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.R;

import java.util.ArrayList;
import java.util.List;

public class ViewAvailability extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AvailabilityAdapter adapter;
    private List<AddAvailability.AvailabilitySlot> availabilityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.tutor_view_availability);

        //get the recycler view ready
        recyclerView = findViewById(R.id.availability_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //availability slots from database

        //empty list cause no database
        availabilityList = new ArrayList<>();

        //set up adapter
        adapter = new AvailabilityAdapter(availabilityList);
        recyclerView.setAdapter(adapter);

        //back button goes to calendar
        Button backButton = findViewById(R.id.btn_return);
        backButton.setOnClickListener(v -> {
            //calendar activity class name
            finish();
        });
    }

    // adapter for showing the list of availability slots
    private class AvailabilityAdapter extends RecyclerView.Adapter<AvailabilityAdapter.ViewHolder> {

        private List<AddAvailability.AvailabilitySlot> slots;

        public AvailabilityAdapter(List<AddAvailability.AvailabilitySlot> slots) {
            this.slots = slots;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tutor_availability_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AddAvailability.AvailabilitySlot slot = slots.get(position);

            //fill in the text
            holder.dateText.setText("Date: " + slot.date);
            holder.timeText.setText("Time: " + slot.startTime + " - " + slot.endTime);
            holder.approvalText.setText("Auto-approve: " + (slot.autoApprove ? "Yes" : "No"));

            //when delete button is clicked
            holder.deleteButton.setOnClickListener(v -> {
                //get where this is in the list
                int currentPosition = holder.getAdapterPosition();

                if (currentPosition != RecyclerView.NO_POSITION) {
                    //remove it from the list
                    slots.remove(currentPosition);

                    //update the display
                    notifyItemRemoved(currentPosition);

                    //delete from database too maybe?

                    Toast.makeText(ViewAvailability.this,
                            "Availability deleted",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return slots.size();
        }

        //hold the views for each slot item
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