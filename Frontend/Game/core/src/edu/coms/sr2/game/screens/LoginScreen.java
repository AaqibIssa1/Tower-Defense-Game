package edu.coms.sr2.game.screens;


import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.Constants;
import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.http.HttpUtils;
import edu.coms.sr2.game.interfacing.Activities;
import edu.coms.sr2.game.interfacing.GenericScreen;
import edu.coms.sr2.game.interfacing.views.TextEntry;
import edu.coms.sr2.game.objects.pojo.LoginInfo;
import edu.coms.sr2.game.objects.pojo.LoginResponse;
import edu.coms.sr2.game.objects.pojo.Profile;
import edu.coms.sr2.game.objects.pojo.ProfileRegistration;
import edu.coms.sr2.game.objects.pojo.RegistrationResponse;
import edu.coms.sr2.game.screens.social.FriendListScreen;
import edu.coms.sr2.game.sockets.SocketAPI;
import edu.coms.sr2.game.sockets.controllers.CommandController;

public class LoginScreen extends GenericScreen {

    private TextEntry emailField, passwordField, profileField;

    protected LoginScreen() {}
    private static LoginScreen instance = new LoginScreen();

    public static LoginScreen getInstance() {
        return instance;
    }

    @Override
    public void onStart() {
        if(!Constants.allowLoginBypass)
            profileField.setVisible(false);
    }

    @Override
    public void onResume() {
        Threading.runAsync(()-> {
            FriendListScreen.getInstance().clearIncomingInvites();
            Application.setProfileId(-1);
            SocketAPI.logout();
        });
    }

    public void moveToLobby(){
        Application.startActivity(Activities.LOBBY);
    }

    private LoginInfo getLoginInfo() {
        return new LoginInfo(emailField.getText(), passwordField.getText());
    }

    @ButtonMethod("login_button")
    public void login()
    {
       if(profileField.getText().isEmpty()) {
            HttpUtils.POST(LoginResponse.class, "login", getLoginInfo(), response -> {
                if (response.isGood()) {
                    Application.setProfileId(response.getProfileId());
                    SocketAPI.login();
                    moveToLobby();
                } else {
                    toast(response.getError());
                }
            });
        } else {
            Application.setProfileId(Integer.parseInt(profileField.getText()));
            Threading.runAsync(SocketAPI::login);
            moveToLobby();
        }
    }

    @ButtonMethod("register_button")
    public void register() {
        HttpUtils.POST(RegistrationResponse.class,"profiles", new ProfileRegistration(new Profile("Noob", "New User"), getLoginInfo()),
            response -> {
                if (response.isGood()) {
                    Application.setProfileId(response.getNewProfile().getId());
                    SocketAPI.login();
                    moveToLobby();
                } else {
                    toast(response.getError());
                }
            });
    }

    public static class LoginCommandController extends CommandController {
        @SocketCommand("alreadyLoggedIn")
        public void alreadyLoggedIn() {
            Application.startActivity(Activities.LOGIN);
            LoginScreen.getInstance().toast("Already logged in on another device.");
        }
    }

}
