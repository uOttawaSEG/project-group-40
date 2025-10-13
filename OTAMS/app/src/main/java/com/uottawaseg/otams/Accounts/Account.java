
package com.uottawaseg.otams.Accounts;

import androidx.annotation.NonNull;

import com.uottawaseg.otams.Database.Database;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class Account {
    protected String _username, _phoneNumber,
            _email, _firstName, _lastName;
    protected byte[] _password;

    // This exists for our administrator. Exclusively the administrator.
    // Do not use
    protected Account() {
    }

    public enum Role {
        ADMIN(0),
        TUTOR(1),
        STUDENT(2),
        UNDEFINED();

        private final int value;
        Role() {
            this.value = -1;
        }
        Role(int val) {
            this.value = val;
        }
        public int GetValue() {
            return value;
        }
        @Override
        public String toString() {
            switch(value) {
                case 0:
                    return "Administrator";
                case 1:
                    return "Tutor";
                case 2:
                    return "Student";
                default:
                    return "Undefined";
            }
        }
        public static Role fromString(String s) {
            s = s.toUpperCase();
            switch (s) {
                case "ADMIN":
                    return ADMIN;
                case "TUTOR":
                    return TUTOR;
                case "STUDENT":
                    return STUDENT;
                default:
                    return UNDEFINED;

            }
        }
    }
    protected static Role _role;
    // At some point, this will be protected and only done through the DB.
    public Account(String firstName, String lastName, String username, String password, String phone_Number, String email){
        _username = username;
        // Passwords should never be stored as plain text.
        _password = Database.GetSHA256(password);
        _phoneNumber = phone_Number;
        _email = email;
        _firstName = firstName;
        _lastName = lastName;
    }

    public String getUsername() {
        return _username;
    }
    public String getPassword() {
        return Arrays.toString(_password);
    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    public String getEmail() {
        return _email;
    }

    public String getFirstName() {
        return  _firstName;
    }
    public String getLastName() {
        return _lastName;
    }

    public String getName() {
        return getFirstName() + " " + getLastName();
    }
    // This will be used publicly not internally, thus the password will be omitted
    // We don't want to print the password at every chance we get
    @NonNull
    public String toString() {
        var sb =  new StringBuilder();
        sb.append("First name: ").append(getFirstName()).append("\n");
        sb.append("Last name").append(getLastName()).append("\n");
        sb.append("Username: ").append(getUsername()).append("\n");
        sb.append("Phone Number: ").append(getPhoneNumber()).append("\n");
        sb.append("Email: ").append(getEmail()).append("\n");
        return sb.toString();
    }
}
