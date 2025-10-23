package com.uottawaseg.otams.Requests;

import android.util.Pair;

import com.uottawaseg.otams.Accounts.Account;

public class RequestDisplayManager {
    /** @noinspection rawtypes, unchecked */
    public static final Pair[] Indices
            = new Pair[] {
            new Pair("Title", 0),
            new Pair("Title", 1),
            new Pair("RequestID", 2),
            new Pair("FirstName", 3),
            new Pair("LastName", 4),
            new Pair("Username", 5),
            new Pair("PhoneNumber", 6),
            new Pair("Email", 7),
            new Pair("Extra", 8),
    };

    public static String[] PrepareRequestToDisplay(AccountCreationRequest req) {
        var isTutor = req.getAccount().getRole() == Account.Role.TUTOR;
        var strs = req.toString().split("\n");
        var result = new String[9];
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
        for(int i = 0; i < 8; i++) {
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
