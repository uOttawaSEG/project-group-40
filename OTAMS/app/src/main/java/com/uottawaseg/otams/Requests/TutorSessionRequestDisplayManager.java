package com.uottawaseg.otams.Requests;

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
    public static String[] GetPendingRequests(List<SessionRequest> sessions) {
        var arr = new String[sessions.size()];
        int i = 0;
        for(var sess : sessions) {
            if(sess.GetRequestStatus() == RequestStatus.PENDING)
                arr[i++] = "Session with:\n" + sess.getStudent() + "\nOn " + sess.getDate();
        }
        return arr;
    }

    /**
     * @param s The session request that needs to be displayed
     * @return A string[] with various fields for the actual display.
     */
    public static String[] Display(SessionRequest s) {
        var sb = new StringBuilder();
        sb.append("Session with:\n")
                .append(s.getStudent())
                .append("\nOn: ").append(s.getDate())
                .append("\nStarts at: ").append(s.getStartTime())
                .append("\nEnd at: ").append(s.getEndTime());
        return sb.toString().split("\n");
    }
}
