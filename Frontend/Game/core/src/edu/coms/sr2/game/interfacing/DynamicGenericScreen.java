package edu.coms.sr2.game.interfacing;

import edu.coms.sr2.game.interfacing.views.View;

public abstract class DynamicGenericScreen extends GenericScreen {
    public interface DynamicLayout {
        <T extends View> T addView(T view);
        void removeView(View view);
        void clear();
    }
    public interface ViewFactory {
        <T extends View> T make(Class<T> clazz);
    }

    protected DynamicLayout dynamicLayout;
    protected ViewFactory viewFactory;


    public void setDynamicLayout(DynamicLayout dynamicLayout) {
        this.dynamicLayout = dynamicLayout;
    }

    public void setViewFactory(ViewFactory factory) {
        this.viewFactory = factory;
    }

    public <T extends View> T makeView(Class<T> clazz) {
        return viewFactory.make(clazz);
    }

    public <T extends View> T addView(T view) {
        return dynamicLayout.addView(view);
    }
    public void removeView(View view) {
        dynamicLayout.removeView(view);
    }
    public void clearViews() {
        dynamicLayout.clear();
    }

}
