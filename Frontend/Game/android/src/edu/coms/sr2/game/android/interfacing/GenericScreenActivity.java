package edu.coms.sr2.game.android.interfacing;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.function.BiFunction;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.android.ActivityPersistenceManager.Persistent;
import edu.coms.sr2.game.interfacing.GenericScreen;
import edu.coms.sr2.game.interfacing.views.View;
import edu.coms.sr2.game.utils.StringUtils;

public class GenericScreenActivity extends NativeActivity
{
    public enum Orientation {
        PORTRAIT{
            @Override
            public int value() { return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT; }
        },
        LANDSCAPE {
            @Override
            public int value() { return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; }
        };
        public abstract int value();
    }

    @Persistent
    protected int viewId;
    @Persistent
    protected GenericScreen screen;
    @Persistent
    protected Orientation orientation;

    //For android activity service
    public GenericScreenActivity() {}

    public GenericScreenActivity(int viewId, GenericScreen screen, Orientation orientation) {
        this.viewId = viewId;
        this.screen = screen;
        this.orientation = orientation;
    }

    public GenericScreenActivity(int viewId, GenericScreen screen) {
        this(viewId, screen, Orientation.PORTRAIT);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isPersistent) {
            if (viewId != -1 && viewId != 0)
                setContentView(viewId);
            setRequestedOrientation(orientation.value());
            linkScreen();
        }
    }

    public BiFunction<Class<? extends View>, String, View> getViewRetriever() {
        return (clazz, fieldName) -> {
            String viewName = StringUtils.toSnakeCase(fieldName);
            int id = getResources().getIdentifier(viewName, "id", getPackageName());
            if (id == 0)
                throw new RuntimeException("Resource not found: \"" + viewName + "\"");
            android.view.View view;
            while (true) {
                view = findViewById(id);
                 if (view != null)
                    return ViewAdapter.toGenericView(view);
                else
                    Application.error(TAG, "findViewById returned null for \"" + viewName + "\": id was " + id);
                }
        };
    }

    public void linkScreen() {
        Application.log(TAG, "Linking view id " + viewId + " to generic screen " + screen.getClass().getSimpleName());

        screen.setActivity(this);
        screen.setToaster(AndroidToaster.getInstance());
        screen.setUiRunner(this::runOnUiThread);
        screen.link(getViewRetriever());
        screen.onStart();
    }

}
