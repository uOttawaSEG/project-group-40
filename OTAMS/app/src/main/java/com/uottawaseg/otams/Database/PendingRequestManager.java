package com.uottawaseg.otams.Database;

import com.uottawaseg.otams.Requests.AccountCreationRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PendingRequestManager {
    private final static String PENDING_REQS = AccountCreationManager.GetRequestDir();
    private static List<AccountCreationRequest> _requests;

    // We don't want to accidentally change the data in the requests, so we'll leave it like this
    public static List<AccountCreationRequest> getRequests() {
        UpdateRequests();
        return Collections.unmodifiableList(_requests);
    }

    public static void UpdateRequests() {
        if(_requests == null) _requests = new ArrayList<>();
        var data = Database.Database.Read(PENDING_REQS);
        var children = data.getChildren();
        for(var snapShot : children) {
            var accountRequest = AccountCreationManager.MakeAccountCreationRequest(snapShot);
            System.out.println(accountRequest);
            _requests.add(accountRequest);
        }
    }
    // Contrary to its name, this only actually gives us the string of requests,
    // Not each individual requested, printed to a console.
    // This will probably be transformed into a slightly nicer way of getting things
    // So that we can have an actually functional list.
    public static String PrintRequests() {
        if(_requests == null) UpdateRequests();
        var sb = new StringBuilder();
        for(var req : _requests) {
            sb.append(req).append("\n");
        }
        return sb.toString();
    }

    public static void AcceptRequest(AccountCreationRequest req) {
        var newAcc = req.getAccount();
        Database.Database.WriteAccount(LoginManager.ACCOUNTS, newAcc);

        Database.Database.Delete(PENDING_REQS + "/" + newAcc.getUsername());
        req.AcceptRequest();

        if(_requests == null) UpdateRequests();
        else _requests.remove(req);
    }

    public static void DeclineRequest(AccountCreationRequest req) {
        // This gets it's own class because there's extra functionality instead of just accepting it.
        DeniedRequestManager.DeclineRequest(req);
    }
}
