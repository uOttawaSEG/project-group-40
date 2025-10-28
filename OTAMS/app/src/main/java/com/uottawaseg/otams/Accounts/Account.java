
package com.uottawaseg.otams.Accounts;

import androidx.annotation.NonNull;

import com.uottawaseg.otams.Database.Database;

import java.util.Arrays;

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

    public Account(String firstName, String lastName, String username, String password, String phone_Number, String email){
        _username = username;
        _phoneNumber = phone_Number;
        _email = email;
        _firstName = firstName;
        _lastName = lastName;

        // Passwords should never be stored as plain text.
        if(!password.startsWith("[") && !password.endsWith("]"))
            _password = Database.GetSHA256(password);
        else
            _password = strToByteArr(password);
    }
    private byte[] strToByteArr(String str) {
        var splitStr = str.split(", ");
        var result = new byte[splitStr.length];
        var i = 0;
        for(String s : splitStr) {
            if(i == 0) {
                // We need to exclude the [
                var data = s.split("");
                var sb = new StringBuilder();
                for(int x = 1; x < data.length; x++) {
                    sb.append(data[x]);
                }
                result[i++] = Byte.parseByte(sb.toString());
            } else if(i == splitStr.length - 1) {

                // We need to exclude the ]
                var data = s.split("");
                var sb = new StringBuilder();
                for(int x = 0; x < data.length - 1; x++) {
                    sb.append(data[x]);
                }
                result[i++] = Byte.parseByte(sb.toString());
            } else {
                result[i++] = Byte.parseByte(s);
            }
        }
        return result;
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

    public abstract Role getRole();

    /**
     * @return A string containing the first name, last name, username, phone number, and email. Does not include password.
     */
    @NonNull
    public String toString() {
        var sb =  new StringBuilder();
        sb.append("First name: ").append(getFirstName()).append("\n");
        sb.append("Last name: ").append(getLastName()).append("\n");
        sb.append("Username: ").append(getUsername()).append("\n");
        sb.append("Phone Number: ").append(getPhoneNumber()).append("\n");
        sb.append("Email: ").append(getEmail()).append("\n");
        return sb.toString();
    }
}
