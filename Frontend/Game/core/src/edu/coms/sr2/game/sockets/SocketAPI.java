package edu.coms.sr2.game.sockets;

import java.io.IOException;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.screens.social.FriendListScreen;

public class SocketAPI {
    public static void logout() {
        try {
            SocketController.send("logout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void login() {
        if(Application.getProfileId() == -1)
            throw new RuntimeException("Can't login with null profile id (-1)");
        try {
            SocketController.send("login", Application.getProfileId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
