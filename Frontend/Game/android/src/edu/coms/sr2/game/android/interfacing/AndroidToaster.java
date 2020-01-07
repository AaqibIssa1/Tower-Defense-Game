package edu.coms.sr2.game.android.interfacing;

import android.widget.Toast;

import edu.coms.sr2.game.android.AndroidLauncher;
import edu.coms.sr2.game.graphics.ScreenPosition;
import edu.coms.sr2.game.graphics.Toaster;

public class AndroidToaster implements Toaster {
    private static AndroidToaster instance = new AndroidToaster();
    public static AndroidToaster getInstance() { return instance; }

    @Override
    public void toast(String text, ScreenPosition position) {
        AndroidLauncher.getInstance().runOnUiThread(()->{
            Toast toast = Toast.makeText(AndroidLauncher.getInstance().getContext(), text, Toast.LENGTH_LONG);
            toast.setGravity(ViewAdapter.getGravity(position), 0, 0);
            toast.show();
        });
    }
}
