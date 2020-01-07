package edu.coms.sr2.game.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GraphicsUtils {
    public static void clear(float r, float g, float b, float a) {
        Gdx.gl.glClearColor(r, g, b, a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public static void clear() {
        clear(1, 1, 1, 1);
    }
}
