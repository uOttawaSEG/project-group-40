package com.uottawaseg.otams.Database;

import com.google.firebase.database.DataSnapshot;
import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Requests.Availability;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AvailabilityReader {
    public static final String AVAILABILITIES = "availabilities";
    public static final String AUTOAPPROVE = "autoApprove";
    public static final String DAY = "day";
    public static final String START = "start";
    public static final String END = "end";

    /** Public wrapper function
     * @param tut The tutor account whose availabilities need to be read
     * @return an unmodifiable list of Availabilities from the account
     */
    public static List<Availability> GenerateAvailability(Tutor tut) {
        return GenerateAvailability(tut.getUsername());
    }

    /** Public wrapper function
     * @param username The username of the account whose availabilities need to be read
     * @return an unmodifiable list of Availabilities from the account
     */
    public static List<Availability> GenerateAvailability(String username) {
        var data = Database.Database.Read(LoginManager.ACCOUNTS + "/" + username + "/" + AVAILABILITIES);
        return GenerateAvailability(data);
    }

    /** Generates an Unmodifiable list of availabilities from a DataSnapshot
     * @param ds The DataSnapshot
     * @return UnmodifiableList<Availabilities> read from the DB
     */
    private static List<Availability> GenerateAvailability(DataSnapshot ds) {
        var children = ds.getChildren();
        var list = new ArrayList<Availability>();
        for(var item : children) {
            var autoApprove = item.child(AUTOAPPROVE).getValue(Boolean.class);
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
        return Collections.unmodifiableList(list);
    }

    /** Reads a hashmap and returns an Offset time
     * @param map Hashmap provided by the database to read
     * @return An OffsetTime from that hashmap
     */
    // This is to be exclusively used when using the database
    public static OffsetTime readOffsetTime(HashMap map) {
        var hours = Integer.parseInt(String.valueOf((long)map.get("hour")));
        var minutes = Integer.parseInt(String.valueOf((long)map.get("minute")));

        // We do not support second-specific actions
        var seconds = 0;
        var nanos = 0;
        var id = ((HashMap)map.get("offset")).get("id").toString();

        ZoneOffset offset = ZoneOffset.of(id);
        return OffsetTime.of(hours, minutes, seconds, nanos, offset);
    }
}
