package edu.coms.sr2.game.android.interfacing;

import android.content.Context;

import edu.coms.sr2.game.interfacing.DynamicGenericScreen;
import edu.coms.sr2.game.interfacing.views.Button;
import edu.coms.sr2.game.interfacing.views.Layout;
import edu.coms.sr2.game.interfacing.views.Spinner;
import edu.coms.sr2.game.interfacing.views.TextEntry;
import edu.coms.sr2.game.interfacing.views.TextView;
import edu.coms.sr2.game.interfacing.views.View;

public class ViewFactory implements DynamicGenericScreen.ViewFactory {
    public static android.view.View make(Class<? extends View> clazz, Context context) {
        return label(construct(clazz, context));
    }

    private static android.view.View construct(Class<? extends View> clazz, Context context) {
        if(clazz == Button.class)
            return new android.widget.Button(context);
        else if(clazz == TextEntry.class)
            return new android.widget.EditText(context);
        else if(clazz == TextView.class)
            return new android.widget.TextView(context);
        else if(clazz == Layout.class)
            return new android.widget.RelativeLayout(context);
        else if(clazz == Spinner.class)
            return new android.widget.ProgressBar(context);
        else
            throw new IllegalStateException("Unsupported interface/class: " + clazz.toString());
    }

    private static android.view.View label(android.view.View view) {
        if (view.getId() == -1)
            view.setId(android.view.View.generateViewId());
        return view;
    }

    private Context context;
    public ViewFactory(Context context) {
        this.context = context;
    }

    public <T extends View> T make(Class<T> clazz) {
        return (T) ViewAdapter.toGenericView(make(clazz, context));
    }
}
