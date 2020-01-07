package edu.coms.sr2.objects;

public class ProfileRegistration
{
    private Profile profile;
    private LoginInfo login;

    public ProfileRegistration(String email, String password, String userName)
    {
        profile = new Profile(userName);
        login = new LoginInfo(email, password);
    }

    public ProfileRegistration(Profile profile, LoginInfo login)
    {
        this.profile = profile;
        this.login = login;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public LoginInfo getLogin() {
        return login;
    }

    public void setLogin(LoginInfo login) {
        this.login = login;
    }
}
