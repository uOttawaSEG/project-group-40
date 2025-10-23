package com.uottawaseg.otams.Requests;

import android.util.Pair;

import com.uottawaseg.otams.Accounts.Account;

public class RequestDisplayManager {
    /** @noinspection rawtypes, unchecked */
    public final Pair<String, Integer> Title =
            new Pair("Title", 0);
    public final Pair<String, Integer> AccountDetails =
            new Pair("AccountDetails", 1);
    public final Pair<String, Integer> FirstName =
            new Pair("FirstName", 2);
    public final Pair<String, Integer> LastName =
            new Pair("LastName", 3);
    public final Pair<String, Integer> Username =
            new Pair("Username", 4);
    public final Pair<String, Integer> PhoneNumber =
            new Pair("PhoneNumber", 5);
    public final Pair<String, Integer> Email =
            new Pair("Email", 6);
    public final Pair<String, Integer> Extra =
            new Pair("Extra", 7);


    public static String[] PrepareRequestToDisplay(AccountCreationRequest req) {
        var isTutor = req.getAccount().getRole() == Account.Role.TUTOR;
        var strs = req.toString().split("\n");
        var result = new String[8];
        /*
        * This string should look like this:
        *                                    newIndex // Old index is just +1 after reqID
        * Title (Request name)                      0
        * RequestID                                 X
        * Account Details:                          1
        * First name                                2
        * Last name                                 3
        * Username                                  4
        * Phone Number                              5
        * Email                                     6
        * If it's a tutor this is where we'd get their
            * Field of Study                        7
            * Highest degree of study               8

        * Otherwise it's a student account
            * Student ID                            7

        * If not we're cooked
        * */
        var index = 0;
        for(int i = 0; i < 7; i++) {
            if(i == 1) continue;
            result[index++] = strs[i];
        }

        if(isTutor) {
            var field = strs[8];
            var degree = strs[9];
            result[index] = field + ", " + degree;
        } else {
            result[index] = strs[8];
        }
        return result;
    }
}
