package edu.coms.sr2.game.screens;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.interfacing.Activities;
import edu.coms.sr2.game.interfacing.GenericScreen;
import edu.coms.sr2.game.interfacing.views.Button;
import edu.coms.sr2.game.interfacing.views.Spinner;
import edu.coms.sr2.game.interfacing.views.TextView;
import edu.coms.sr2.game.sockets.SocketController;
import edu.coms.sr2.game.sockets.controllers.CommandController;
import edu.coms.sr2.game.utils.HtmlBuilder;

public class LobbyScreen extends GenericScreen {

    @LinkView("lobby_title")
    private TextView title;
    @LinkView("mm_progress")
    private Spinner spinner;
    private TextView findingMatchText;
    private Button playButton;
    private boolean isFindingMatch = false;

    public static final String FINDING_MATCH_HTML = new HtmlBuilder("Finding match...").addItalics().addColor("34eb61").build();
    public static final String CANCEL_HTML = new HtmlBuilder("CANCEL").addItalics().addColor("FF0000").build();
    public static final String PLAY_HTML = new HtmlBuilder("PLAY").addBold().addColor("34eb61").build();

    protected LobbyScreen() {}
    private static LobbyScreen instance = new LobbyScreen();
    public static LobbyScreen getInstance() {
        return instance;
    }

    @Override
    public void onStart() {
        findingMatchText.setVisible(false);
        findingMatchText.setTextFromHTML(FINDING_MATCH_HTML);
        spinner.setVisible(false);
        playButton.setTextFromHTML(PLAY_HTML);
        showLoginId();
    }

    @ButtonMethod
    public void backButton() {
        stop();
    }

    @ButtonMethod
    public void friendsButton() {
        Application.startActivity(Activities.FRIENDS_LIST);
    }

    public void showLoginId() {
        if(isActive())
            runOnUiThread(()->
                title.setText("Logged in as ID: " + Application.getProfileId()));
    }

    public void showTime(String time) {
        if(isActive())
            runOnUiThread(()->
                    title.setText("Server time: " + time));
    }

    @ButtonMethod
    public void profileButton() {
        Application.startActivity(Activities.PERSONAL_PROFILE);
    }

    @ButtonMethod
    public void playButton() {
        Threading.runAsync(()-> {
            if(isFindingMatch)
                cancelMatchMaking();
            else
                startMatchMaking();
        });
    }

    public void cancelMatchMaking() {
        SocketController.trySend("stopFindingMatch");
    }

    public void startMatchMaking() {
        SocketController.trySend("findMatch");
    }

    @ButtonMethod
    public void leaderboardButton() {
        Application.startActivity(Activities.LEADERBOARD);
    }

    @ButtonMethod
    public void recentPlayersButton() {
        Application.startActivity(Activities.RECENT_PLAYERS);
    }

    public static class LobbyCommandController extends CommandController {
        @SocketCommand("set_time")
        public void setTime(String time) {
            getInstance().showTime(time);
        }

        @SocketCommand("startGame")
        public void startGame() {
            Application.startActivity(Activities.GAME_SCREEN);
        }

        @SocketCommand
        public void findingMatch() {
            getInstance().isFindingMatch = true;
            getInstance().playButton.setTextFromHTML(CANCEL_HTML);
            getInstance().spinner.setVisible(true);
            getInstance().findingMatchText.setVisible(true);
        }

        @SocketCommand
        public void stopFindingMatch() {
            getInstance().isFindingMatch = false;
            getInstance().playButton.setTextFromHTML(PLAY_HTML);
            getInstance().spinner.setVisible(false);
            getInstance().findingMatchText.setVisible(false);
        }
    }
}
