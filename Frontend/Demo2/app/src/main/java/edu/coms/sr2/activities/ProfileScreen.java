package edu.coms.sr2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import edu.coms.sr2.R;
import edu.coms.sr2.objects.Profile;
import edu.coms.sr2.utils.HttpUtils;

public class ProfileScreen extends AppCompatActivity {

    private TextView user,name,currentRank,highestRank,lowestRank,wins,losses;

    private Button closeButton;

    private void setViews()
    {
        user = findViewById(R.id.user);
        name = findViewById(R.id.realName);
        currentRank = findViewById(R.id.currentRank);
        highestRank = findViewById(R.id.highestRank);
        lowestRank = findViewById(R.id.lowestRank);
        wins = findViewById(R.id.wins);
        losses = findViewById(R.id.losses);

        closeButton = findViewById(R.id.close_button);
    }

    private void setListeners() {
        closeButton.setOnClickListener(view -> moveToLobby());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_screen);
        setViews();
        setListeners();

        displayProfile();
    }

    public void moveToLobby(){
        startActivity(new Intent(this, LobbyScreen.class));
    }

    public void displayProfile()
    {
        HttpUtils.GET(Profile.class, "profiles/" + LoginScreen.getID(), profile->{
            user.setText("Username: " + profile.getUserName());
            name.setText("Name: " + profile.getRealName());
            currentRank.setText("Current Rank: " + profile.getCurrentRank());
            highestRank.setText("Highest Rank: " + profile.getHighestRank());
            lowestRank.setText("Lowest Rank: " + profile.getLowestRank());
            wins.setText("Wins: " + profile.getWins());
            losses.setText("Losses: " + profile.getLosses());
        });
    }
}
