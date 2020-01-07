package edu.coms.sr2.game.graphics;

public interface Toaster {
    void toast(String text, ScreenPosition position);

    default void toast(String text) { toast(text, ScreenPosition.CENTER); }
}
