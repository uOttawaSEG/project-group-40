package com.uottawaseg.otams.Layout;

import android.content.Intent;
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
    private List<AvailabilitySlot> availabilityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.tutor_view_availability);

        //getting the recycler view ready
        recyclerView = findViewById(R.id.availability_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //needs to get availability slots from database

        // for now there's just an empty list
        availabilityList = new ArrayList<>();

        //set up adapter
        adapter = new AvailabilityAdapter(availabilityList);
        recyclerView.setAdapter(adapter);

        //back button goes to calendar
        Button backButton = findViewById(R.id.btn_return);
        backButton.setOnClickListener(v -> {
            //replace with actual calendar activity class name
            finish();
        }); //idk if i can do this tbh i jusr free balled it
    }

    //adapter for showing the list of availability slots
    private class AvailabilityAdapter extends RecyclerView.Adapter<AvailabilityAdapter.ViewHolder> {

        private List<AvailabilitySlot> slots;

        public AvailabilityAdapter(List<AvailabilitySlot> slots) {
            this.slots = slots;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.availability_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AvailabilitySlot slot = slots.get(position);

            //fill in the text
            holder.dateText.setText("Date: " + slot.date);
            holder.timeText.setText("Time: " + slot.startTime + " - " + slot.endTime);
            holder.approvalText.setText("Auto-approve: " + (slot.autoApprove ? "Yes" : "No"));

            //when delete button is clicked
            holder.deleteButton.setOnClickListener(v -> {
                //get where the item is in the list
                int currentPosition = holder.getAdapterPosition();

                if (currentPosition != RecyclerView.NO_POSITION) {
                    //remove it from the list
                    slots.remove(currentPosition);

                    //update the display
                    notifyItemRemoved(currentPosition);

                    //delete from database too i think

                    Toast.makeText(ViewAvailability.this,"Availability deleted", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return slots.size();
        }

        //hold the views for slot
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

    //holds slot info
    public static class AvailabilitySlot {
        String date;
        String startTime;
        String endTime;
        boolean autoApprove;

        public AvailabilitySlot(String date, String startTime, String endTime, boolean autoApprove) {
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.autoApprove = autoApprove;
        }
    }
}