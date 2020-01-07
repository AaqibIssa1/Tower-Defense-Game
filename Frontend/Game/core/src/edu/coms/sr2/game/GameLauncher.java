package edu.coms.sr2.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import edu.coms.sr2.game.interfacing.Activities;
import edu.coms.sr2.game.interfacing.Activity;

public class GameLauncher extends Game {

	public static final Activities bootActivity = Activities.LOGIN;
	public static final String TAG = GameLauncher.class.getSimpleName();

	private static GameLauncher instance;
	public static GameLauncher getInstance() { return instance; }

	private Activity activity;
	public GameLauncher(Activity activity) { this.activity = activity; }
	public GameLauncher() { activity = null; }

	private static boolean isActive;

	private ThreadSetterKey threadSetterKey = new ThreadSetterKey();

	public static void setActive(boolean val) { isActive = val; }
	public static boolean isActive() { return isActive; }

	public static ArrayList<ContextListener> contextListeners = new ArrayList<>();

	public interface ContextListener {
		void reload();
	}

	public static boolean addContextListener(ContextListener listener) {
		return contextListeners.add(listener);
	}

	@Override
	public void create () {
		Application.log(TAG, "create()");
		//if(instance != null)
			//instance.dispose();

		instance = this;
		isActive = true;

		Threading.runAsync(()->{
			Application.waitForInit();
			if(activity != null)
				activity.start();
			else
				Application.startActivity(bootActivity);
		});

		for(ContextListener listener : contextListeners)
			listener.reload();
	}

	@Override
	public void dispose() {
		super.dispose();
		isActive = false;
	}

	@Override
	public void render() {
		Threading.setGlThread(threadSetterKey);
		super.render();
		if(!isActive())
			Gdx.app.exit();
	}

	public static class ThreadSetterKey {
		private ThreadSetterKey(){}
	}
}
