package com.uottawaseg.otams.Accounts;

public class Administrator extends Account {
    private final Role _role = Role.ADMIN;
    public Administrator(String firstName, String lastName, String username, String password, String phone_Number, String email) {
        super(firstName, lastName, username, password, phone_Number, email);
    }

    public Role getRole() {
        return _role;
    }
    // TODO:
    // Add more functionality :D
}
