package your.package;

public final class Course {

    public enum FieldOfStudy {
        GNG,
        CSI,
        MAT,
        PHY,
        CHM
        // etc…
    }

    private final String name;
    private final int number;
    private final FieldOfStudy fieldOfStudy;

    private Course(String name, int number, FieldOfStudy fieldOfStudy) {
        this.name = name;
        this.number = number;
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public FieldOfStudy getFieldOfStudy() {
        return fieldOfStudy;
    }

    public String getCode() {
        return fieldOfStudy.name() + number;
    }

    @Override
    public String toString() {
        // Example: "GNG1505 - Introduction to Engineering"
        return getCode() + " - " + name;
    }

    // “const” courses
    public static final Course GNG1505 =
            new Course("Introduction to Engineering", 1505, FieldOfStudy.GNG);

    public static final Course CSI2110 =
            new Course("Data Structures and Algorithms", 2110, FieldOfStudy.CSI);

    // Add more as needed…
}
