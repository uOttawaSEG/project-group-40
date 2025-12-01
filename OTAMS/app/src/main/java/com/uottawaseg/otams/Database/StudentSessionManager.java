package com.uottawaseg.otams.Database;

import com.uottawaseg.otams.Accounts.Student;
import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Courses.Course;
import com.uottawaseg.otams.Requests.Availability;
import com.uottawaseg.otams.Requests.SessionRequest;
import com.uottawaseg.otams.Requests.RequestStatus;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class StudentSessionManager {
    private static final String SESSIONS = "sessions";

    /**
     * Returns an ImmutableList of SessionRequest for the given student.
     */
    public static List<SessionRequest> GetSessions(Student s) {
        if (s == null) return Collections.unmodifiableList( new ArrayList<>() );
        return SessionRequestManager.GenerateSessions(s.getUsername());
    }

    /**
     * Prepare a single session for display. Returns a String[] with lines of information.
     */
    public static String PrepareForDisplay(SessionRequest s) {
        if (s == null) return "";
        return "Date: " + s.getDate().toLocalDate() + "\n" +
                "Start: " + s.getStartTime() + "\n" +
                "End: " + s.getEndTime() + "\n" +
                "Tutor: " + s.getTutor() + "\n" +
                "Course: " + s.getCourse();
    }

    /**
     * Update the sessions for this student in the DB.
     */
    public static void UpdateSessions(Student s, List<SessionRequest> sessions) {
        if (s == null) return;
        if (sessions == null) {
            // Write empty list if caller passed null
            sessions = new ArrayList<>();
        }
        Database.Database.Write(LoginManager.ACCOUNTS + "/" + s.getUsername() + "/" + SESSIONS, sessions);
    }

    /**
     * The tutor may decline a session for a student.
     */
    public static void DeclineSession(SessionRequest sess) {
        var s = Database.Database.Read(LoginManager.ACCOUNTS + "/" + sess.getStudent());
        if (s == null || !s.exists()) return;

        var student = (Student) LoginManager.makeAccountFromQuery(s);
        // Update student sessions
        var studentSessions = new ArrayList<>(GetSessions(student));
        for (var sr : studentSessions) {
            if (sr.equals(sess)) {
                sr.setStatus(RequestStatus.DENIED);
                UpdateSessions(student, studentSessions);
                return;
            }
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
    public static List<SessionRequest> ViewPastSessions(Student s) {
        var now = OffsetDateTime.now();
        var sessions = GetSessions(s);
        var out = new ArrayList<SessionRequest>();
        for (var req : sessions) {
            if (req.getDate().isBefore(now)) out.add(req);
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * Returns sessions strictly in the future (based on session.getDate()).
     */
    public static List<SessionRequest> ViewUpcomingSessions(Student s) {
        var now = OffsetDateTime.now();
        var sessions = GetSessions(s);
        var out = new ArrayList<SessionRequest>();
        for (var req : sessions) {
            if (req.getDate().isAfter(now)) out.add(req);
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * Prepare all sessions for preview screen.
     * Returns a String[] where each element is the info of a single session, separated by newlines.
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

    public static boolean RequestSession(OffsetTime start, OffsetTime end, Availability tutAvail, OffsetDateTime day, Course c) {
        var stud = (Student) LoginManager.getCurrentAccount();

        var tutorUsername = tutAvail.getTutor();
        var request = new SessionRequest(LoginManager.getCurrentAccount().getUsername(), tutorUsername,
                start, end, day.getDayOfMonth(), day.getMonth().getValue(), day.getYear(), c
        );
        var tutor = (Tutor) LoginManager.makeAccountFromQuery(
                Database.Database.Read(LoginManager.ACCOUNTS + "/" + tutorUsername)
        );
        if (InvalidSessionRequest(stud, request)) return false;

        SessionRequestManager.RequestSession(request, tutor);
        stud.AddRequest(request);
        return true;
    }

    private static boolean InvalidSessionRequest(Student stud, SessionRequest request) {
        var sessions = stud.getSessions();
        for(var s : sessions) {
            if(s.getDate().isEqual(request.getDate())) {
                if(DoTimeSlotsOverlap(s.getStartTime(), s.getEndTime(), request.getStartTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean DoTimeSlotsOverlap(OffsetTime existingStart, OffsetTime existingEnd,
                                             OffsetTime newStart) {
        // existingStart <= newStart <= existingEnd
        // Then we have an overlap
        // Thus, newStart < existingStart or newStart > existingEnd
        // To simplify we know that
        // a <= b is logically equivalent to !( a > b )
        // so we can transform it into
        // !(existingStart > newStart) && !( newStart > existingEnd)
        if(!existingStart.isAfter(newStart) && !newStart.isAfter(existingEnd)) {
            return true;
        }
        // We know that our start is before or after the existing slot
        // Thus, we only need to check if the end is before the start of the next session,
        // Because if it is we're good, otherwise we have an overlap
        // basically
        //      has been checked for already
        // \/                             \/        \/
        // existingStart < existingEnd < newStart < newEnd
        // OR
        //    has been checked for already
        // \/            \/         \/
        // newStart < newEnd < existing start < existing end
        // We know that existingStart < existing end, it's a requirement
        // So we can simplify our conditions to just
        // existingEnd < newStart < newEnd
        // Simplify again
        // existingEnd < newStart
        if(existingEnd.isAfter(newStart)) {
            return true;
        }
        return false;
    }
}
