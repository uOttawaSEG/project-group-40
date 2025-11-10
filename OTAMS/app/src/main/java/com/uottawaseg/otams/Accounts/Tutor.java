
package com.uottawaseg.otams.Accounts;

import androidx.annotation.NonNull;

import com.uottawaseg.otams.Courses.Degree;
import com.uottawaseg.otams.Courses.Field;
import com.uottawaseg.otams.Database.PendingRequestManager;
import com.uottawaseg.otams.Requests.Availability;
import com.uottawaseg.otams.Requests.RequestStatus;
import com.uottawaseg.otams.Requests.SessionRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tutor extends Account {
    private Degree _highestDegreeOfStudy;
    private Field _fieldOfStudy;

    private final Role _role = Role.TUTOR;

    private ArrayList<Availability> _availabilities;
    private ArrayList<SessionRequest> _requests;

    // SOOO MANY THNIGS AAAAAAH
    // Also overloads make me sad please give me back default parameters!
    public Tutor(String firstName, String lastName, String username, String password,
                 String phoneNumber, String email, Degree highestDegreeOfStudy, Field fieldOfStudy) {
        this(firstName, lastName, username, password, phoneNumber, email, highestDegreeOfStudy, fieldOfStudy, null);
    }

    public Tutor(String firstName, String lastName, String username, String password,
                 String phoneNumber, String email, Degree highestDegreeOfStudy, Field fieldOfStudy, ArrayList<Availability> avails) {
        this(firstName, lastName, username, password, phoneNumber, email, highestDegreeOfStudy, fieldOfStudy, null, null);
    }

    public Tutor(String firstName, String lastName, String username, String password,
                 String phoneNumber, String email, Degree highestDegreeOfStudy, Field fieldOfStudy,
                 ArrayList<Availability> avails, ArrayList<SessionRequest> requests) {
        super(firstName, lastName, username, password, phoneNumber, email);
        _highestDegreeOfStudy = highestDegreeOfStudy;
        _fieldOfStudy = fieldOfStudy;

        if(avails == null)
            _availabilities = new ArrayList<>();
        else
            _availabilities = avails;

        if(requests == null)
            _requests = new ArrayList<>();
        else
            _requests = requests;
    }

    public Degree getDegree() {
        return _highestDegreeOfStudy;
    }
    public Role getRole() {
        return _role;
    }
    public List<Availability> getAvailabilities() {
        return Collections.unmodifiableList(_availabilities);
    }
    public void AddAvailability(Availability newAvail) {
        _availabilities.add(newAvail);
        PendingRequestManager.UpdateAvailability(this, _availabilities);
    }

    /**
     * @return A list of approved sessions
     */
    public List<SessionRequest> getSessions() {
        var temp = new ArrayList<SessionRequest>(_requests.size());
        for(var req : _requests) {
            if(req.GetRequestStatus() == RequestStatus.ACCEPTED) {
                temp.add(req);
            }
        }
        return Collections.unmodifiableList(temp);
    }

    /**
     * @return A list of all session requests
     */
    public List<SessionRequest> GetAllSessions() {
        return Collections.unmodifiableList(_requests);
    }
    /**
     * @param newHighest The degree to set as the new degree
     * @return Whether or not we were able to update the degree
     */
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

    public void removeAvailability(Availability avail) {
        _availabilities.remove(avail);
        PendingRequestManager.UpdateAvailability(this, _availabilities);
    }
}
