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
//        Availability a= dataset.get(position);
//        String time= a.getStart().format(timeFormatter) + " - " + a.getEnd().format(timeFormatter);
//        String text= a.getTutorFirstName() + " " + a.getTutorLastName() + "\n" + "Date: " + a.getDate().toString() + "\n" + "Time: " + time;
//        if (a.isBooked()) {
//            text+= "\n(BOOKED)";
//        } else {
//            text+= "\n(PENDING APPROVAL)";
//        }

        /*
        * if (s == null) return "";
        return "Date: " + s.getDate().toLocalDate() + "\n" +
                "Start: " + s.getStartTime() + "\n" +
                "End: " + s.getEndTime() + "\n" +
                "Tutor: " + s.getTutor() + "\n" +
                "Course: " + s.getCourse();
        * */

//        var strs = dataset.get(position).split("\n");
//        // String format:
//        // 0 1 2 3 4 5 6 7 8 9 10
//        // D a t e :   localDate                0
//
//        // 0 1 2 3 4 5 6 7 8 9 10
//        // S t a r t :   StartTime              1
//
//        // 0 1 2 3 4 5 6 7 8 9 10
//        // E n d :   EndTime                    2
//
//        // 0 1 2 3 4 5 6 7 8 9 10
//        // T u t o r :   TutorUsername          3
//
//        // 0 1 2 3 4 5 6 7 8 9 10
//        // C o u r s e :   Course               4
//        var dateStr = strs[0].substring(6);
//        var startStr = strs[1].substring(7);
//        var endStr = strs[2].substring(5);
//        var tutorStr = strs[3].substring(7);
//        var courseStr = strs[4].substring(8);

        holder.getText().setText(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
