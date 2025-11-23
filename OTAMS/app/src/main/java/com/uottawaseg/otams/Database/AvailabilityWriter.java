package com.uottawaseg.otams.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uottawaseg.otams.Requests.Availability;

import java.time.OffsetTime;
import java.util.HashMap;
import java.util.List;

public class AvailabilityWriter {


    public static void writeAllAvailabilities(String username, List<Availability> availabilities) {
        DatabaseReference ref= FirebaseDatabase.getInstance()
                .getReference(LoginManager.ACCOUNTS)
                .child(username)
                .child("availabilities");
        HashMap<String, Object> all= new HashMap<>();
        for (int i=0; i<availabilities.size(); i++) {
            Availability a= availabilities.get(i);
            HashMap<String, Object> map= new HashMap<>();
            map.put("autoApprove", a.getAutoApprove());
            map.put("date", a.getDate().toString());
            map.put("start", convertOffsetTime(a.getStart()));
            map.put("end", convertOffsetTime(a.getEnd()));
            all.put(String.valueOf(i), map);
        }
        // clears slots to prevent cloning
        ref.setValue(all);
    }

    private static HashMap<String, Object> convertOffsetTime(OffsetTime t) {
        HashMap<String, Object> map= new HashMap<>();
        map.put("hour", t.getHour());
        map.put("minute", t.getMinute());
        HashMap<String, Object> offsetMap= new HashMap<>();
        offsetMap.put("id", t.getOffset().toString());
        map.put("offset", offsetMap);
        return map;
    }
}




