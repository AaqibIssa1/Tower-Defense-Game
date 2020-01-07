package edu.coms.sr2.game.android.interfacing;

import android.os.Bundle;

import java.util.ArrayList;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.GameLauncher;
import edu.coms.sr2.game.android.ActivityPersistenceManager.Persistent;
import edu.coms.sr2.game.android.ActivityPersistenceManager;
import edu.coms.sr2.game.android.AndroidLauncher;
import edu.coms.sr2.game.interfacing.Activity;

public abstract class NativeActivity extends android.app.Activity implements Activity
{
    public static final String TAG = NativeActivity.class.getSimpleName();
    protected boolean isActive = false;
    @Persistent
    protected boolean isPersistent = false;
    @Persistent
    protected ArrayList<Listener> listeners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityPersistenceManager.restore(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
        for(Listener l: listeners)
            l.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
        for(Listener l: listeners)
            l.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        for(Listener l: listeners)
            l.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        for(Listener l: listeners)
            l.onPause();
    }

    @Override
    public void start() {
        Application.log("MyTag", "Start Native Activity");
        GameLauncher.setActive(false);
        isPersistent = true;
        AndroidLauncher.getInstance().startActivity(
                ActivityPersistenceManager.store(this));
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void stop() {
        super.finish();
        ActivityPersistenceManager.remove(getIntent());
    }

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }
}
