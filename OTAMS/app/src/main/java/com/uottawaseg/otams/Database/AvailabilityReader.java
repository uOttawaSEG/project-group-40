package com.uottawaseg.otams.Database;

import com.google.firebase.database.DataSnapshot;
import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Requests.Availability;

import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AvailabilityReader {
    public static final String AVAILABILITIES = "availabilities";
    public static final String AUTOAPPROVE = "autoApprove";
    public static final String START = "start";
    public static final String END = "end";
    public static final String DATE = "date";

    public static List<Availability> GenerateAvailability(Tutor tut) {
        if(tut==null || tut.getUsername()==null){
            return new ArrayList<>();
        }
        return GenerateAvailability(tut.getUsername());
    }

    public static List<Availability> GenerateAvailability(String username) {
        var data = Database.Database.Read(LoginManager.ACCOUNTS + "/" + username + "/" + AVAILABILITIES);
        return GenerateAvailability(data);
    }

    private static List<Availability> GenerateAvailability(DataSnapshot ds) {
        var list= new ArrayList<Availability>();
        if (ds==null){
            return list;
        }

        for (var item : ds.getChildren()) {
            try {
                Boolean autoApprove= item.child(AUTOAPPROVE).getValue(Boolean.class);

                String dateStr= item.child(DATE).getValue(String.class);
                LocalDate date= LocalDate.now();
                if (dateStr != null) {
                    try {
                        date= LocalDate.parse(dateStr);
                    } catch(Exception ignored) {}
                }
                Object startObj= item.child(START).getValue();
                Object endObj= item.child(END).getValue();
                if (startObj != null && endObj != null) {
                    OffsetTime startTime;
                    OffsetTime endTime;

                    // From Daniil: handles legacy string format... by legacy I mean whatever we used before
                    if (startObj instanceof String && endObj instanceof String) {
                        startTime = parseLegacyString((String) startObj);
                        endTime = parseLegacyString((String) endObj);
                    } else {
                        // From Daniil: new nested map format, which database doesnt currently use.... Im gonna cry
                        startTime = readOffsetTime((HashMap) startObj);
                        endTime = readOffsetTime((HashMap) endObj);
                    }

                    list.add(new Availability(
                            autoApprove != null && autoApprove,
                            startTime,
                            endTime,
                            date
                    ));
                }
            } catch (Exception e) {
                e.printStackTrace(); // log any issues but continue
            }
        }
        return list;
    }

    //From Daniil: parses old string format
    private static OffsetTime parseLegacyString(String str) {
        try {
            String[] parts = str.split("-");
            String[] hm = parts[0].split(":");
            int hour = Integer.parseInt(hm[0]);
            int minute = Integer.parseInt(hm[1]);
            ZoneOffset offset = ZoneOffset.of(parts[1]);
            return OffsetTime.of(hour, minute, 0, 0, offset);
        } catch(Exception e) {
            return OffsetTime.of(0,0,0,0, ZoneOffset.UTC);
        }
    }


    public static OffsetTime readOffsetTime(HashMap map) {
        if (map==null){
            return OffsetTime.of(0,0,0,0, ZoneOffset.UTC);
        }
        Object h= map.get("hour");
        Object m= map.get("minute");
        int hours= h instanceof Number ? ((Number) h).intValue() : 0;
        int minutes= m instanceof Number ? ((Number) m).intValue() : 0;
        ZoneOffset offset= ZoneOffset.UTC;
        Object offsetMap= map.get("offset");
        if (offsetMap instanceof HashMap) {
            Object idObj= ((HashMap) offsetMap).get("id");
            if (idObj != null) {
                try {
                    offset= ZoneOffset.of(idObj.toString());
                } catch(Exception ignored) {}
            }
        }

        return OffsetTime.of(hours, minutes, 0, 0, offset);
    }
}

