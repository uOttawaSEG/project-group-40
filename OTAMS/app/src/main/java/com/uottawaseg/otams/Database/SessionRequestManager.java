package com.uottawaseg.otams.Database;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Requests.RequestStatus;
import com.uottawaseg.otams.Requests.SessionRequest;

import java.time.OffsetTime;
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

    /**
     * @param username Username of the tutor account
     * @return An UnmodifiableList of SessionRequests if any are found,
     * otherwise empty ArrayList<SessionRequest> if nothing was found
     */
    public static List<SessionRequest> GenerateSessions(String username) {
        var data = Database.Database.Read(LoginManager.ACCOUNTS + "/" + username + "/" + SESSIONS);
        if (!data.exists()) {
            Database.Database.Write(LoginManager.ACCOUNTS + "/" + username + "/" + SESSIONS, new ArrayList<>());
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

    /**
     * @param s The session to accept
     */
    public static void Accept(SessionRequest s) {
        System.out.println(getTutor().getSessions());
        getTutor().AcceptSession(s);
    }

    /**
     * @param s The session to decline
     */
    public static void Decline(SessionRequest s) {
        getTutor().DeclineSession(s);
    }

    /**
     * @param map A hashmap from the database to read
     * @return An int[] of {day, month, year}
     */
    private static int[] readOffsetDateTime(HashMap map) {
        var day = Integer.valueOf(String.valueOf(map.get("dayOfMonth")));
        var month = Integer.valueOf(String.valueOf(map.get("monthValue")));
        var year = Integer.valueOf(String.valueOf(map.get("year")));
        return new int[] {day, month, year};
    }

    /** Just a wrapper function
     * @param map A hash map from the database to read
     * @return OffsetTime acquired from said hashmap
     */
    private static OffsetTime readOffsetTime(HashMap map) {
        return AvailabilityReader.readOffsetTime(map);
    }

    /**
     * @param tut The tutor whose sessions should be updated
     */
    public static void UpdateSessions(Tutor tut) {
        Database.Database.Write(
                LoginManager.ACCOUNTS + "/" + tut.getUsername() + "/" + SESSIONS,
                tut.getSessions());
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

    /** Filters requests and returns a list of the pending requests
     * @param username The tutors username
     * @return A list of pending requests.
     */
    public static List<SessionRequest> GeneratePendingRequests(String username) {
        var totalSes = GenerateSessions(username);
        var list = new ArrayList<SessionRequest>(totalSes.size());
        for(var s : totalSes)
            if(s.GetRequestStatus() == RequestStatus.PENDING)
                list.add(s);
        return Collections.unmodifiableList(list);
    }

    /** Acquires the account from the login manager and casts it to tutor.
     * @return The tutor from LoginManager
     */
    private static Tutor getTutor() { return (Tutor)LoginManager.getCurrentAccount(); }
}
