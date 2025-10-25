package com.uottawaseg.otams.Requests;

import android.util.Pair;

import com.uottawaseg.otams.Accounts.Account;

import java.util.List;

public class RequestDisplayManager {
    /** @noinspection rawtypes, unchecked */
    public static final Pair<String, Integer> TITLE =
            new Pair("Title", 0);
    public static final Pair<String, Integer> ACCOUNT_DETAILS =
            new Pair("AccountDetails", 1);
    public static final Pair<String, Integer> FIRST_NAME =
            new Pair("FirstName", 2);
    public static final Pair<String, Integer> LAST_NAME =
            new Pair("LastName", 3);
    public static final Pair<String, Integer> USERNAME =
            new Pair("Username", 4);
    public static final Pair<String, Integer> PHONE_NUMBER =
            new Pair("PhoneNumber", 5);
    public static final Pair<String, Integer> EMAIL =
            new Pair("Email", 6);
    public static final Pair<String, Integer> OTHER =
            new Pair("Extra", 7);


    public static String[] PrepareRequestToDisplay(AccountCreationRequest req) {
        var isTutor = req.getAccount().getRole() == Account.Role.TUTOR;
        var strs = req.toString().split("\n");
        var result = new String[8];
        /*
        * This string should look like this:
        *                                    newIndex
        * Title (Request name)                      0
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
            var field = strs[7];
            var degree = strs[8];
            result[index] = field + ", " + degree;
        } else {
            result[index] = strs[8];
        }
        return result;
    }

    public static String[] GetRequestForPendingPage(List<AccountCreationRequest> reqs) {
        var arr = new String[reqs.size()];

        /*
        * Each string will be like this
        * Name:             Full name           -> 0
        * Username:         Username            -> 1
        * Role requested:   TUTOR || STUDENT    -> 2
        * This is fine because it's not the full request, it's just for the pending page.
        * */
        int i = 0;
        for(var req : reqs) {
            var acc = req.getAccount();
            arr[i++] = acc.getName() + "\nUsername: " + acc.getUsername() + "\nRole requested: " + acc.getRole();
        }
        return arr;
    }
}
