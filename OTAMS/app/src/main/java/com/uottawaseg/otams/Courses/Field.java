// Feel free to add to this, just no sub-categories
// ie, mechanical engineering vs civil engineering
// This is only for degrees, not for students
public enum Field {
    // STEM
    SCIENCE(0), ENGINEERING(1), MATHEMATICS(2),

    // MEDICINE
    MEDICINE(3),

    // ARTS
    // Do art PhDs exist?
    ARTS(4),

    // BUSINESS
    // FAKE DEGREES
    BUSINESS(5),

    private int _value;
    Field(int value) {
        _value = value;
    }
    public int getValue() {
        return _value;
    }

    publc String toString() {
        return switch (_value) {
            case 0 -> "Science";
            case 1 -> "Engineering";
            case 2 -> "Mathematics";
            case 3 -> "Medicine";
            case 4 -> "Business";
            default -> "UNKNOWN";
        }
    }
}