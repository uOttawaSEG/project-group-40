package your.package;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentSessionManager {

    private final Database database;

    public StudentSessionManager(Database database) {
        this.database = database;
    }

    // GetSessions()
    public List<SessionRequest> GetSessions(Student s) {
        return Collections.unmodifiableList(s.getSessions());
    }

    // PrepareForDisplay(SessionRequest s) -> [time, tutor name, course code, course name]
    public String[] PrepareForDisplay(SessionRequest s) {
        String time = s.getStartTime().toString(); // adjust formatting if needed
        String tutorName = (s.getTutor() != null)
                ? (s.getTutor().getUsername() != null
                    ? s.getTutor().getUsername()
                    : s.getTutor().getFirstName())
                : "Unknown tutor";

        String courseCode = s.getCourse() != null ? s.getCourse().getCode() : "N/A";
        String courseName = s.getCourse() != null ? s.getCourse().getName() : "N/A";

        return new String[] {
                time,
                tutorName,
                courseCode,
                courseName
        };
    }

    // UpdateSessions(Student s, List<SessionRequest> sessions)
    public void UpdateSessions(Student s, List<SessionRequest> sessions) {
        if (sessions == null) {
            return;
        }
        for (SessionRequest session : sessions) {
            database.updateSession(session);
            s.addOrUpdateSession(session);
        }
    }

    // DeclineSession(Student s, SessionRequest sess)
    public void DeclineSession(Student s, SessionRequest sess) {
        if (sess == null) return;

        // Determine who declined: simple version assumes if student matches, it's student
        if (sess.getStudent() != null && sess.getStudent().getId() == s.getId()) {
            sess.setStatus(SessionStatus.DECLINED_BY_STUDENT);
        } else {
            // fallback – but usually you handle tutor-side decline from SessionRequestManager
            sess.setStatus(SessionStatus.DECLINED_BY_TUTOR);
        }

        database.updateSession(sess);
        s.addOrUpdateSession(sess);
    }

    // CancelSession(Student s, SessionRequest sess)
    public void CancelSession(Student s, SessionRequest sess) {
        if (sess == null) return;

        if (sess.getStudent() != null && sess.getStudent().getId() == s.getId()) {
            sess.setStatus(SessionStatus.CANCELLED_BY_STUDENT);
        } else {
            sess.setStatus(SessionStatus.CANCELLED_BY_TUTOR);
        }

        database.updateSession(sess);
        s.addOrUpdateSession(sess);
    }

    // ViewPastSessions(Student s)
    public List<SessionRequest> ViewPastSessions(Student s) {
        LocalDateTime now = LocalDateTime.now();
        List<SessionRequest> past = new ArrayList<>();

        for (SessionRequest sess : s.getSessions()) {
            if (!sess.getEndTime().isAfter(now)) { // end <= now
                past.add(sess);
            }
        }

        return Collections.unmodifiableList(past);
    }

    // ViewUpcomingSessions(Student s)
    public List<SessionRequest> ViewUpcomingSessions(Student s) {
        LocalDateTime now = LocalDateTime.now();
        List<SessionRequest> upcoming = new ArrayList<>();

        for (SessionRequest sess : s.getSessions()) {
            if (sess.getStartTime().isAfter(now)) {
                upcoming.add(sess);
            }
        }

        return Collections.unmodifiableList(upcoming);
    }

    // PrepareSessionsForDisplay(Student s)
    // One string per session, each string: time\nTutorName\nCourseCode\nCourseName
    public String[] PrepareSessionsForDisplay(Student s) {
        List<SessionRequest> sorted = new ArrayList<>(s.getSessions());
        sorted.sort(Comparator.comparing(SessionRequest::getStartTime));

        String[] result = new String[sorted.size()];
        for (int i = 0; i < sorted.size(); i++) {
            SessionRequest sess = sorted.get(i);
            String tutorName = (sess.getTutor() != null)
                    ? (sess.getTutor().getUsername() != null
                        ? sess.getTutor().getUsername()
                        : sess.getTutor().getFirstName())
                    : "Unknown tutor";

            String courseCode = sess.getCourse() != null ? sess.getCourse().getCode() : "N/A";
            String courseName = sess.getCourse() != null ? sess.getCourse().getName() : "N/A";

            String value =
                    sess.getStartTime() + "\n" +
                    tutorName + "\n" +
                    courseCode + "\n" +
                    courseName;

            result[i] = value;
        }
        return result;
    }
}

