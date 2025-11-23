package com.uottawaseg.otams.Requests;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

// Long ass name
public class TutorSessionRequestDisplayManager {
    public static final int INTRO = 0;
    public static final int NAME = 1;
    public static final int DATE = 2;
    public static final int STARTS_AT = 3;
    public static final int ENDS_AT = 4;

    /**
     * @param sessions The sessions to make a list out of
     * @return A String[] to populate a recyclerview
     */
    public static String[] GetRequests(List<SessionRequest> sessions) {
        var arr = new String[sessions.size()];
        int i = 0;
        var now = OffsetDateTime.now();
        for (var sess : sessions) {
            if(!isInPast(sess, now))
                arr[i++] = "Session with:\n" + sess.getStudent() + "\nOn " + sess.getDate();
        }

        return arr;
    }

    /**
     * @param s The session request that needs to be displayed
     * @return A string[] with various fields for the actual display.
     */
    public static String[] Display(SessionRequest s) {
        String sb = "Session with:\n" +
                s.getStudent() +
                "\nOn: " + s.getDate() +
                "\nStarts at: " + s.getStartTime() +
                "\nEnd at: " + s.getEndTime();
        return sb.split("\n");
    }

    public static String[] GetPastSessions(List<SessionRequest> sessionRequests) {
        var temp = new ArrayList<SessionRequest>(sessionRequests.size());
        var rightNow = OffsetDateTime.now();
        for (var s : sessionRequests) {
            if(!s.getStatus().equals(RequestStatus.ACCEPTED) || isInPast(s, rightNow)) continue;
            temp.add(s);
        }
        return GetRequests(temp);
    }

    private static boolean isInPast(SessionRequest s, OffsetDateTime now) {
        var sessDate = s.getDate();
        var sessEnd = s.getEndTime();
        // If the date is in the past, we add it
        return sessDate.isBefore(now) ||
                (sessDate.isEqual(now) && sessEnd.compareTo(now.toOffsetTime()) < 1);
    }

    public static String[] GetUpcomingRequests(List<SessionRequest> sessionRequests) {
        var temp = new ArrayList<SessionRequest>(sessionRequests.size());
        var rightNow = OffsetDateTime.now();
        for (var s : sessionRequests) {
            if (s.getStatus().equals(RequestStatus.ACCEPTED)) {
                var sessDate = s.getDate();
                var sessEnd = s.getEndTime();
                // If the date is in the future, we add it
                if (sessDate.isAfter(rightNow))
                    temp.add(s);
                    // If it's today but the end time is in the future, we add it
                else if (sessDate.isEqual(rightNow) && sessEnd.isAfter(rightNow.toOffsetTime()))
                    temp.add(s);
            }
        }
        return GetRequests(temp);
    }
}
