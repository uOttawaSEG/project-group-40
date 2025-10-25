package com.uottawaseg.otams.Database;

import com.google.firebase.database.DataSnapshot;
import com.uottawaseg.otams.Accounts.*;
import com.uottawaseg.otams.Requests.AccountCreationRequest;
import com.uottawaseg.otams.Requests.RequestType;
import com.uottawaseg.otams.Requests.StudentAccountRequest;
import com.uottawaseg.otams.Requests.TutorAccountRequest;

public class AccountCreationManager {
    private final static String PENDING_REQUESTS = "pendingReqs";
    private final static String ACCOUNT_LOCATION = "account";
    public final static String STATUS = "status";
    public final static String TYPE = "type";

    public final static String GetRequestDir() {
        return PENDING_REQUESTS;
    }
    public final static String GetAccountLocation() { return ACCOUNT_LOCATION; }
    // This is the equivalent to internal in C# (sorta), but Java doesn't have an explicit keyword for it
    static void MakeAccountCreationRequest(Account acc) {
        switch(acc.getRole()) {
            case TUTOR:
                TutorCreation((Tutor) acc);
                break;
            case STUDENT:
                StudentCreation((Student) acc);
                break;
                // We don't need to consider the admin because it already exists.
            default:
                break;
        }
    }

    // This is an overload
    // It takes in DB information, so we won't bother writing to the DB
    static AccountCreationRequest MakeAccountCreationRequest(DataSnapshot data) {
        var type = RequestType.fromString((String) data.child(TYPE).getValue());
        var account = LoginManager.makeAccountFromQuery(data.child(ACCOUNT_LOCATION));
        if(type == RequestType.TutorAccountCreation) {
            return new TutorAccountRequest(account);
        } else {
            // Has to be a student
            return new StudentAccountRequest(account);
        }
    }

    // These functions could be inlined but honestly it's cleaner this way
    private static void TutorCreation(Tutor tut) {
        // We need to make the request
        // Then add it to Database/PendingRequests
        var request = new TutorAccountRequest(tut);
        Database.Database.WriteRequest(PENDING_REQUESTS + "/" + tut.getUsername(), request);
    }

    private static void StudentCreation(Student st) {
        var request = new StudentAccountRequest(st);
        Database.Database.WriteRequest(PENDING_REQUESTS + "/" + st.getUsername(), request);
    }
}
