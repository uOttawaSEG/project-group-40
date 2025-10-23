
package com.uottawaseg.otams.Accounts;

import com.uottawaseg.otams.Database.Database;
import com.uottawaseg.otams.Database.PendingRequestManager;
import com.uottawaseg.otams.Requests.AccountCreationRequest;

import java.util.Arrays;
import java.util.List;

public class Administrator extends Account {
    private final Role _role = Role.ADMIN;
    private List<AccountCreationRequest> _requests;
    public Administrator() {
        _username = "root";
        _password = Database.GetSHA256("toor");
        _firstName = "Sebastien";
        _lastName = "Gosselin";
        _phoneNumber = "8675309";
        _email = "admin@admin.com";
        _requests = null;
    }

    public Role getRole() {
        return _role;
    }
    // TODO:
    // Add more functionality :D

    /*
    *
    *
    *
    *
    * */

    public void UpdateRequests() {
        _requests = PendingRequestManager.getRequests();
    }


}
