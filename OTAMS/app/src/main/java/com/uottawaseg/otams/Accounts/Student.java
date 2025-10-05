package com.uottawaseg.otams.Account;

import System;

public class Student extends Account {
    private String _studentNumber;

    // We'll make a Course enum with the courses.
    // Let's not worry about it now
    //private String[] course_selection= {"CHM1311","PHY1124"."GNG1105.GNG1103,MAT1321,MAT1320,MAT1348,"};
    // We can then use flags for this
    //private String course_preference;

    public Student(String firstName, String lastName, String username, String password,
                   String phoneNumber, String email, String studentNumber) {
        super(username, password, phoneNumber, email);
        _studentNumber = studentNumber;

    }

    public String getStudentNumber() {
        return _studentNumber;
    }

}
