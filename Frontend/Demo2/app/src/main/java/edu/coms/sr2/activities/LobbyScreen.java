package edu.coms.sr2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import edu.coms.sr2.R;

public class LobbyScreen extends AppCompatActivity {

    private Button backButton;
    private Button profileButton;
    private TextView title;

    private void setViews()
    {
        title = findViewById(R.id.lobby_title);
        backButton = findViewById(R.id.back_button);
        profileButton = findViewById(R.id.profile_button);
    }

    private void setListeners()
    {
        backButton.setOnClickListener(view -> moveToMain());
        profileButton.setOnClickListener(view -> moveToProfile());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lobby_screen);
        setViews();
        setListeners();

        showLoginId();
    }

    private void showLoginId() {
        title.setText("Logged in as ID: " + LoginScreen.getID());
    }

    public void moveToProfile(){
        startActivity(new Intent(this, ProfileScreen.class));
    }

    public void moveToMain(){
        startActivity(new Intent(this, LoginScreen.class));
    }
}
