package com.uottawaseg.otams.Requests;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;

import com.uottawaseg.otams.Accounts.Account;
import com.uottawaseg.otams.Layout.Login;
import com.uottawaseg.otams.Layout.MainActivity;
import com.uottawaseg.otams.Layout.RejectedMessage;

public class StudentAccountRequest extends AccountCreationRequest {
    private Account _acc;
    private RequestStatus _status;
    private final RequestType TYPE = RequestType.StudentAccountCreation;
    public StudentAccountRequest(Account acc) {
        _acc = acc;
        _status = RequestStatus.PENDING;
    }

    // 25/11/14 - Annie - Also added this... trying to figure out how to make it work
    private Context context;
    


    @Override
    public RequestStatus getStatus() {
        return _status;
    }

    @Override
    public RequestType getType() {
        return TYPE;
    }

    @Override
    public Account getAccount() {
        return _acc;
    }

    @Override
    public void AcceptRequest() {
        _status = RequestStatus.ACCEPTED;
        // TODO: Send user an email
    }

    @Override
    public void DeclineRequest() {
        _status = RequestStatus.DENIED;

        // 25/11/14 - Annie - Added these lines of code so that the rejected XML page
        // appers if the admin rejects the account creation request
        Intent intent = new Intent(context, RejectedMessage.class);
        context.startActivity(intent);
        // TODO: Send user an email
    }

    @Override
    public String toString() {
        // StringBuilder not at all required here but it keeps things nice.
        var sb = new StringBuilder();
        sb.append("Student account creation request:\n");
        sb.append("\nAccount details:\n").append(getAccount().toString());
        return sb.toString();
    }
}
