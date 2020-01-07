package edu.coms.sr2.game.sockets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.IOException;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.Constants;
import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.interfacing.Activities;
import edu.coms.sr2.game.interfacing.Activity;
import edu.coms.sr2.game.screens.ConnectionWaitScreen;
import edu.coms.sr2.game.sockets.controllers.CommandController;
import edu.coms.sr2.game.utils.BetterInputStream;
import edu.coms.sr2.game.utils.BetterOutputStream;

public class SocketController
{
    public static final String TAG = SocketController.class.getSimpleName();
    private static Socket socket;
    private static BetterInputStream inputStream;
    private static BetterOutputStream outputStream;
    private static Activity waitingActivity;

    public static void init() {
        try {
            connectAndListen();
        } catch (Exception e) {
            Application.error(TAG, e);
        }
    }

    private static void connectAndListen() throws InterruptedException {
        socket = null;
        Application.startActivity(Activities.WAIT_FOR_CONNECTION);
        while (socket == null) {
            try {
                socket = Gdx.net.newClientSocket(Net.Protocol.TCP, Constants.sr2Server, Constants.sr2TcpPort, new SocketHints());
            } catch (Exception e) {
                Application.error(TAG, e);
                Thread.sleep(5000l);
            }
        }
        inputStream = new BetterInputStream(socket.getInputStream());
        outputStream = new BetterOutputStream(socket.getOutputStream());

        if (Application.getProfileId() != -1) {
            SocketAPI.logout();
            SocketAPI.login();
        }
        if(ConnectionWaitScreen.getInstance().isActive())
            ConnectionWaitScreen.getInstance().stop();
        sendBrokenActivitiesToLobby();

        listen();
    }

    private static void sendBrokenActivitiesToLobby() {
        switch(Application.getCurrentActivity()) {
            case GAME_SCREEN:
            case SPECTATE:
                Application.startActivity(Activities.LOBBY);
        }
    }

    private static void listen() {
        Threading.runAsync(()->{
            while (true) {
                try {
                    String command;
                    try{
                        command = inputStream.readUTF();
                    } catch(Exception e) {
                        Application.error(TAG, e);
                        connectAndListen();
                        return;
                    }
                    CommandController.dispatch(command, inputStream);
                } catch (Exception e) {
                    Application.error(TAG, e);
                }
            }
        });
    }

    public static void send(String command, Object... params) throws IOException {
        Application.log(TAG, "Sending command \"" + command + "\"");
        Threading.assertWorkerThread();
        outputStream.writeUTF(command);
        for(Object param : params)
            outputStream.write(param);
        Application.log(TAG, "Finished sending command \"" + command + "\"");
    }

    public static void trySend(String command, Object... params) {
        try {
            send(command, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(Command command) throws IOException {
        Threading.assertWorkerThread();
        outputStream.writeUTF(command.getName());
        for(Object param : command.getParams())
            outputStream.write(param);
    }
}
