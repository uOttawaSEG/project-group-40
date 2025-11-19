package your.package;

import java.time.Duration;
import java.time.LocalDateTime;

public class SessionRequest {

    private int id;
    private Student student;
    private Tutor tutor;
    private LocalDateTime startTime;
    private Duration duration;
    private Course course;
    private SessionStatus status;

    public SessionRequest(int id,
                          Student student,
                          Tutor tutor,
                          LocalDateTime startTime,
                          Duration duration,
                          Course course,
                          SessionStatus status) {
        this.id = id;
        this.student = student;
        this.tutor = tutor;
        this.startTime = startTime;
        this.duration = duration;
        this.course = course;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public Course getCourse() {
        return course;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }
}
