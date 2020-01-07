package edu.coms.sr2.game.interfacing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.graphics.ScreenPosition;
import edu.coms.sr2.game.graphics.Toaster;
import edu.coms.sr2.game.interfacing.views.Button;
import edu.coms.sr2.game.interfacing.views.View;

public abstract class GenericScreen implements Activity.Listener
{
    public static final String TAG = GenericScreen.class.getSimpleName();

    protected Activity activity;
    protected Toaster toaster;
    protected Consumer<Runnable> uiRunner;
    protected BiFunction<Class<? extends View>, String, View> retriever;

    public void setActivity(Activity activity) {
        this.activity = activity;
        activity.addListener(this);
    }

    public void setToaster(Toaster toaster) { this.toaster = toaster; }
    public void setUiRunner(Consumer<Runnable> uiRunner) { this.uiRunner = uiRunner; }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface ButtonMethod {
        String value() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface LinkView {
        String value();
    }

    public void toast(String text) {
        toaster.toast(text);
    }

    public void toast(String text, ScreenPosition pos) {
        toaster.toast(text, pos);
    }

    @Override
    public void onStart() {}

    @Override
    public void onResume() {}

    @Override
    public void onStop() {}

    @Override
    public void onPause() {}

    public void stop() {
        activity.stop();
    }

    public void runOnUiThread(Runnable r) {
        uiRunner.accept(r);
    }
    
    public boolean isActive() {
        return activity != null && activity.isActive();
    }

    public void link(BiFunction<Class<? extends View>, String, View> retriever) {
        this.retriever = retriever;
        for(Field f: this.getClass().getDeclaredFields()) {
            if(View.class.isAssignableFrom(f.getType())) {
                f.setAccessible(true);

                View val = retriever.apply((Class<? extends View>) f.getType(), f.isAnnotationPresent(LinkView.class)?
                        f.getAnnotation(LinkView.class).value() :
                        f.getName());

                if(val != null) {
                    try {
                        f.set(this, val);
                    } catch (IllegalAccessException e) {
                        Application.error(TAG, e);
                    }
                }
                else
                    Application.error(TAG, "Retreiver returned null for field: " + f.getName());
            }
        }

        for(Method m : this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(ButtonMethod.class)) {
                String buttonName = m.getAnnotation(ButtonMethod.class).value();

                ((Button)retriever.apply(Button.class, buttonName.isEmpty()? m.getName() : buttonName)).onClick(() -> {
                    try {
                        m.invoke(this);
                    } catch (Exception e) {
                        Application.error(TAG, e);
                    }
                });
            }
        }
    }

}
