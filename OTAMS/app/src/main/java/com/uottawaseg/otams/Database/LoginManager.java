package com.uottawaseg.otams.Database;

import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.uottawaseg.otams.Accounts.Account;
import com.uottawaseg.otams.Accounts.Administrator;
import com.uottawaseg.otams.Accounts.Student;
import com.uottawaseg.otams.Accounts.Tutor;
import com.uottawaseg.otams.Courses.Degree;
import com.uottawaseg.otams.Courses.Field;

import java.util.Arrays;

public class LoginManager {

    // These constants are to be used to simplify DB queries,
    // Or anywhere where you need to interact with the DB.
    // These are both the same intentionally, please leave them as is,
    // Sometimes it makes more sense to refer to it as a type or a role
    // But ultimately it's the same thing

    public static final String ROLE = "role";
    public static final String TYPE = "role";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String STUDENT_NUMBER = "studentNumber";
    public static final String FIELD_OF_STUDY = "fieldOfStudy";
    public static final String HIGHEST_DEGREE = "degree";
    public static final String ACCOUNTS = "accounts";
    // Returns false whether or not the database was able to be setup
    private static boolean _wasPending = false;
    private static Account _currentAccount;
    private static final DatabaseReference db = Database.Database.GetDB();
    private static void setAccount(Account a) {
        _currentAccount = a;
    }
    public static Account getCurrentAccount() {
        return _currentAccount;
    }


    /**
     * @return Whether or not the account found was a pending request or not.
     */
    public static boolean WasPending() {
        return _wasPending;
    }

    /**
     * This checks both the pending requests and the actual accounts.
     * @param username The username to attempt to login with
     * @param password The password that should be associated with that username.
     * @return
     */
    public static Account Login(String username, String password) {

        var data = Database.Database.Read(ACCOUNTS + "/" + username);
        _wasPending = (data == null || !data.exists());
        if(_wasPending) {
            data = Database.Database.Read(
                    AccountCreationManager.GetRequestDir() + "/" + username + "/" + AccountCreationManager.GetAccountLocation());
            if(data == null || !data.exists()) {
                return null;
            }
        }

        var bytes = data.child(PASSWORD).getValue();
        assert bytes != null;
        var pass = Database.GetSHA256(password);
        System.out.println(checkBytes(bytes, pass));
        if(checkBytes(bytes, pass)) {
            setAccount(makeAccountFromQuery(data));
            return getCurrentAccount();
        }
        // If the query was unsuccessful it's because there's a problem somewhere...
        // Probably
        // OOOOOR they screwed up their password/username
        return null;
    }

    /**
     * Sets the current account to null -> Effectively logging out the user.
     */
    public static void LogOut() {
        setAccount(null);
    }

    /**
     * Registers a student account
     * @param firstName The students first name
     * @param lastName The students last name
     * @param username The students username
     * @param password The students password
     * @param phoneNumber The students phone number
     * @param email The students email
     * @param studentNumber The students student number
     */
    // Register a student account
    public static void Register(String firstName, String lastName,
                                   String username, String password, String phoneNumber,
                                   String email, String studentNumber) {
        var acc = new Student(firstName, lastName, username, password,
                phoneNumber, email, studentNumber);
        Register(acc);
    }

    /**
     *  Registers an administrator in the database
     *  This can be made unprivate if the administrator ever gets deleted.
     */
    private static void RegisterAdmin() {
        var account = new Administrator();
        Database.Database.WriteAccount(ACCOUNTS + "/" + account.getUsername(), account);
    }

    /**
     * Overload for the tutor registration.
     * @param firstName The tutors first name
     * @param lastName The tutors last name
     * @param username The tutors username
     * @param password The tutors password name
     * @param phoneNumber The tutors phone number name
     * @param email The tutors email name
     * @param highestDegree The tutors highest degree of study
     * @param fieldOfStudy The tutors field of study
     */
    public static void Register(String firstName, String lastName,
                                   String username, String password, String phoneNumber,
                                   String email, Degree highestDegree, Field fieldOfStudy) {
        var acc = new Tutor(firstName, lastName, username, password,
                phoneNumber, email, highestDegree, fieldOfStudy);

        Register(acc);
    }

