package edu.coms.sr2.game.screens.social;


import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.http.RestAPI;
import edu.coms.sr2.game.interfacing.Activities;
import edu.coms.sr2.game.interfacing.GenericScreen;
import edu.coms.sr2.game.interfacing.views.TextView;

public class ProfileScreen extends GenericScreen {

    private TextView userName,realName,currentRank,highestRank,lowestRank,wins,losses;

    protected ProfileScreen() {}
    private static ProfileScreen instance = new ProfileScreen();

    public static ProfileScreen getInstance() {
        return instance;
    }

    @Override
    public void onResume() {
        displayProfile();
    }

    @ButtonMethod
    public void backButton() {
        stop();
    }

    @ButtonMethod
    public void editButton() {
        Application.startActivity(Activities.EDIT_PROFILE);
    }

    public void displayProfile()
    {
        RestAPI.getProfile(profile->{
            userName.setText("Username: " + profile.getUserName());
            realName.setText("Name: " + profile.getRealName());
            currentRank.setText("Current Rank: " + profile.getCurrentRank());
            highestRank.setText("Highest Rank: " + profile.getHighestRank());
            lowestRank.setText("Lowest Rank: " + profile.getLowestRank());
            wins.setText("Wins: " + profile.getWins());
            losses.setText("Losses: " + profile.getLosses());
        });
    }
}
