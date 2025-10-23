package com.uottawaseg.otams.Requests;

import com.uottawaseg.otams.Accounts.Account;

public class StudentAccountRequest extends AccountCreationRequest {
    private Account _acc;
    private long _reqID;
    private final RequestType TYPE = RequestType.StudentAccountCreation;
    public StudentAccountRequest(long ID, Account acc){
        _reqID = ID;
        _acc = acc;
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
    public long GetRequestID() {
        return _reqID;
    }

    // I'll make an email manager class.
    @Override
    public void AcceptRequest() {
        // TODO: Send user an email
    }

    @Override
    public void DeclineRequest() {
        // TODO: Send user an email
    }

    @Override
    public String toString() {
        // StringBuilder not at all required here but it keeps things nice.
        var sb = new StringBuilder();
        sb.append("Tutor account creation request:\n");
        sb.append("Request ID: ").append(GetRequestID());
        sb.append("\n Account details:\n").append(getAccount().toString());
        return sb.toString();
    }
}
