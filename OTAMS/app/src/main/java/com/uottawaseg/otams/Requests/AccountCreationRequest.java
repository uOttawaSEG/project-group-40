package com.uottawaseg.otams.Requests;

import com.uottawaseg.otams.Accounts.Account;

public abstract class AccountCreationRequest implements Request {

    public RequestStatus GetRequestStatus() {
        return getStatus();
    }
    public RequestType GetRequestType() {
        return getType();
    }
    public abstract RequestStatus getStatus();
    public abstract RequestType getType();
    public abstract Account getAccount();

    @Override
    public abstract void AcceptRequest();

    @Override
    public abstract void DeclineRequest();

    @Override
    public abstract String toString();
}
