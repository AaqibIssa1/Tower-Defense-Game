package edu.coms.sr2.game.android.interfacing;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;

import java.util.function.BiFunction;

import edu.coms.sr2.game.android.ActivityPersistenceManager.Persistent;
import edu.coms.sr2.game.android.AndroidLauncher;
import edu.coms.sr2.game.interfacing.DynamicGenericScreen;
import edu.coms.sr2.game.interfacing.views.View;

public class DynamicScreenActivity extends GenericScreenActivity implements DynamicGenericScreen.DynamicLayout {
    @Persistent
    DynamicGenericScreen dynamicScreen;
    ScrollView scrollView;
    LinearLayout linearLayout;

    //For android activity service
    public DynamicScreenActivity() {}

    public DynamicScreenActivity(DynamicGenericScreen screen, Orientation orientation) {
        super(-1, screen, orientation);
        this.dynamicScreen = screen;
    }

    public DynamicScreenActivity(DynamicGenericScreen screen) {
        this(screen, Orientation.PORTRAIT);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        scrollView = new ScrollView(this);
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);

        super.onCreate(savedInstanceState);
        setContentView(scrollView);
    }

    @Override
    public BiFunction<Class<? extends View>, String, View> getViewRetriever() {
        return (clazz, fieldName) -> dynamicScreen.makeView(clazz);
    }

    @Override
    public void linkScreen() {
        dynamicScreen.setDynamicLayout(this);
        dynamicScreen.setViewFactory(new ViewFactory(this));
        dynamicScreen.onStart();
        //super.linkScreen();
    }

    @Override
    public <T extends View> T addView(T view) {
        AndroidLauncher.getInstance().runOnUiThread(()->
                linearLayout.addView(ViewAdapter.getAndroidView(view)));
        return view;
    }

    @Override
    public void removeView(View view) {
        AndroidLauncher.getInstance().runOnUiThread(()->
                linearLayout.removeView(ViewAdapter.getAndroidView(view)));
    }

    @Override
    public void clear() {
        AndroidLauncher.getInstance().runOnUiThread(()->
                linearLayout.removeAllViews());
    }
}
