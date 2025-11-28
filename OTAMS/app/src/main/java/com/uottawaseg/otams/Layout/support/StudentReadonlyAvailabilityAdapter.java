package com.uottawaseg.otams.Layout.support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Requests.Availability;
import com.uottawaseg.otams.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentReadonlySessionAdapter extends RecyclerView.Adapter<StudentReadonlySessionAdapter.ViewHolder> {

    private final List<String> dataset;
    private final Context context;
    private final DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("HH:mm");

    public StudentReadonlySessionAdapter(Context context, List<String> dataset) {
        this.context= context;
        this.dataset= dataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            text= itemView.findViewById(R.id.textView);
        }

        public TextView getText() {
            return text;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.do_not_delete_recycler_view_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position
    ) {
        Availability a= dataset.get(position);
        String time= a.getStart().format(timeFormatter) + " - " + a.getEnd().format(timeFormatter);
        String text= a.getTutorFirstName() + " " + a.getTutorLastName() + "\n" + "Date: " + a.getDate().toString() + "\n" + "Time: " + time;
        if (a.isBooked()) {
            text+= "\n(BOOKED)";
        } else {
            text+= "\n(PENDING APPROVAL)";
        }
        holder.getText().setText(text);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
