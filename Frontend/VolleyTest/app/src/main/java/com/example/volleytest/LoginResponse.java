package com.example.volleytest;

public class LoginResponse
{
    public enum Status { GOOD, BAD }
    Status status;
    String error;
    int authToken;
    int profileId;

    @Override
    public String toString() {
        if(status == Status.GOOD)
            return "Good login: token is " + authToken + ", profile id is " + profileId;
        else
            return "Bad login: error is \"" + error + "\"";
    }
}
