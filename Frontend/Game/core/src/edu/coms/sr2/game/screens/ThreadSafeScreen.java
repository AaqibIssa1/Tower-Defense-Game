package edu.coms.sr2.game.screens;

import com.badlogic.gdx.Screen;

import edu.coms.sr2.game.Threading;

public abstract class ThreadSafeScreen implements Screen {
    protected ThreadSafeScreen() {
        Threading.assertGlThread("Screen class may depend on GL. Unsafe constructor call from non-GL thread.");
    }
}
