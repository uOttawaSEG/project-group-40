
package com.uottawaseg.otams.Accounts;

import androidx.annotation.NonNull;

import com.uottawaseg.otams.Courses.Degree;
import com.uottawaseg.otams.Courses.Field;
import com.uottawaseg.otams.Database.PendingRequestManager;
import com.uottawaseg.otams.Database.SessionRequestManager;
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
    private ArrayList<SessionRequest> _sessions;

    /**
     * @param firstName Tutors First name
     * @param lastName Tutors last name
     * @param username Accounts username
     * @param password Accounts password, plain text
     * @param phoneNumber A valid phone number
     * @param email A valid email
     * @param highestDegreeOfStudy The highested degree obtained.
     * @param fieldOfStudy The field of which the tutor is/has studied
     */
    // SOOO MANY THNIGS AAAAAAH
    // Also overloads make me sad please give me back default parameters!
    public Tutor(String firstName, String lastName, String username, String password,
                 String phoneNumber, String email, Degree highestDegreeOfStudy, Field fieldOfStudy) {
        this(firstName, lastName, username, password, phoneNumber, email, highestDegreeOfStudy, fieldOfStudy, null, null);
    }

    /**
     * @param firstName Tutors First name
     * @param lastName Tutors last name
     * @param username Accounts username
     * @param password Accounts password, plain text
     * @param phoneNumber A valid phone number
     * @param email A valid email
     * @param highestDegreeOfStudy The highested degree obtained.
     * @param fieldOfStudy The field of which the tutor is/has studied
     * @param avails An ArrayList<Availability> of availabilities
     * @param requests An ArrayList<SessionRequest> of requests applicable to the tutor
     */
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
            _sessions = new ArrayList<>();
        else
            _sessions = requests;
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
    public List<SessionRequest> getAcceptedSessions() {
        var temp = new ArrayList<SessionRequest>(_sessions.size());
        for(var req : getSessions()) {
            if(req.getStatus().equals(RequestStatus.ACCEPTED)) {
                temp.add(req);
            }
        }
        return Collections.unmodifiableList(temp);
    }

    /**
     * @return A list of all session requests
     */
    public List<SessionRequest> getSessions() {
        return Collections.unmodifiableList(_sessions);
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

    /**
     * @param f Field of study to assign
     */
    public void setFieldOfStudy(Field f) {
        _fieldOfStudy = f;
    }

    /**
     * @return Field of study of the current object
     */
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

    /**
     * @param avail Availability to remove
     */
    public void removeAvailability(Availability avail) {
        _availabilities.remove(avail);
        PendingRequestManager.UpdateAvailability(this, _availabilities);
    }

    /**
     * @param sessionRequest Session to add
     */
    public void AddSession(SessionRequest sessionRequest) {
        _sessions.add(sessionRequest);
        SessionRequestManager.UpdateSessions(this);
    }

    /**
     * @param s SessionRequest to accept
     */
    public void AcceptSession(SessionRequest s) {
        for(var sess : getSessions()) {
            if(sess.equals(s)) {
                sess.setStatus(RequestStatus.ACCEPTED);
                SessionRequestManager.UpdateSessions(this);
                return;
            }
        }
    }

    /**
     * @param s SessionRequest to decline
     */
    public void DeclineSession(SessionRequest s) {
        //IndexOf was always -1 for some reason
        for(var sess : getSessions()) {
            if(sess.equals(s)) {
                _sessions.remove(sess);
                SessionRequestManager.UpdateSessions(this);
                return;
            }
        }
    }
}
