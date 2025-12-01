package com.uottawaseg.otams.Database;

import com.google.firebase.database.DataSnapshot;
import com.uottawaseg.otams.Accounts.Account;
import com.uottawaseg.otams.Accounts.Student;
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
    public static final String TUTOR = "tutor";

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
            var tut = item.child(TUTOR).getValue(String.class);

            list.add(new Availability(
                    autoApprove,
                    readOffsetTime((HashMap) start),
                    readOffsetTime((HashMap) end),
                    DayOfWeek.valueOf(day),
                    tut
            ));
        }
        return list;
    }

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
        var sessions = ((Student)(LoginManager.getCurrentAccount())).getSessions();
        List<Availability> allAvailabilities = new ArrayList<>();
        DataSnapshot allAccounts = Database.Database.Read(LoginManager.ACCOUNTS);
        if (allAccounts.exists()) {
            var children = allAccounts.getChildren();
            for(var snapshot : children) {
                var role = Account.Role.fromString(snapshot.child(LoginManager.ROLE).getValue().toString());
                if(role == Account.Role.TUTOR) {
                    var field = Field.fromString(snapshot.child(LoginManager.FIELD_OF_STUDY).getValue().toString());
                    if(field == relevantCourse.getField())
                    {
                        var avails = GenerateAvailability(snapshot.child(AVAILABILITIES));
                        if(sessions.size() == 0) {
                            allAvailabilities.addAll(avails);
                        } else {
                            for(var a : avails) {
                                for(var s : sessions) {
                                    if(a.getDay() != s.getDate().getDayOfWeek())
                                        allAvailabilities.add(a);
                                    else if(!StudentSessionManager.DoTimeSlotsOverlap(s.getStartTime(), s.getEndTime(), a.getStart()))
                                        allAvailabilities.add(a);
                                }
                            }
                        }
                    }
                }
            }
        }
        return allAvailabilities;
    }
}

