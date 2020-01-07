package edu.coms.sr2.game.utils;

import com.badlogic.gdx.math.Rectangle;

public class HelperFunctions {

    public static boolean pointInRect(float x, float y, float rectX, float rectY, float rectWidth, float rectHeight) {

        if(x < rectX + rectWidth &&
            x > rectX &&
            y < rectY + rectHeight &&
            y > rectY) {
            return true;
        }
        return false;
    }

    public static boolean rectInRect(float rect1X, float rect1Y, float rect1Wdith, float rect1Height, float rect2X, float rect2Y, float rect2Width, float rect2Height) {
        if(rect1X < rect2X + rect2Width &&
            rect1X + rect1Wdith > rect2X &&
            rect1Y < rect2Y + rect2Height &&
            rect1Y + rect1Height > rect2Y) {
            return true;
        }
        return false;
    }

}
