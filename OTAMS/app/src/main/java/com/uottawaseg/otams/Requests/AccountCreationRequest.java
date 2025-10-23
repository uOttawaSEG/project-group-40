package com.uottawaseg.otams.Requests;

import com.uottawaseg.otams.Accounts.Account;

public abstract class AccountCreationRequest implements Request {
    public RequestType GetRequestType() {
        return getType();
    }
    public abstract RequestType getType();
    public abstract Account getAccount();

    @Override
    public abstract long GetRequestID();

    // Though implemented, these functions won't actually touch the database!
    @Override
    public abstract void AcceptRequest();

    @Override
    public abstract void DeclineRequest();

    @Override
    public abstract String toString();
}
