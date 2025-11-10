package com.uottawaseg.otams.Requests;

import java.util.List;

// Long ass name
public class TutorSessionRequestDisplayManager {

    public static String[] GetPendingRequests(List<SessionRequest> sessions) {
        var arr = new String[sessions.size()];
        int i = 0;
        for(var sess : sessions) {
            arr[i++] = "Session with:\n" + sess.getStudent() + "\nOn " + sess.getDate();
        }
        return arr;
    }
}
