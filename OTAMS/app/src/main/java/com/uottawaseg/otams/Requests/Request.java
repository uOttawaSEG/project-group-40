package com.uottawaseg.otams.Requests;

public interface Request {
    RequestStatus GetRequestStatus();
    RequestType GetRequestType();
    long GetRequestID();
    void AcceptRequest();
    void DeclineRequest();

    String toString();
}
