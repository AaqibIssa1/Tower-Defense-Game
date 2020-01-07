package edu.coms.sr2.game.android;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;

import edu.coms.sr2.game.Application;

public class ActivityPersistenceManager {
    public static final String TAG = ActivityPersistenceManager.class.getSimpleName();
    public static final String intentIdString = "intentId";
    private static HashMap<Integer, HashMap<String, Object>> mapsPerIntent = new HashMap<>();
    private static int intentIdGen = 1;

    public static Intent makeIntentWithId(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(intentIdString, intentIdGen++);
        return intent;
    }

    public static int getId(Intent intent) {
        int id = intent.getIntExtra(intentIdString, -1);
        if(id != -1)
            return id;
        else
            throw new RuntimeException("Intent does not have id.");
    }

    private static HashMap<String, Object> get(Intent intent) {
        return mapsPerIntent.computeIfAbsent(getId(intent), k->new HashMap<>());
    }

    public static Object put(Intent intent, String key, Object val) {
        return get(intent).put(key, val);
    }

    public static void remove(Intent intent) {
        mapsPerIntent.remove(intent);
    }

    public static Object get(Intent intent, String key) {
        return get(intent).get(key);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Persistent { }

    public static Intent store(android.app.Activity thiz) {
        Intent intent = makeIntentWithId(AndroidLauncher.getInstance().getContext(), thiz.getClass());
        for(Class clazz = thiz.getClass(); clazz != android.app.Activity.class; clazz = clazz.getSuperclass())
            for(Field f: clazz.getDeclaredFields())
                if(f.isAnnotationPresent(Persistent.class)) {
                    f.setAccessible(true);
                    try {
                        put(intent, f.getName(), f.get(thiz));
                    } catch (IllegalAccessException e) {
                        Application.error(TAG, e);
                    }
                }
        return intent;
    }

    public static void restore(android.app.Activity thiz) {
        for(Class clazz = thiz.getClass(); clazz != android.app.Activity.class; clazz = clazz.getSuperclass())
            for(Field f: clazz.getDeclaredFields())
                if(f.isAnnotationPresent(Persistent.class)) {
                    f.setAccessible(true);
                    try {
                        Object val = get(thiz.getIntent(), f.getName());
                        if(val == null) {
                            Log.d(TAG, "Null nonsense on \"" + f.getName() + "\" with Intent id " + getId(thiz.getIntent()) + ": " + get(thiz.getIntent()));
                        }
                        else
                            f.set(thiz, val);
                    } catch (IllegalAccessException e) {
                        Application.error(TAG, e);
                    }
                }
    }

}
