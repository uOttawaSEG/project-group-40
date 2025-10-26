package com.uottawaseg.otams.Requests;

import com.uottawaseg.otams.Accounts.Account;

public class TutorAccountRequest extends AccountCreationRequest {
    private Account _acc;
    private RequestStatus _status;
    private final RequestType TYPE = RequestType.TutorAccountCreation;
    public TutorAccountRequest(Account acc) {
        _acc = acc;
        _status = RequestStatus.PENDING;
    }

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

    // I'll make an email manager class.
    @Override
    public void AcceptRequest() {
        _status = RequestStatus.ACCEPTED;
        // TODO: Send user an email
    }

    @Override
    public void DeclineRequest() {
        _status = RequestStatus.DENIED;
        // TODO: Send user an email
    }

    @Override
    public String toString() {
        // StringBuilder not at all required here but it keeps things nice.
        var sb = new StringBuilder();
        sb.append("Tutor account creation request:\n");
        sb.append("\nAccount details:\n").append(getAccount().toString());
        return sb.toString();
    }
}
