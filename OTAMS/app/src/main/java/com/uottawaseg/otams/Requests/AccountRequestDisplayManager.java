package com.uottawaseg.otams.Requests;

import android.util.Pair;

import com.uottawaseg.otams.Accounts.Account;
import com.uottawaseg.otams.Database.DeniedRequestManager;
import com.uottawaseg.otams.Database.PendingRequestManager;

import java.util.List;

public class AccountRequestDisplayManager {
    /** @noinspection rawtypes, unchecked */
    public static final Pair<String, Integer> TITLE =
            new Pair("Title", 0);
    public static final Pair<String, Integer> ACCOUNT_DETAILS =
            new Pair("AccountDetails", 2);
    public static final Pair<String, Integer> FIRST_NAME =
            new Pair("FirstName", 3);
    public static final Pair<String, Integer> LAST_NAME =
            new Pair("LastName", 4);
    public static final Pair<String, Integer> USERNAME =
            new Pair("Username", 5);
    public static final Pair<String, Integer> PHONE_NUMBER =
            new Pair("PhoneNumber", 6);
    public static final Pair<String, Integer> EMAIL =
            new Pair("Email", 7);
    public static final Pair<String, Integer> OTHER =
            new Pair("Extra", 8);


    /**
     * Prepares a single request to be displayed on the admin_client_info page.
     * @param req The request to display
     * @return A string[] with constant indexes for given values
     */
    public static String[] PrepareRequestToDisplay(AccountCreationRequest req) {
        var isTutor = req.getAccount().getRole() == Account.Role.TUTOR;
        var strs = req.toString().split("\n");
        var result = new String[strs.length];
        /*
        * This string should look like this:
        *                                            newIndex
        * Title (Request name)                              0
        * \n                                                1
        * Account Details:                                  2
        * First name                                        3
        * Last name                                         4
        * Username                                          5
        * Phone Number                                      6
        * Email                                             7
        * If it's a tutor this is where we'd get their
            * Highest degree of study + Field of study      8

        * Otherwise it's a student account
            * Student ID                                    8

        * If not we're cooked
        * */
        var index = 0;
        for(int i = 0; i < 8; i++) {
            result[index++] = strs[i];
        }

        if(isTutor) {
            var degree = strs[8];
            result[index] = degree;
        } else {
            result[index] = strs[8];
        }
        return result;
    }

    /**
     * @return A string[] of all pending requests
     */
    public static String[] GetRequestForPendingPage() {
        return GetRequestForPage(PendingRequestManager.getRequests());
    }

    /**
     * @param reqs A list of AccountCreationRequests to change into a string[]
     * @return A string[] to display on the admin_pending_request or admin_rejected_requests pages
     */
    private static String[] GetRequestForPage(List<AccountCreationRequest> reqs) {
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

    /**
     * @return A string[] of all the denied requests
     */
    public static String[] GetRequestForDeniedPage() {
        return GetRequestForPage(DeniedRequestManager.getRequests());
    }
}
