package edu.coms.sr2.game.objects.pojo;

public class RegistrationResponse {
    private enum Status { GOOD, BAD };
    private Status status;
    private String error;
    private Profile newProfile;

    public boolean isGood() {
        return status == Status.GOOD;
    }

    public String getError() {
        return error;
    }

    public Profile getNewProfile() {
        return newProfile;
    }
}
