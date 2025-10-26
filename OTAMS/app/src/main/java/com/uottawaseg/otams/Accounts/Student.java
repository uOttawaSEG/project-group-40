
package com.uottawaseg.otams.Accounts;

import java.util.HashMap;

public class Student extends Account {
    private final String _studentNumber;
    private final Role _role = Role.STUDENT;

    public Student(String firstName, String lastName, String username, String password,
                   String phoneNumber, String email, String studentNumber) {
        super(firstName, lastName, username, password, phoneNumber, email);
        _studentNumber = studentNumber;

    }

    public String getStudentNumber() {
        return _studentNumber;
    }
    public Role getRole() {
        return _role;
    }

}
