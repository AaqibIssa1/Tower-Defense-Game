package edu.coms.sr2.game.objects.gdx;

import java.util.function.Supplier;

import edu.coms.sr2.game.GameLauncher;

public class GdxContextObject<T> implements GameLauncher.ContextListener {
    private T val;
    private boolean isLoaded = false;
    private Supplier<T> supplier;

    public GdxContextObject(Supplier<T> supplier) {
        this.supplier = supplier;
        GameLauncher.addContextListener(this);
    }

    public T get() {
        if(!isLoaded)
            load();
        return val;
    }

    private void load() {
        val = supplier.get();
        isLoaded = true;
    }

    @Override
    public void reload() {
        isLoaded = false;
    }
}
