// Minimal stub so the snippet compiles. Replace with your real Student type.
package com.example.tutoring;


public class Student {
private final String id;
public Student(String id) { this.id = id; }
@Override public String toString() { return "Student{" + id + '}'; }
}


// ==========================================
// filename: README.txt (notes)
// ==========================================
// - Availability enforces a >= 30 minute window and end-after-start.
// - SessionRequest is immutable and exposes getters for student, tutor, start, end, date.
// - Tutor initializes availability/request lists to empty (never null) and provides an overload
// that accepts pre-fetched lists (e.g., when materializing from a database). Methods update DB
// through a stubbed updateAvailabilityInDb().
// - Replace the placeholder Request/Student with your project’s actual types if they already exist.
