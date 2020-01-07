package edu.coms.sr2.game.screens;

import edu.coms.sr2.game.interfacing.GenericScreen;

public class ConnectionWaitScreen extends GenericScreen {
    protected ConnectionWaitScreen() {}
    private static ConnectionWaitScreen instance = new ConnectionWaitScreen();
    public static ConnectionWaitScreen getInstance() { return instance; }
}
