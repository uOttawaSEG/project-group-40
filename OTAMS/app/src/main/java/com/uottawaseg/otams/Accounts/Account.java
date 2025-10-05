package com.uottawaseg.otams.Account;

public abstract class Account {
    protected String _username, _password, _phoneNumber,
            _email, _firstName, _lastName;

    // At some point, this will be protected and only done through the DB.
    public Account(String firstName, String lastName, String username, String password, String phone_Number, String email){
        _username = username;
        _password = password;
        _phoneNumber = phone_Number;
        _email = email;
        _firstName = firstName;
        _lastName = lastName;
    }

    public String getUsername() {
        return _username;
    }
    public String getPassword() {
        return _password;
    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    public String getEmail() {
        return _email;
    }

    public String getName() {
        return  _firstName + " " + _lastName;
    }
    // This will be used publicly not internally, thus the password will be ommitted
    // We don't want to print the password at every chance we get
    public String toString() {
        var sb =  new StringBuilder();
        sb.append("Name: ").append(getName()).append("\n");
        sb.append("Username: ").append(getUsername()).append("\n");
        sb.append("Phone Number: ").append(getPhoneNumber()).append("\n");
        sb.append("Email: ").append(getEmail()).append("\n");
    }
}
