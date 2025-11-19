package your.package;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class AvailabilitySlot {

    private final LocalDateTime startTime;
    private final Duration duration;

    public AvailabilitySlot(LocalDateTime startTime, Duration duration) {
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    // Useful to allow List.remove(slotToRemove) to work predictably
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailabilitySlot)) return false;
        AvailabilitySlot that = (AvailabilitySlot) o;
        return Objects.equals(startTime, that.startTime) &&
               Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, duration);
    }
}

