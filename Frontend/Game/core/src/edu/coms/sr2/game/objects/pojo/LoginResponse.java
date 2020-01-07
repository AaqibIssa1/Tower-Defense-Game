package edu.coms.sr2.game.objects.pojo;

public class LoginResponse
{
    public enum Status { GOOD, BAD }
    Status status;
    String error;
    int authToken;
    int profileId;

    @Override
    public String toString() {
        if(isGood())
            return "Good login: token is " + authToken + ", profile id is " + profileId;
        else
            return "Bad login: error is \"" + error + "\"";
    }

    public boolean isGood() {
        return status == Status.GOOD;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getAuthToken() {
        return authToken;
    }

    public void setAuthToken(int authToken) {
        this.authToken = authToken;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }
}
