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

    private static Account _currentAccount;
    private static final DatabaseReference db = Database.getDB();
    private static void setAccount(Account a) {
        _currentAccount = a;
    }
    public static Account getCurrentAccount() {
        return _currentAccount;
    }


    public static Account Login(String username, String password) {
        var query = db.child(ACCOUNTS).child(username);

        var data = query.get();
        while(!(data.isComplete() || data.isCanceled() || data.isSuccessful())) {
            try{
                Thread.sleep(1);
            } catch (InterruptedException ignored) {

            }
        }
        if(data.isSuccessful()) {
            var result = data.getResult();
            var bytes = result.child(PASSWORD).getValue();
            assert bytes != null;
            var pass = Database.GetSHA256(password);
            System.out.println("Bytes == pass: " + checkBytes(bytes, pass));
            if(checkBytes(bytes, pass)) {
                setAccount(makeAccountFromQuery(result));
                return getCurrentAccount();
            }
        }
        // If the query was unsuccessful it's because there's a problem somewhere...
        // Probably
        return null;
    }
    // This returns whether or not the logout was successful
    public static boolean LogOut() {
        setAccount(null);
        return true;
    }
    // Register a student account
    public static Account Register(String firstName, String lastName,
                                   String username, String password, String phoneNumber,
                                   String email, String studentNumber) {
        var acc = new Student(firstName, lastName, username, password,
                phoneNumber, email, studentNumber);
        return Register(acc);
    }
    public static Account RegisterAdmin() {
        var account = new Administrator();
        return Register(account);
    }
    public static Account Register(String firstName, String lastName,
                                   String username, String password, String phoneNumber,
                                   String email, Degree highestDegree, Field fieldOfStudy) {
        var acc = new Tutor(firstName, lastName, username, password,
                phoneNumber, email, highestDegree, fieldOfStudy);

        return Register(acc);
    }

    private static Account Register(Account acc) {
        // Check if the username already exists
        // if it does, ret false
        // otherwise set

        db.child(ACCOUNTS).child(acc.getUsername()).setValue(acc);
        System.out.println(acc);
        setAccount(acc);
        return acc;
    }

    @Nullable
    private static Account makeAccountFromQuery(DataSnapshot data) throws ClassCastException {
        Account.Role type = Account.Role.fromString(data.child(TYPE).getValue().toString());
        System.out.println(type);
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
        var phoneNum = (String)(data.child(PHONE_NUMBER).getValue());
        var email = (String)(data.child(EMAIL).getValue());

        if (type == Account.Role.STUDENT) {
            /*
             * var superMap = super.ConvertToMap();
             * superMap.put("StudentNumber", _studentNumber);
             * */
            var studentNum = (String)(data.child(STUDENT_NUMBER).getValue());
            return new Student(fName, lName, username,
                    pass, phoneNum, email, studentNum);
        }
        if (type == Account.Role.TUTOR) {
            /*
             * var superMap = super.ConvertToMap();
             * superMap.put("FieldOfStudy", _fieldOfStudy);
             * superMap.put("HighestDegree", _highestDegreeOfStudy);
             * */

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

    private static boolean checkBytes(Object bytes, byte[] pass) {
        var str = Arrays.toString(pass);
        return bytes.equals(str);
    }

    public static boolean CheckUsername(String username) {
        var chars = username.toCharArray();
        for(char c : chars) {
            if(!usernameCharIsAllowed(c)) {
                System.out.println(username);
                return false;
            }
        }
        return true;
    }

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
