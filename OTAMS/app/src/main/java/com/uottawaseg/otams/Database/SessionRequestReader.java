package com.uottawaseg.otams.Database;

import com.uottawaseg.otams.Requests.SessionRequest;

import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SessionRequestReader {
    public final static String SESSIONS = "sessions";
    private final static String STUDENT = "student";
    private final static String TUTOR = "tutor";
    private final static String START_TIME = "startTime";
    private final static String END_TIME = "endTime";
    private final static String DAY = "day";
    private final static String MONTH = "month";
    private final static String YEAR = "year";
    public static List<SessionRequest> GenerateSessions(String username) {
        //public SessionRequest(String student, String tutor, OffsetDateTime startTime, OffsetDateTime endTime, OffsetDateTime date) {
        var data = Database.Database.Read(AccountCreationManager.GetAccountLocation() + "/" + username + "/" + SESSIONS);
        if (!data.exists()) {
            return new ArrayList<SessionRequest>();
        }
        var list = new ArrayList<SessionRequest>((int) data.getChildrenCount());
        var children = data.getChildren();
        for(var d : children) {

            var stud = (String) data.child(STUDENT).getValue();
            var tut = (String) data.child(TUTOR).getValue();
            var start = readOffsetTime((HashMap) data.child(START_TIME).getValue());
            var end = readOffsetTime((HashMap) data.child(END_TIME).getValue());
            var day = (int) data.child(DAY).getValue();
            var month = (int) data.child(MONTH).getValue();
            var year = (int) data.child(YEAR).getValue();

            list.add(new SessionRequest(stud, tut, start, end, day, month, year));
        }
        return Collections.unmodifiableList(list);
    }

    private static OffsetTime readOffsetTime(HashMap map) {
        return AvailabilityReader.readOffsetTime(map);
    }

}
