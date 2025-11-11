package com.uottawaseg.otams.Requests;

public interface Request {
    // We expect all requests to be able to be accepted, declined, and have their status/type checked.
    RequestStatus GetRequestStatus();
    RequestType GetRequestType();
    void AcceptRequest();
    void DeclineRequest();

    String toString();

}
