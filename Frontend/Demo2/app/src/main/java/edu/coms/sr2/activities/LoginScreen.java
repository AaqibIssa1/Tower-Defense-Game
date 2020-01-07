package edu.coms.sr2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import edu.coms.sr2.objects.LoginInfo;
import edu.coms.sr2.objects.LoginResponse;
import edu.coms.sr2.R;
import edu.coms.sr2.objects.Profile;
import edu.coms.sr2.objects.ProfileRegistration;
import edu.coms.sr2.utils.HttpUtils;


public class LoginScreen extends AppCompatActivity {

    private static final String TAG = LoginScreen.class.getSimpleName();

    private TextView allProfiles;
    private Button lobbyButton, getProfilesButton, clearButton, registerButton, loginButton;
    private EditText emailField, passwordField;
    private static int ID = 0;

    private void setViews()
    {
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        allProfiles = findViewById(R.id.all_profiles);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
        getProfilesButton = findViewById(R.id.get_profiles_button);
        clearButton = findViewById(R.id.clear_button);
        lobbyButton = findViewById(R.id.lobby_button);
    }

    private void setListeners()
    {
        getProfilesButton.setOnClickListener(view -> displayAllProfiles());
        clearButton.setOnClickListener(view -> allProfiles.setText(""));
        lobbyButton.setOnClickListener(view -> moveToLobby());
        loginButton.setOnClickListener(view -> login());
        registerButton.setOnClickListener(view -> register());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_screen);
        setViews();
        setListeners();
    }

    public void moveToLobby(){
        startActivity(new Intent(this, LobbyScreen.class));
    }

    public static int getID(){
        return ID;
    }

    private LoginInfo getLoginInfo() {
        return new LoginInfo(
                emailField.getText().toString(),
                passwordField.getText().toString());
    }

    public void login()
    {
        HttpUtils.POST(LoginResponse.class, "login", getLoginInfo(), response->{
            if (response.isGood()) {
                ID = response.getProfileId();
                moveToLobby();
            }
        });
    }

    public void register() {
        HttpUtils.POST(String.class,"profiles", new ProfileRegistration(new Profile("NewUser"), getLoginInfo()));
    }

    private void displayAllProfiles() {
        HttpUtils.GET(Profile[].class, "profiles", profiles->{
            StringBuilder builder = new StringBuilder();
            for(Profile p : profiles)
                builder.append(p.toString() + "\n\n");

            allProfiles.setText(builder.toString());
        });
    }
}
