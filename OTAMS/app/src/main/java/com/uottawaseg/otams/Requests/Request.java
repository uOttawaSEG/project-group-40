package com.uottawaseg.otams.Requests;

public interface Request {
    RequestStatus GetRequestStatus();
    RequestType GetRequestType();
    void AcceptRequest();
    void DeclineRequest();

    String toString();
}
