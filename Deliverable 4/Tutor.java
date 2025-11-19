package your.package;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tutor {

    private final int id;
    private final String username;
    private final String firstName;

    private final List<SessionRequest> sessions = new ArrayList<>();
    private final List<AvailabilitySlot> availability = new ArrayList<>();

    public Tutor(int id, String username, String firstName) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public List<SessionRequest> getSessions() {
        return Collections.unmodifiableList(sessions);
    }

    public void addOrUpdateSession(SessionRequest session) {
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getId() == session.getId()) {
                sessions.set(i, session);
                return;
            }
        }
        sessions.add(session);
    }

    public List<AvailabilitySlot> getAvailability() {
        return Collections.unmodifiableList(availability);
    }

    public void addAvailability(AvailabilitySlot slot) {
        availability.add(slot);
    }

    /**
     * Remove availability only if there are no sessions booked
     * overlapping that time slot.
     *
     * @return true if removed, false if blocked.
     */
    public boolean RemoveAvailability(AvailabilitySlot slotToRemove) {
        boolean hasBookedSessions = sessions.stream().anyMatch(s ->
                (s.getStatus() == SessionStatus.ACCEPTED ||
                 s.getStatus() == SessionStatus.PENDING)
                && sessionsOverlap(
                        s.getStartTime(),
                        s.getDuration(),
                        slotToRemove.getStartTime(),
                        slotToRemove.getDuration())
        );

        if (hasBookedSessions) {
            return false;
        }

        return availability.remove(slotToRemove);
    }

    private boolean sessionsOverlap(LocalDateTime s1Start,
                                    Duration s1Duration,
                                    LocalDateTime s2Start,
                                    Duration s2Duration) {
        LocalDateTime s1End = s1Start.plus(s1Duration);
        LocalDateTime s2End = s2Start.plus(s2Duration);
        return s1Start.isBefore(s2End) && s2Start.isBefore(s1End);
    }
}

