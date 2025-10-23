package com.uottawaseg.otams.Requests;

public interface Request {

    RequestType GetRequestType();
    long GetRequestID();
    void AcceptRequest();
    void DeclineRequest();

    String toString();
}
