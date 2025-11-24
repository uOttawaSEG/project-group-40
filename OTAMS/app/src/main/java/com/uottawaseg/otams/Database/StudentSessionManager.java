package com.uottawaseg.otams.Database;

import com.google.common.collect.ImmutableList;
import com.uottawaseg.otams.Accounts.Student;
import com.uottawaseg.otams.Requests.SessionRequest;
import com.uottawaseg.otams.Requests.RequestStatus;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public final class StudentSessionManager {
    private static final String SESSIONS = "sessions";

    /**
     * Returns an ImmutableList of SessionRequest for the given student.
     */
    public static ImmutableList<SessionRequest> GetSessions(Student s) {
        if (s == null) return ImmutableList.of();
        var list = SessionRequestManager.GenerateSessions(s.getUsername());
        // GenerateSessions already returns an unmodifiable List, ImmutableList.copyOf will convert it.
        return ImmutableList.copyOf(list);
    }

    /**
     * Prepare a single session for display. Returns a String[] with lines of information.
     */
    public static String[] PrepareForDisplay(SessionRequest s) {
        if (s == null) return new String[0];
        return new String[]{
                "Date: " + s.getDate().toLocalDate(),
                "Start: " + s.getStartTime(),
                "End: " + s.getEndTime(),
                "Tutor: " + s.getTutor(),
                "Course: " + s.getCourse()
        };
    }

    /**
     * Update the sessions for this student in the DB.
     */
    public static void UpdateSessions(Student s, List<SessionRequest> sessions) {
        if (s == null) return;
        if (sessions == null) {
            // Write empty list if caller passed null
            Database.Database.Write(LoginManager.ACCOUNTS + "/" + s.getUsername() + "/" + SESSIONS, new ArrayList<>());
        } else {
            Database.Database.Write(LoginManager.ACCOUNTS + "/" + s.getUsername() + "/" + SESSIONS, sessions);
        }
    }

    /**
     * The tutor may decline a session for a student.
     */
    public static void DeclineSession(Student s, SessionRequest sess) {
        if (s == null || sess == null) return;

        // Update student sessions
        var studentSessions = new ArrayList<>(GetSessions(s));
        for (var sr : studentSessions) {
            if (sr.equals(sess)) {
                sr.setStatus(RequestStatus.DENIED);
                break;
            }
        }
        UpdateSessions(s, studentSessions);

        // Update tutor sessions
        var tutorUsername = sess.getTutor();
        if (tutorUsername != null) {
            var tutorSessions = new ArrayList<>(SessionRequestManager.GenerateSessions(tutorUsername));
            for (var ts : tutorSessions) {
                if (ts.equals(sess)) {
                    ts.setStatus(RequestStatus.DENIED);
                    break;
                }
            }
            Database.Database.Write(LoginManager.ACCOUNTS + "/" + tutorUsername + "/" + SESSIONS, tutorSessions);
        }
    }

    /**
     * Cancel a session initiated by either the student or the tutor.
     * We set the status to DENIED (not deleted) and update both student and tutor DB entries.
     */
    public static void CancelSession(Student s, SessionRequest sess) {
        if (s == null || sess == null) return;

        // Update student sessions
        var studentSessions = new ArrayList<>(GetSessions(s));
        for (var sr : studentSessions) {
            if (sr.equals(sess)) {
                sr.setStatus(RequestStatus.DENIED);
                break;
            }
        }
        UpdateSessions(s, studentSessions);

        // Update tutor sessions
        var tutorUsername = sess.getTutor();
        if (tutorUsername != null) {
            var tutorSessions = new ArrayList<>(SessionRequestManager.GenerateSessions(tutorUsername));
            for (var ts : tutorSessions) {
                if (ts.equals(sess)) {
                    ts.setStatus(RequestStatus.DENIED);
                    break;
                }
            }
            Database.Database.Write(LoginManager.ACCOUNTS + "/" + tutorUsername + "/" + SESSIONS, tutorSessions);
        }
    }

    /**
     * Returns sessions strictly in the past (based on session.getDate()).
     */
    public static ImmutableList<SessionRequest> ViewPastSessions(Student s) {
        var now = OffsetDateTime.now();
        var sessions = GetSessions(s);
        var out = new ArrayList<SessionRequest>();
        for (var req : sessions) {
            if (req.getDate().isBefore(now)) out.add(req);
        }
        return ImmutableList.copyOf(out);
    }

    /**
     * Returns sessions strictly in the future (based on session.getDate()).
     */
    public static ImmutableList<SessionRequest> ViewUpcomingSessions(Student s) {
        var now = OffsetDateTime.now();
        var sessions = GetSessions(s);
        var out = new ArrayList<SessionRequest>();
        for (var req : sessions) {
            if (req.getDate().isAfter(now)) out.add(req);
        }
        return ImmutableList.copyOf(out);
    }

    /**
     * Prepare all sessions for preview screen.
     * Returns a String[] where each element is one session's info separated by newlines.
     */
    public static String[] PrepareSessionsForDisplay(Student s) {
        var sessions = GetSessions(s);
        var output = new String[sessions.size()];
        int i = 0;
        for (var req : sessions) {
            output[i++] = req.getDate().toString()
                    + "\n" + req.getTutor()
                    + "\n" + req.getCourse();
        }
        return output;
    }
}
