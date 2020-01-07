package edu.coms.sr2.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;

import edu.coms.sr2.game.graphics.Toast;
import edu.coms.sr2.game.interfacing.Activities;
import edu.coms.sr2.game.interfacing.Activity;
import edu.coms.sr2.game.interfacing.ActivityManager;
import edu.coms.sr2.game.sockets.SocketController;


public class Application {
    public static final String TAG = Application.class.getSimpleName();
    public static final Gson gson = new Gson();
    public static Toast.ToastFactory toastFactory;

    private static boolean created = false;
    private static ApplicationType applicationType;
    private static ActivityManager activityManager;
    private static Activities currentActivity;

    public static void create(ApplicationType applicationType, ActivityManager activityManager) {
        if(created)
            throw new RuntimeException("Application.create() called twice!");

        Application.applicationType = applicationType;
        Application.activityManager = activityManager;
        Threading.setUiThread(new ThreadSetterKey());

        //TODO Potential danger of several threads running this at same time...
        Threading.runAsync(()->{
            while(Gdx.gl == null) {/*spin*/}
            log(TAG, "Gdx gl initialized.");

            SocketController.init();
            toastFactory = new Toast.ToastFactory.Builder().font(Constants.defaultFont).build();
            created = true;
        });
    }

    private static int profileId = -1;
    public static void setProfileId(int id) { profileId = id; }
    public static int getProfileId() { return profileId; }

    public static void waitForInit() { while(!created) {/*spin*/} }

    public static Activity getActivity(Activities activity) {
        return activityManager.getActivity(activity);
    }
    public static void startActivity(Activities activity) {
        Threading.runAsync(()->{
            activityManager.startActivity(activity);
            currentActivity = activity;
        });
    }

    public static Activities getCurrentActivity() {
        return currentActivity;
    }

    public static ApplicationType getApplicationType() {
        return applicationType;
    }

    public static boolean isAndroid() {
        return applicationType == ApplicationType.Android;
    }

    public static void log(String tag, String message) { Gdx.app.log(tag, message); }
    public static void error(String tag, String message) {
        Gdx.app.error(tag, message);
    }
    public static void error(String tag, Throwable t) {
        Gdx.app.error(tag, t.getMessage(), t);
    }

    public static class ThreadSetterKey { private ThreadSetterKey() {} }
}