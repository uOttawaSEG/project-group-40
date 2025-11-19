package your.package;

import java.time.Duration;
import java.time.LocalDateTime;

public class SessionRequestManager {

    private final Database database;
    private final StudentSessionManager studentSessionManager;
    private final TutorSessionManager tutorSessionManager;

    private static final Duration TUTOR_CANCEL_MIN_NOTICE = Duration.ofHours(24);

    public SessionRequestManager(Database database,
                                 StudentSessionManager studentSessionManager,
                                 TutorSessionManager tutorSessionManager) {
        this.database = database;
        this.studentSessionManager = studentSessionManager;
        this.tutorSessionManager = tutorSessionManager;
    }

    // Decline as tutor
    public void DeclineSessionAsTutor(Tutor tutor, Student student, SessionRequest sess) {
        if (sess.getTutor() == null || sess.getTutor().getId() != tutor.getId()) {
            throw new IllegalArgumentException("Tutor is not owner of this session.");
        }

        sess.setStatus(SessionStatus.DECLINED_BY_TUTOR);
        updateBothSides(student, tutor, sess);
    }

    // Decline as student
    public void DeclineSessionAsStudent(Student student, Tutor tutor, SessionRequest sess) {
        if (sess.getStudent() == null || sess.getStudent().getId() != student.getId()) {
            throw new IllegalArgumentException("Student is not owner of this session.");
        }

        sess.setStatus(SessionStatus.DECLINED_BY_STUDENT);
        updateBothSides(student, tutor, sess);
    }

    // Cancel as tutor (must be >= 24h before)
    public void CancelSessionAsTutor(Tutor tutor, Student student, SessionRequest sess) {
        if (sess.getTutor() == null || sess.getTutor().getId() != tutor.getId()) {
            throw new IllegalArgumentException("Tutor is not owner of this session.");
        }

        LocalDateTime now = LocalDateTime.now();
        Duration untilStart = Duration.between(now, sess.getStartTime());

        if (untilStart.compareTo(TUTOR_CANCEL_MIN_NOTICE) < 0) {
            throw new IllegalStateException("Tutor cannot cancel within 24 hours of the session.");
        }

        sess.setStatus(SessionStatus.CANCELLED_BY_TUTOR);
        updateBothSides(student, tutor, sess);
    }

    // Cancel as student (any time)
    public void CancelSessionAsStudent(Student student, Tutor tutor, SessionRequest sess) {
        if (sess.getStudent() == null || sess.getStudent().getId() != student.getId()) {
            throw new IllegalArgumentException("Student is not owner of this session.");
        }

        sess.setStatus(SessionStatus.CANCELLED_BY_STUDENT);
        updateBothSides(student, tutor, sess);
    }

    private void updateBothSides(Student student, Tutor tutor, SessionRequest sess) {
        database.updateSession(sess);

        student.addOrUpdateSession(sess);
        tutor.addOrUpdateSession(sess);
    }
}

