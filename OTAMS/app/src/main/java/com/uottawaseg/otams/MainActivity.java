package com.uottawaseg.otams;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Added import for access to the DB
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseTest"; // added
    private DatabaseReference mDatabase; // added


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // --------------- The following is test code to make sure the DB is working ---------------
        // From online & ChatGPT


        // initializing Firebase DB reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // test write to DB
        mDatabase.child("test").setValue("Hello, Firebase!");

        // test read from DB
        mDatabase.child("test").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                System.out.println("Firebase read success! Value = " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Firebase read failed: " + error.getMessage());
            }
        });
    }
}