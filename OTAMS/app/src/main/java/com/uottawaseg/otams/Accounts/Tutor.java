package com.uottawaseg.otams.Account;

import com.uottawaseg.otams.Courses;

public class Tutor extends Account {
    private Degree _highestDegreeOfStudy;
    private Field _fieldOfStudy;

    public Tutor(String firstName, String lastName, String username, String password,
                   String phoneNumber, String email, Degree highestDegreeOfStudy, Field fieldOfStudy) {
        super(username, password, phoneNumber, email);
        _highestDegreeOfStudy = highestDegreeOfStudy;
        _fieldOfStudy = fieldOfStudy;
    }

    public Degree getDegree() {
        return _highestDegreeOfStudy;
    }
    public Field getFieldOfStudy() {
        return _fieldOfStudy;
    }
    @Override
    public String toString() {
        // If their degree is >= masters they get a prefix
        // Otherwise it's just normal with their degree tacked onto the end
        var sb = new StringBuilder();
        sb.append(super.toString());

        if(getDegree().getValue() >= Degree.MASTERS) {
            // At this point it's either a masters or a doctorate
            if(getDegree() == Degree.MASTERS) {
                sb.append("Master of ");
            } else {
                sb.append("Doctor of ")
            }
            sb.append(getFieldOfStudy()).append("\n");
        }
        else {
            sb.append("Highest Degree of Study: ").append(_highestDegreeOfStudy).append("\n");
        }
        return sb.toString();
    }

}
