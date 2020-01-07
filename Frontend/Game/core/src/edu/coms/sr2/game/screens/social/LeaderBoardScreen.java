package edu.coms.sr2.game.screens.social;

import java.util.Timer;
import java.util.TimerTask;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.http.RestAPI;
import edu.coms.sr2.game.objects.pojo.Profile;
import edu.coms.sr2.game.utils.HtmlBuilder;

public class LeaderBoardScreen extends FriendAddingScreen {
    private static LeaderBoardScreen instance = new LeaderBoardScreen();
    public static LeaderBoardScreen getInstance() { return instance; }
    public static final int numLeaderboardRows = 5;

    protected LeaderBoardScreen() { super(numLeaderboardRows); }
    private Timer refreshTimer;

    @Override
    public void onResume() {
        Threading.runAsync(()->showLeaderboard());
        startRefreshing();
    }

    @Override
    public void onPause() {
        refreshTimer.cancel();
    }

    private void startRefreshing() {
        if(refreshTimer != null)
            refreshTimer.cancel();

        refreshTimer = new Timer();
        refreshTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                showLeaderboard();
            }
        }, 2500l, 2500l);
    }

    private void showLeaderboard() {
        Profile[] leaderboard = RestAPI.getLeaderboard(numLeaderboardRows).get();

        updateViews(leaderboard, (index, isFriend)->{
            Profile profile = leaderboard[index];
            String rank = new HtmlBuilder("" + profile.getCurrentRank()).addBold().addColor(getRankColor(index)).build();
            String userName = getUsernameString(profile.getUserName(), isFriend, profile.getId() == Application.getProfileId());

            return rank + ":  " + userName;
        }, ()-> showLeaderboard());
    }

    private String getRankColor(int index) {
        switch(index) {
            case 0:
                return "fcdf03";
            case 1:
                return "fc7703";
            case 2:
                return "fc0388";
            case 3:
                return "503fc";
            case 4:
                return "0388fc";
            default:
                throw new IllegalStateException("Unexpected value: " + index);
        }
    }
}
