package com.uottawaseg.otams.Database;

import com.uottawaseg.otams.Requests.AccountCreationRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeniedRequestManager {
    public static final String DECLINED = "denied";
    private static List<AccountCreationRequest> _requests;

    // Package-only functions
    // We will not be giving them reasons.

    static void DeclineRequest(AccountCreationRequest req) {
        var username = req.getAccount().getUsername();
        Database.Database.Delete(AccountCreationManager.GetRequestDir() + "/" + username);
        Database.Database.WriteRequest(DECLINED + "/" + username, req);
        // When the request is denied, the request will take care of sending the email
        req.DeclineRequest();
    }

    // We don't want to accidentally change the data in the requests, so we'll leave it like this
    public static List<AccountCreationRequest> getRequests() {
        UpdateRequests();
        return Collections.unmodifiableList(_requests);
    }

    public static void UpdateRequests() {
        _requests = new ArrayList<>();
        var data = Database.Database.Read(DECLINED);
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


    public static boolean SelectRequest(String username) {
        var data = Database.Database.Read(DECLINED + "/" + username);
        if(!data.exists() || data == null) {
            return false;
        }
        PendingRequestManager.SelectRequest(AccountCreationManager.MakeAccountCreationRequest(data));
        return true;
    }
}