    /**
     * Generates an AccountCreationRequest and adds it to the database
     * @param acc The account to register
     */
    private static void Register(Account acc) {
        // Check if the username already exists
        // if it does, ret false
        // otherwise set
        _wasPending = true;
        _currentAccount = acc;
        AccountCreationManager.MakeAccountCreationRequest(acc);

    }

    /**
     * Generates an account from a DataSnapshot.
     * @param data Data to analyse to make the account
     * @return The account made from the data
     * @throws ClassCastException this should never actually throw.
     */
    @Nullable
    public static Account makeAccountFromQuery(DataSnapshot data) throws ClassCastException {
        Account.Role type = Account.Role.fromString(data.child(TYPE).getValue().toString());
        if(type == null || type == Account.Role.UNDEFINED) {
            return null;
        }
        if(type == Account.Role.ADMIN) {
            // TODO: Actually implement this
            return new Administrator();
        }

        /*
         * From Account:
         * map.put(Database.TYPE, _role);
         * map.put(Database.FIRST_NAME, _firstName);
         * map.put(Database.LAST_NAME, _lastName);
         * map.put(Database.USERNAME, getUsername());
         * map.put(Database.PASSWORD, getPassword());
         * map.put(Database.PHONE_NUMBER, getPhoneNumber());
         * map.put(Database.EMAIL, getEmail());
         * These maps aren't actually used but it does let us make sure we have everything
         *
         * Thus, this applies to the only two options left,
         * Tutor and Student
         * */
        var fName = (String)(data.child(FIRST_NAME).getValue());
        var lName = (String)(data.child(LAST_NAME).getValue());
        var username = (String)(data.child(USERNAME).getValue());
        var pass = (String)(data.child(PASSWORD).getValue());
        System.out.println(data.child(PASSWORD).getValue());
        var phoneNum = (String)(data.child(PHONE_NUMBER).getValue());
        var email = (String)(data.child(EMAIL).getValue());

        if (type == Account.Role.STUDENT) {
            var studentNum = (String)(data.child(STUDENT_NUMBER).getValue());
            return new Student(fName, lName, username,
                    pass, phoneNum, email, studentNum);
        }
        if (type == Account.Role.TUTOR) {

            var FoS = Field.fromString((String) (data.child(FIELD_OF_STUDY).getValue()));
            var Deg = Degree.fromString((String) (data.child(HIGHEST_DEGREE).getValue()));
            return new Tutor(fName, lName, username,
                    pass, phoneNum, email, Deg, FoS);
        }
        // We should never get here, all of our options are exhausted.
        // If we somehow get here, we can safely assume something went wrong,
        // But again, we should never get here
        // Once I can confirm everything just works I'll move things around to remove this statement
        return null;
    }

    /**
     * @param bytes The password, from the database
     * @param pass the SHA256 of the users entered password
     * @return Whether or not they're equal.
     */
    private static boolean checkBytes(Object bytes, byte[] pass) {
        var str = Arrays.toString(pass);
        return bytes.equals(str);
    }

    /**
     * @param username Username to check
     * @return Whether or not the username is permitted
     */
    public static boolean CheckUsername(String username) {
        var chars = username.toCharArray();
        for(char c : chars) {
            if(!usernameCharIsAllowed(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param c Char to check
     * @return Whether or not firebase allows that character for a username char.
     */
    private static boolean usernameCharIsAllowed(char c) {
        return !(
                c == '.' || c == ',' || c == '@' || c == '<' || c == '>' ||
                        c == '/' || c == '\'' || c == '\"' || c == '[' || c == ']' ||
                        c == '{' || c == '}' || c == '(' || c == ')' || c == '-' ||
                        c == '+' || c == '=' || c == '&' || c == '^' || c == '%' ||
                        c == '$' || c == '#' || c == '!' || c == '*'
        );
    }

}
