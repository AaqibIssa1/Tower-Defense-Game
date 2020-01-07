package edu.coms.sr2.game.interfacing.views;

import edu.coms.sr2.game.graphics.ScreenPosition;

public interface View {
    int getId();
    int getHeight();
    int getWidth();
    //int getPadding();
    boolean isVisible();
    View setHeight(int height);
    View setWidth(int width);
    View setPadding(int left, int top, int right, int bottom);
    View setGravity(ScreenPosition pos);
    View setVisible(boolean val);

    default View setPadding(int padding) {
        return setPadding(padding, padding, padding, padding);
    }
}
