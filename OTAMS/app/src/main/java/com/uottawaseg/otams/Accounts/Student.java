package com.uottawaseg.otams.Accounts;

import com.uottawaseg.otams.Database.Database;
import com.uottawaseg.otams.Database.LoginManager;
import com.uottawaseg.otams.Database.SessionRequestManager;
import com.uottawaseg.otams.Database.StudentSessionManager;
import com.uottawaseg.otams.Requests.RequestStatus;
import com.uottawaseg.otams.Requests.SessionRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Student extends Account {
    private final String _studentNumber;
    private final Role _role = Role.STUDENT;

    private ArrayList<SessionRequest> _sessions;


    public Student(String firstName, String lastName, String username, String password,
                   String phoneNumber, String email, String studentNumber) {
        this(firstName, lastName, username, password, phoneNumber, email, studentNumber, null);
    }

    public Student(String firstName, String lastName, String username, String password,
                   String phoneNumber, String email, String studentNumber,
                   List<SessionRequest> sessions) {

        super(firstName, lastName, username, password, phoneNumber, email);

        _studentNumber = studentNumber;

        if (sessions == null)
            _sessions = new ArrayList<>();
        else
            _sessions = new ArrayList<>(sessions);
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

    public void AddRequest(SessionRequest request) {
        _sessions.add(request);
        SessionRequestManager.UpdateSessions(this);
    }

    public void AcceptSession(SessionRequest s) {
        for(var sess : getSessions()) {
            if(sess.equals(s)) {
                sess.setStatus(RequestStatus.ACCEPTED);
                StudentSessionManager.UpdateSessions(this, getSessions());
                return;
            }
        }
    }

    public void RemoveSession(int position) {
        var sess = _sessions.get(position);
        _sessions.remove(position);
        StudentSessionManager.UpdateSessions(this, getSessions());

        var tut = (Tutor) LoginManager.makeAccountFromQuery(Database.Database.Read(
                LoginManager.ACCOUNTS + "/" + sess.getTutor()
        ));
        tut.CancelSession(sess);
    }
}
