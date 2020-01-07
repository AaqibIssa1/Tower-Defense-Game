package edu.coms.sr2.game.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.GameLauncher;
import edu.coms.sr2.game.R;
import edu.coms.sr2.game.android.interfacing.DynamicScreenActivity;
import edu.coms.sr2.game.android.interfacing.GdxActivity;
import edu.coms.sr2.game.android.interfacing.GenericScreenActivity;
import edu.coms.sr2.game.android.interfacing.NativeActivity;
import edu.coms.sr2.game.interfacing.Activities;
import edu.coms.sr2.game.interfacing.Activity;
import edu.coms.sr2.game.interfacing.ActivityManager;
import edu.coms.sr2.game.screens.ConnectionWaitScreen;
import edu.coms.sr2.game.screens.social.FriendListScreen;
import edu.coms.sr2.game.screens.game.GameScreen;
import edu.coms.sr2.game.screens.social.LeaderBoardScreen;
import edu.coms.sr2.game.screens.LobbyScreen;
import edu.coms.sr2.game.screens.LoginScreen;
import edu.coms.sr2.game.screens.social.ProfileEditScreen;
import edu.coms.sr2.game.screens.social.ProfileScreen;
import edu.coms.sr2.game.screens.social.RecentPlayersScreen;
import edu.coms.sr2.game.screens.game.SpectateScreen;


public class AndroidLauncher extends AndroidApplication implements ActivityManager
{
	public static final String TAG = AndroidLauncher.class.getSimpleName();
	private static AndroidLauncher instance;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;

		initialize(new GameLauncher());
		//Necessary
		Application.log(TAG, NativeActivity.TAG);

		Application.create(ApplicationType.Android, this);
	}

	public static AndroidLauncher getInstance() { return instance; }

	@Override
	public Activity getActivity(Activities activity) {
		switch(activity)
		{
			case LOGIN:
				return new GenericScreenActivity(R.layout.login_screen, LoginScreen.getInstance());
			case LOBBY:
				return new GenericScreenActivity(R.layout.lobby_screen, LobbyScreen.getInstance());
			case PERSONAL_PROFILE:
				return new GenericScreenActivity(R.layout.profile_screen, ProfileScreen.getInstance());
			case EDIT_PROFILE:
				return new GenericScreenActivity(R.layout.profile_edit_screen, ProfileEditScreen.getInstance());
			case GAME_SCREEN:
				return new GdxActivity(()->GameScreen.newInstance());
			case FRIENDS_LIST:
				return new DynamicScreenActivity(FriendListScreen.getInstance());
			case SPECTATE:
				return new GdxActivity(()-> SpectateScreen.newInstance());
			case LEADERBOARD:
				return new GenericScreenActivity(R.layout.leaderboard_screen, LeaderBoardScreen.getInstance());
			case WAIT_FOR_CONNECTION:
				return new GenericScreenActivity(R.layout.connection_wait_screen, ConnectionWaitScreen.getInstance());
			case RECENT_PLAYERS:
				return new GenericScreenActivity(R.layout.recent_players_screen, RecentPlayersScreen.getInstance());
			default:
				throw new IllegalStateException("Unexpected value: " + activity);
		}
	}

	@Override
	public void startActivity(Activities activity) {
		AndroidLauncher.getInstance().runOnUiThread(()-> getActivity(activity).start());
	}
}
