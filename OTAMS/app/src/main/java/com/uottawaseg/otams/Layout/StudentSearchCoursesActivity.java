package com.uottawaseg.otams.Layout;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawaseg.otams.Courses.Course;
import com.uottawaseg.otams.Database.AvailabilityReader;
import com.uottawaseg.otams.Database.StudentSessionManager;
import com.uottawaseg.otams.Layout.support.StudentSearchCoursesAdapter;
import com.uottawaseg.otams.R;

public class StudentSearchCoursesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    protected Course selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_search_courses);
        recyclerView = findViewById(R.id.availability_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        var coursesSpinner = (Spinner) findViewById(R.id.courses);
        // This setups up the two spinners
        // This took way too long to get working I'm not even going to lie.
        var courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Course.GetCourseCodes());
        courseAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        coursesSpinner.setAdapter(courseAdapter);
        coursesSpinner.setSelection(1);

        Button btnHome= findViewById(R.id.btn_home1);
        Button btnBack= findViewById(R.id.btn_back1);
        btnHome.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());



        coursesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

            @Override
            public void onItemSelected(AdapterView parent, View v, int position, long id) {
                selectedCourse = Course.FromString(parent.getItemAtPosition(position).toString());
                loadAvailabilities();
            }
        });

        selectedCourse = Course.GNG1105;
        loadAvailabilities();
    }
    private void loadAvailabilities() {
        System.out.println(selectedCourse);
        if(selectedCourse == null) selectedCourse = Course.GNG1105;
        var all = AvailabilityReader.GenerateAvailabilityFromAllTutors(selectedCourse);
        var adapter = new StudentSearchCoursesAdapter(this, all);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnClickListener( v -> {
            StudentSessionManager.RequestSession(v.toString());
        });
    }
}


