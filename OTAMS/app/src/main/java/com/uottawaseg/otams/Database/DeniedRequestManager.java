package com.uottawaseg.otams.Database;

import com.uottawaseg.otams.Requests.AccountCreationRequest;

public class DeniedRequestManager {
    private static final String DECLINED = "declined";

    // Package-only functions
    // We will not be giving them reasons.
    static void DeclineRequest(AccountCreationRequest req) {
        var username = req.getAccount().getUsername();
        Database.Database.Delete(AccountCreationManager.GetRequestDir() + "/" + username);
        Database.Database.WriteRequest(DECLINED + "/" + username, req);
        // When the request is denied, the request will take care of sending the email
        req.DeclineRequest();
    }

}
