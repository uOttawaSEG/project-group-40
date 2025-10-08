package com.uottawaseg.otams.Accounts;

import com.uottawaseg.otams.Database.Database;

import java.util.Arrays;

public class Administrator extends Account {
    private final Role _role = Role.ADMIN;
    public Administrator() {
        super("root", Arrays.toString(Database.GetSHA256("toor")), "8675309", "admin@admin.com");
        _username = "root";
        _password = Database.GetSHA256("toor");
        _firstName = "Sebastien";
        _lastName = "Gosselin";
        _phoneNumber = "8675309";
        _email = "admin@admin.com";
    }

    public Role getRole() {
        return _role;
    }
    // TODO:
    // Add more functionality :D
}
