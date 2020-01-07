package edu.coms.sr2.game.interfacing;

public interface Activity {
    void start();
    boolean isActive();
    void stop();

    interface Listener {
        void onStart();
        void onResume();
        void onStop();
        void onPause();
    }

    void addListener(Listener listener);
}
