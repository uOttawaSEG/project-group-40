package com.uottawaseg.otams.Database;

import com.google.firebase.database.DataSnapshot;
import com.uottawaseg.otams.Accounts.Account;
import com.uottawaseg.otams.Courses.Course;
import com.uottawaseg.otams.Courses.Field;
import com.uottawaseg.otams.Requests.Availability;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AvailabilityReader {
    public static final String AVAILABILITIES = "availabilities";
    public static final String AUTO_APPROVE = "autoApprove";
    public static final String START = "start";
    public static final String END = "end";
    public static final String DAY = "day";

    public static List<Availability> GenerateAvailability(String username) {
        var data = Database.Database.Read(LoginManager.ACCOUNTS + "/" + username + "/" + AVAILABILITIES);
        return GenerateAvailability(data);
    }

    private static List<Availability> GenerateAvailability(DataSnapshot ds) {
        var list = new ArrayList<Availability>();
        if (ds == null) {
            return list;
        }

        var children = ds.getChildren();
        for (var item : children) {
            var autoApprove = item.child(AUTO_APPROVE).getValue(Boolean.class);
            var day = item.child(DAY).getValue(String.class);
            var start = item.child(START).getValue();
            var end = item.child(END).getValue();

            list.add(new Availability(
                    autoApprove,
                    readOffsetTime((HashMap) start),
                    readOffsetTime((HashMap) end),
                    DayOfWeek.valueOf(day)
            ));
        }
        /*for (var item : ds.getChildren()) {
            try {
                Boolean autoApprove= item.child(AUTOAPPROVE).getValue(Boolean.class);
                Boolean pending = item.child(PENDING).getValue(Boolean.class);
                Boolean booked = item.child(BOOKED).getValue(Boolean.class);

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
                        startTime= parseLegacyString((String) startObj);
                        endTime= parseLegacyString((String) endObj);
                    } else {
                        // From Daniil: new nested map format
                        startTime= readOffsetTime((HashMap) startObj);
                        endTime= readOffsetTime((HashMap) endObj);
                    }
                    String tutorFirst= item.child("tutorFirstName").getValue(String.class);
                    String tutorLast= item.child("tutorLastName").getValue(String.class);
                    String tutorUsername= item.child("tutorUsername").getValue(String.class);
                    if (tutorUsername==null || tutorUsername.isEmpty()) {
                        System.out.println("Warning: Missing tutor username for availability on " + date);
                        continue;
                    }
                    String studentFirst= item.child("studentFirstName").getValue(String.class);
                    String studentLast= item.child("studentLastName").getValue(String.class);
                    String studentUsername= item.child("studentUsername").getValue(String.class);
                    Availability avail= new Availability(autoApprove != null && autoApprove, startTime, endTime, date);
                    avail.setTutorCredentials(tutorFirst, tutorLast, tutorUsername);
                    if (studentFirst != null && studentLast != null) {
                        avail.setStudentCredentials(studentFirst, studentLast, studentUsername);
                    }
                    if (pending != null) {
                        avail.setPending(pending);
                    }
                    if (booked != null) {
                        avail.setBooked(booked);
                    }
                    list.add(avail);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        return list;
    }

    //From Daniil: parses old string format
//    private static OffsetTime parseLegacyString(String str) {
//        try {
//            String[] parts= str.split("-");
//            String[] hm= parts[0].split(":");
//            int hour= Integer.parseInt(hm[0]);
//            int minute= Integer.parseInt(hm[1]);
//            ZoneOffset offset= ZoneOffset.of(parts[1]);
//            return OffsetTime.of(hour, minute, 0, 0, offset);
//        } catch(Exception e) {
//            return OffsetTime.of(0,0,0,0, ZoneOffset.UTC);
//        }
//    }


//    public static OffsetTime readOffsetTime(HashMap map) {
//        if (map==null){
//            return OffsetTime.of(0,0,0,0, ZoneOffset.UTC);
//        }
//        Object h= map.get("hour");
//        Object m= map.get("minute");
//        int hours= h instanceof Number ? ((Number) h).intValue() : 0;
//        int minutes= m instanceof Number ? ((Number) m).intValue() : 0;
//        ZoneOffset offset= ZoneOffset.UTC;
//        Object offsetMap= map.get("offset");
//        if (offsetMap instanceof HashMap) {
//            Object idObj= ((HashMap) offsetMap).get("id");
//            if (idObj != null) {
//                try {
//                    offset= ZoneOffset.of(idObj.toString());
//                } catch(Exception ignored) {}
//            }
//        }
//        return OffsetTime.of(hours, minutes, 0, 0, offset);
//    }

    /**
     * Reads a hashmap and returns an Offset time
     *
     * @param map Hashmap provided by the database to read
     * @return An OffsetTime from that hashmap
     */
    // This is to be exclusively used when using the database
    public static OffsetTime readOffsetTime(HashMap map) {
        var hours = Integer.parseInt(String.valueOf((long) map.get("hour")));
        var minutes = Integer.parseInt(String.valueOf((long) map.get("minute")));

        // We do not support second-specific actions
        var seconds = 0;
        var nanos = 0;
        var id = ((HashMap) map.get("offset")).get("id").toString();

        ZoneOffset offset = ZoneOffset.of(id);
        return OffsetTime.of(hours, minutes, seconds, nanos, offset);
    }

    public static List<Availability> GenerateAvailabilityFromAllTutors(Course relevantCourse) {
        List<Availability> allAvailabilities = new ArrayList<>();
        //reads all accounts from database

        DataSnapshot allAccounts = Database.Database.Read(LoginManager.ACCOUNTS);

//        if (allAccounts == null) return allAvailabilities;
//        for (DataSnapshot userSnapshot : allAccounts.getChildren()) {
//            String username = userSnapshot.getKey();
//            //checks role to filter tutors
//            Object roleObj = userSnapshot.child(LoginManager.ROLE).getValue();
//            if (roleObj != null && roleObj.toString().equalsIgnoreCase("TUTOR")) {
//                List<Availability> tutorAvailabilities = GenerateAvailability(username);
//                allAvailabilities.addAll(tutorAvailabilities);
//            }
//        }

        if (allAccounts.exists()) {
            var children = allAccounts.getChildren();
            for(var snapshot : children) {
                var role = Account.Role.fromString(snapshot.child(LoginManager.ROLE).getValue().toString());
                var field = Field.fromString(snapshot.child(LoginManager.FIELD_OF_STUDY).getValue().toString());
                if(role == Account.Role.TUTOR && field == relevantCourse.getField())
                    allAvailabilities.addAll(GenerateAvailability(snapshot));
            }
        }
        return allAvailabilities;
    }
}

