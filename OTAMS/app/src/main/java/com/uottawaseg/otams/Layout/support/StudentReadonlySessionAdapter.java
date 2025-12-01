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
import com.uottawaseg.otams.Requests.Availability;
import com.uottawaseg.otams.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentReadonlySessionAdapter extends RecyclerView.Adapter<StudentReadonlySessionAdapter.ViewHolder> {

    private final List<String> dataset;
    private final Context context;
    private final Student stud;

    public StudentReadonlySessionAdapter(Context context, List<String> dataset, Student acc) {
        this.context= context;
        this.dataset= dataset;
        stud = acc;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.do_not_delete_recycler_view_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getText().setText(dataset.get(position));
        holder.getText().setOnClickListener(v -> {
            // We need to extrapolate the time info D:
            // Date : YYYY-MM-DD \n
            // Start: HH:MM-HH:MM
            // The -HH:MM is for the time diff from UTC
            // End is same as start
            var strs = dataset.get(position).split("\n");
            // D a t e : _ Y Y Y Y  -  M  M  -  D  D
            // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
            var dateStr = strs[0];

            // S t a r t :   H H : M M - H H : M M
            // 0 1 2 3 4 5 6 7
            var startTimeStr = strs[1].split("-")[0];
            var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            var timeFormatter = DateTimeFormatter.ofPattern("HH:mm");


            var date = LocalDate.from(dateFormatter.parse(dateStr.substring(6)));
            var startTime = LocalTime.parse(startTimeStr.substring(7), timeFormatter);

            var localDateTime = LocalDateTime.of(date, startTime);
            var offsetDateTime = OffsetDateTime.of(localDateTime, OffsetDateTime.now().getOffset());

            if(offsetDateTime.minusDays(1L).isBefore(OffsetDateTime.now())) {
                Toast.makeText(v.getContext(), "Cannot delete a session less than 24h away", 1).show();
                return;
            }

            dataset.remove(position);
            notifyDataSetChanged();
            stud.RemoveSession(position);
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
