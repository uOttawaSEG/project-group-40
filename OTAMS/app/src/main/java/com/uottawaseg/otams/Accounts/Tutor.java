package com.uottawaseg.otams.Accounts;

import androidx.annotation.NonNull;

import com.uottawaseg.otams.Courses.*;

import java.util.HashMap;

public class Tutor extends Account {
    private Degree _highestDegreeOfStudy;
    private Field _fieldOfStudy;

    private final Role _role = Role.TUTOR;

    public Tutor(String firstName, String lastName, String username, String password,
                 String phoneNumber, String email, Degree highestDegreeOfStudy, Field fieldOfStudy) {
        super(firstName, lastName, username, password, phoneNumber, email);
        _highestDegreeOfStudy = highestDegreeOfStudy;
        _fieldOfStudy = fieldOfStudy;
    }

    public Degree getDegree() {
        return _highestDegreeOfStudy;
    }
    public Role getRole() {
        return _role;
    }

    // We should have to manually fix any degree screw ups
    public boolean updateDegree(Degree newHighest) {
        if(newHighest.getValue() < _highestDegreeOfStudy.getValue()) {
            return false;
        }
        _highestDegreeOfStudy = newHighest;
        return true;
    }

    public void setFieldOfStudy(Field f) {
        _fieldOfStudy = f;
    }
    public Field getFieldOfStudy() {
        return _fieldOfStudy;
    }

    @NonNull
    @Override
    public String toString() {
        // If their degree is >= masters they get a prefix
        // Otherwise it's just normal with their degree tacked onto the end
        var sb = new StringBuilder();
        sb.append(super.toString());

        if(getDegree().getValue() >= Degree.MASTERS.getValue()) {
            // At this point it's either a masters or a doctorate
            if(getDegree() == Degree.MASTERS) {
                sb.append("Master of ");
            } else {
                sb.append("Doctor of ");
            }
            sb.append(getFieldOfStudy()).append("\n");
        }
        else {
            sb.append("Highest Degree of Study: ").append(_highestDegreeOfStudy).append("\n");
        }
        return sb.toString();
    }

    @Override
    public HashMap<String, Object> ConvertToMap() {
        var superMap = super.ConvertToMap();
        superMap.put("FieldOfStudy", _fieldOfStudy);
        superMap.put("HighestDegree", _highestDegreeOfStudy);
        return superMap;
    }
}
