package com.uottawaseg.otams.Database;

import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Requests.AccountCreationRequest;
import com.uottawaseg.otams.Requests.Availability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PendingRequestManager {
    private final static String PENDING_REQS = AccountCreationManager.GetRequestDir();
    private static List<AccountCreationRequest> _requests;

    private static AccountCreationRequest _selectedRequest;

    /**
     * @return Updates the requests and returns an unmodifiable list containing them
     */
    public static List<AccountCreationRequest> getRequests() {
        UpdateRequests();
        return Collections.unmodifiableList(_requests);
    }


    /**
     * @param username The username of the request to find
     * @return Whether or not we were able to find the request associated with the username.
     */
    public static boolean SelectRequest(String username) {
        // Query DB
        // Make account from snapshot
        var data = Database.Database.Read(PENDING_REQS + "/" + username);
        if(data == null || !data.exists()) {
            return false;
        }
        SelectRequest(AccountCreationManager.MakeAccountCreationRequest(data));
        return true;
    }

    public static void SelectRequest(AccountCreationRequest req) {
        _selectedRequest = req;
    }
    public static AccountCreationRequest GetSelectedRequest() {
        return _selectedRequest;
    }

    /**
     * Updates the pending requests.
     */
    public static void UpdateRequests() {
        _requests = new ArrayList<>();
        var data = Database.Database.Read(PENDING_REQS);
        var children = data.getChildren();
        for(var snapShot : children) {
            var accountRequest = AccountCreationManager.MakeAccountCreationRequest(snapShot);
            _requests.add(accountRequest);
        }
    }

    /**
     * @return A string to be printed for our request list.
     */
    public static String PrintRequests() {
        if(_requests == null) UpdateRequests();
        var sb = new StringBuilder();
        for(var req : _requests) {
            sb.append(req).append("\n");
        }
        return sb.toString();
    }

    /**
     * @param req The request to accept
     */
    public static void AcceptRequest(AccountCreationRequest req) {
        var newAcc = req.getAccount();

        //TODO:
        //  Check if the account was declined previously
        //  We should have done this already but w/e
        Database.Database.WriteAccount(LoginManager.ACCOUNTS + "/" + newAcc.getUsername(), newAcc);

        Database.Database.Delete(PENDING_REQS + "/" + newAcc.getUsername());
        Database.Database.Delete(DeniedRequestManager.DECLINED + "/" + newAcc.getUsername());
        req.AcceptRequest();

        if(_requests == null) UpdateRequests();
        else _requests.remove(req);
    }

    /**
     * @param req The request to decline
     */
    public static void DeclineRequest(AccountCreationRequest req) {
        // This gets it's own class because there's extra functionality instead of just accepting it.
        DeniedRequestManager.DeclineRequest(req);
    }

    public static void UpdateAvailability(Tutor tut, List<Availability> avails) {
        System.out.println(avails);
        Database.Database.WriteAvailability(
                LoginManager.ACCOUNTS + "/" + tut.getUsername() + "/" + AvailabilityReader.AVAILABILITIES,
                avails);
    }

}
