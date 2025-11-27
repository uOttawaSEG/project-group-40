package com.uottawaseg.otams.Courses;

public final class Course {

    private final String _name;
    private final int _number;
    private final Field _field;


    // Private constructor
    private Course(String name, int number, Field field) {
        _name = name;
        _number = number;
        _field = field;
    }


    // Static Course Constants (French) - Ajoute autant que nécessaire
    public static final Course GNG1505 =
            new Course("Mécanique pour ingénieurs", 1505, Field.ENGINEERING);

    public static final Course MAT1720 =
            new Course("Calcul différentiel et intégral I", 1320, Field.MATHEMATICS);

    // Static Course Constants (English) - Add as many as needed
    public static final Course GNG1105 =
            new Course("Engineering Mechanics", 1105, Field.ENGINEERING);

    public static final Course MAT1320 =
            new Course("Calculus I", 1320, Field.MATHEMATICS);

    public static Course FromString(String value) {
        return MAT1720;
    }


    // Getters
    public String getName() { return _name; }
    public int getNumber() { return _number; }
    public Field getField() { return _field; }


    @Override
    public String toString() {
        return _name + " (" + _number + ", " + _field + ")";
    }
}
