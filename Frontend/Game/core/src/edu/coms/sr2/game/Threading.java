package edu.coms.sr2.game;

import com.badlogic.gdx.Gdx;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import edu.coms.sr2.game.exceptions.BlockedMainThreadException;
import edu.coms.sr2.game.exceptions.GlThreadException;
import edu.coms.sr2.game.exceptions.LooperThreadException;
import edu.coms.sr2.game.utils.ThreadSafeFuture;

public class Threading {
    private static Thread uiThread;
    private static Thread glThread;

    public static void setUiThread(Application.ThreadSetterKey key) {
        Objects.requireNonNull(key);
        uiThread = Thread.currentThread();
    }

    public static void setGlThread(GameLauncher.ThreadSetterKey key) {
        Objects.requireNonNull(key);
        glThread = Thread.currentThread();
    }

    public static boolean currentThreadIsUiThread() {
        return Thread.currentThread() == uiThread;
    }

    public static Thread getUiThread() {
        return uiThread;
    }

    public static Thread getGlThread() {
        return glThread;
    }

    public static void assertLooperThread(String message) {
        if(Application.isAndroid() && !currentThreadIsUiThread())
            throw new LooperThreadException(message);
    }
    public static void assertWorkerThread() {
        if(Application.isAndroid() && currentThreadIsUiThread())
            throw new BlockedMainThreadException();
    }

    public static void assertGlThread(String message) {
        if(Thread.currentThread() != glThread)
            throw new GlThreadException(message);
    }

    public static void runAsync(Runnable task) {
        new Thread(task).start();
    }

    public static <T> T supplyFromGlThread(Supplier<T> supplier) {
        if(Thread.currentThread() != glThread) {
            CompletableFuture<T> future = new CompletableFuture<>();
            Gdx.app.postRunnable(() -> future.complete(supplier.get()));
            return new ThreadSafeFuture<T>(future).get();
        }
        else
            return supplier.get();
    }
}
