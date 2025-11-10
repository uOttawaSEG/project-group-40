package com.uottawaseg.otams.Database;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Requests.RequestStatus;
import com.uottawaseg.otams.Requests.SessionRequest;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SessionRequestManager {
    private final static int DAY = 0;
    private final static int MONTH = 1;
    private final static int YEAR = 2;
    public final static String SESSIONS = "sessions";
    private final static String STUDENT = "student";
    private final static String TUTOR = "tutor";
    private final static String START_TIME = "startTime";
    private final static String END_TIME = "endTime";
    private final static String DATE = "date";
    private static SessionRequest _selected;
    public static List<SessionRequest> GenerateSessions(String username) {
        //public SessionRequest(String student, String tutor, OffsetDateTime startTime, OffsetDateTime endTime, OffsetDateTime date) {
        var data = Database.Database.Read(LoginManager.ACCOUNTS + "/" + username + "/" + SESSIONS);
        if (!data.exists()) {
            return new ArrayList<>();
        }
        var list = new ArrayList<SessionRequest>((int) data.getChildrenCount());
        var children = data.getChildren();
        for(var d : children) {

            var stud = (String) d.child(STUDENT).getValue();
            var tut = (String) d.child(TUTOR).getValue();
            var start = readOffsetTime((HashMap) d.child(START_TIME).getValue());
            var end = readOffsetTime((HashMap) d.child(END_TIME).getValue());
            var date = readOffsetDateTime((HashMap) d.child(DATE).getValue());

            list.add(new SessionRequest(stud, tut, start, end, date[DAY], date[MONTH], date[YEAR]));
        }
        return Collections.unmodifiableList(list);
    }
    public static SessionRequest GetSelected() {
        return _selected;
    }
    public static void Accept(SessionRequest s) {
        getTutor().AcceptSession(s);
        UpdateSessions(getTutor(), getTutor().getSessions());
    }

    public static void Decline(SessionRequest s) {
        getTutor().DeclineSession(s);
        UpdateSessions(getTutor(), getTutor().getSessions());
    }
    private static int[] readOffsetDateTime(HashMap map) {
        var day = Integer.valueOf(String.valueOf(map.get("dayOfMonth")));
        var month = Integer.valueOf(String.valueOf(map.get("monthValue")));
        var year = Integer.valueOf(String.valueOf(map.get("year")));
        return new int[] {day, month, year};
    }

    private static OffsetTime readOffsetTime(HashMap map) {
        return AvailabilityReader.readOffsetTime(map);
    }

    public static void UpdateSessions(Tutor tut, List<SessionRequest> sessions) {
        Database.Database.Write(
                LoginManager.ACCOUNTS + "/" + tut.getUsername() + "/" + SESSIONS,
                sessions);
    }

    public static boolean Select(String username, String date) {
        // We need to find a request that matches the date and the username.
        var sessions = GenerateSessions(LoginManager.getCurrentAccount().getUsername());
        // O N   2 0 2 5 - 1 1  -  1  0  T  0  0  :  0  0  Z
        //       Y E A R - M M  -  D  D                    ZONE NUM
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19
        var dateSubStr = date.substring(3);
        for(var s : sessions) {
            if(s.getStudent().equalsIgnoreCase(username) && dateSubStr.equalsIgnoreCase(s.getDate().toString())) {
                _selected = s;
                return true;
            }
        }
        return false;
    }
    public static List<SessionRequest> GeneratePendingRequests(String username) {
        var totalSes = GenerateSessions(username);
        var list = new ArrayList<SessionRequest>(totalSes.size());
        for(var s : totalSes)
            if(s.GetRequestStatus() == RequestStatus.PENDING)
                list.add(s);
        return Collections.unmodifiableList(list);
    }

    private static Tutor getTutor() { return (Tutor)LoginManager.getCurrentAccount(); }
}
