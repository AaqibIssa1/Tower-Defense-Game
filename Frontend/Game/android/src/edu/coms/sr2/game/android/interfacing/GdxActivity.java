package edu.coms.sr2.game.android.interfacing;


import android.os.Bundle;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.android.AndroidApplication;

import java.util.ArrayList;
import java.util.function.Supplier;

import edu.coms.sr2.game.GameLauncher;
import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.android.ActivityPersistenceManager;
import edu.coms.sr2.game.android.ActivityPersistenceManager.Persistent;
import edu.coms.sr2.game.android.AndroidLauncher;
import edu.coms.sr2.game.interfacing.Activity;

public class GdxActivity extends AndroidApplication implements Activity {
	public static final String TAG = GdxActivity.class.getSimpleName();

	@Persistent
	protected Screen previousScreen;
	@Persistent
	protected Supplier<Screen> screenSupplier;
	@Persistent
	protected ArrayList<Listener> listeners = new ArrayList<>();

	//Null constructor for Android's activity service
	public GdxActivity() {}

	public GdxActivity(Supplier<Screen> screenSupplier) {
		this.screenSupplier = screenSupplier;
	}
	private boolean isActive;

	@Override
	protected void onStart() {
		super.onStart();
		isActive = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		isActive = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		for(Listener l : listeners)
			l.onResume();
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityPersistenceManager.restore(this);
		initialize(new GameLauncher(this));
	}

	@Override
	public void start() {
		if(GameLauncher.isActive()) {
			previousScreen = GameLauncher.getInstance().getScreen();
			Threading.runAsync(()->GameLauncher.getInstance().setScreen(screenSupplier.get()));
		} else {
			previousScreen = null;
			AndroidLauncher.getInstance().startActivity(ActivityPersistenceManager.store(this));
		}
	}

	@Override
	public void stop() {
		ActivityPersistenceManager.remove(getIntent());
		if(previousScreen == null)
			super.finish();
		else
			GameLauncher.getInstance().setScreen(previousScreen);
	}

	@Override
	public void addListener(Listener listener) {
		listeners.add(listener);
	}
}
