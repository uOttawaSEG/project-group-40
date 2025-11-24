package com.uottawaseg.otams.Accounts;

import com.uottawaseg.otams.Requests.SessionRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student extends Account {
    private final String _studentNumber;
    private final Role _role = Role.STUDENT;

    private ArrayList<SessionRequest> _sessions;


    // Regular constructor (used when creating a new student)
    public Student(String firstName, String lastName, String username, String password,
                   String phoneNumber, String email, String studentNumber) {

        super(firstName, lastName, username, password, phoneNumber, email);

        this._studentNumber = studentNumber;
        this._sessions = new ArrayList<>();   // new students start with zero session requests
    }

    // Constructor used when loading from DB (with sessions)
    public Student(String firstName, String lastName, String username, String password,
                   String phoneNumber, String email, String studentNumber,
                   List<SessionRequest> sessions) {

        super(firstName, lastName, username, password, phoneNumber, email);

        this._studentNumber = studentNumber;

        if (sessions == null)
            this._sessions = new ArrayList<>();
        else
            this._sessions = new ArrayList<>(sessions);
    }


    // Getters
    public String getStudentNumber() {
        return _studentNumber;
    }

    public Role getRole() {
        return _role;
    }

    public List<SessionRequest> getSessions() {
        return Collections.unmodifiableList(_sessions);
    }


    @Override
    public String toString() {
        var s = super.toString();
        s += "Student Number: " + _studentNumber;
        return s;
    }

}
