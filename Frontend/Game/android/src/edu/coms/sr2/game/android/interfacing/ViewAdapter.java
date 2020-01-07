package edu.coms.sr2.game.android.interfacing;

import android.text.Html;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.HashMap;

import edu.coms.sr2.game.android.AndroidLauncher;
import edu.coms.sr2.game.graphics.ScreenPosition;
import edu.coms.sr2.game.interfacing.views.Button;
import edu.coms.sr2.game.interfacing.views.Layout;
import edu.coms.sr2.game.interfacing.views.Spinner;
import edu.coms.sr2.game.interfacing.views.TextEntry;
import edu.coms.sr2.game.interfacing.views.TextView;
import edu.coms.sr2.game.interfacing.views.View;

public class ViewAdapter {
    private static HashMap<View, android.view.View> genericToAndroidMap = new HashMap<>();

    public static android.view.View getAndroidView(View view) {
        return genericToAndroidMap.get(view);
    }

    public static int getGravity(ScreenPosition position)
    {
        switch(position)
        {
            case TOP_LEFT:
                return Gravity.TOP|Gravity.LEFT;
            case TOP:
                return Gravity.TOP;
            case TOP_RIGHT:
                return Gravity.TOP|Gravity.RIGHT;
            case CENTER_LEFT:
                return Gravity.CENTER_HORIZONTAL|Gravity.LEFT;
            case CENTER:
                return Gravity.CENTER;
            case CENTER_RIGHT:
                return Gravity.CENTER_HORIZONTAL|Gravity.RIGHT;
            case BOTTOM_LEFT:
                return Gravity.BOTTOM|Gravity.LEFT;
            case BOTTOM:
                return Gravity.BOTTOM;
            case BOTTOM_RIGHT:
                return Gravity.BOTTOM|Gravity.RIGHT;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
    }

    public static View toGenericView(android.view.View view) {
        if(view instanceof android.widget.Button)
            return toGenericButton((android.widget.Button) view);
        else if(view instanceof android.widget.EditText)
            return toGenericTextEntry((android.widget.EditText) view);
        else if(view instanceof android.widget.TextView)
            return toGenericTextView((android.widget.TextView) view);
        else if(view instanceof RelativeLayout)
            return toGenericLayout((RelativeLayout) view);
        else if(view instanceof ProgressBar)
            return toGenericSpinner((ProgressBar) view);
        else
            return new ViewIMPL(view);
    }

    public static class ViewIMPL implements View {
        protected android.view.View view;
        public ViewIMPL(android.view.View view) {
            this.view = view;
            genericToAndroidMap.put(this, view);
        }

        @Override
        public int getId() {
            return view.getId();
        }

        @Override
        public int getHeight() {
            return view.getHeight();
        }

        @Override
        public int getWidth() {
            return view.getWidth();
        }

//        @Override
//        public int getPadding() {
//            return view.getPaddingTop();
//        }

        @Override
        public boolean isVisible() {
            return view.getVisibility() == android.view.View.VISIBLE;
        }

        @Override
        public View setHeight(int height) {
            AndroidLauncher.getInstance().runOnUiThread(()->
                    view.getLayoutParams().height = height);
            return this;
        }

        @Override
        public View setWidth(int width) {
            AndroidLauncher.getInstance().runOnUiThread(()->{
                if(view instanceof android.widget.Button)
                    ((android.widget.Button) view).setWidth(width);
                else
                    view.getLayoutParams().width = width;
            });
            return this;
        }

        @Override
        public View setPadding(int left, int top, int right, int bottom) {
            AndroidLauncher.getInstance().runOnUiThread(()->
                    view.setPadding(left, top, right, bottom));
            return this;
        }

        @Override
        public View setGravity(ScreenPosition pos) {
            AndroidLauncher.getInstance().runOnUiThread(()->{
                ViewGroup.LayoutParams params = view.getLayoutParams();
                if(params instanceof LinearLayout.LayoutParams)
                    ((LinearLayout.LayoutParams) params).gravity = getGravity(pos);
                else if(params instanceof RelativeLayout.LayoutParams)
                    for(int verb : getLayoutGravityVerbs(pos))
                        ((RelativeLayout.LayoutParams) params).addRule(verb);
            });
            return this;
        }

        @Override
        public View setVisible(boolean val) {
            AndroidLauncher.getInstance().runOnUiThread(()->
                    view.setVisibility(val? android.view.View.VISIBLE : android.view.View.INVISIBLE));
            return this;
        }
    }

    public static int getLayoutVerb(Layout.Relation relation) {
        switch (relation) {
            case ABOVE:
                return RelativeLayout.ABOVE;
            case BELOW:
                return RelativeLayout.BELOW;
            case LEFT_OF:
                return RelativeLayout.LEFT_OF;
            case RIGHT_OF:
                return RelativeLayout.RIGHT_OF;
            default:
                throw new IllegalStateException("Unexpected value: " + relation);
        }
    }

    public static int[] getLayoutGravityVerbs(ScreenPosition pos) {
        if(pos == ScreenPosition.CENTER)
            return new int[]{ RelativeLayout.CENTER_VERTICAL, RelativeLayout.CENTER_HORIZONTAL };
        else
            return new int[]{ getLayoutGravityVerb(pos) };
    }

    public static int getLayoutGravityVerb(ScreenPosition pos) {
        switch(pos) {
            case TOP_LEFT:
                return RelativeLayout.ALIGN_PARENT_TOP | RelativeLayout.ALIGN_PARENT_LEFT;
            case TOP:
                return RelativeLayout.ALIGN_PARENT_TOP;
            case TOP_RIGHT:
                return RelativeLayout.ALIGN_PARENT_TOP | RelativeLayout.ALIGN_PARENT_RIGHT;
            case CENTER_LEFT:
                return RelativeLayout.ALIGN_PARENT_LEFT;
            case CENTER:
                throw new IllegalStateException("Unsupported: " + ScreenPosition.CENTER.toString());
            case CENTER_RIGHT:
                return RelativeLayout.ALIGN_PARENT_RIGHT;
            case BOTTOM_LEFT:
                return RelativeLayout.ALIGN_PARENT_BOTTOM | RelativeLayout.ALIGN_PARENT_LEFT;
            case BOTTOM:
                return RelativeLayout.ALIGN_PARENT_BOTTOM;
            case BOTTOM_RIGHT:
                return RelativeLayout.ALIGN_PARENT_BOTTOM | RelativeLayout.ALIGN_PARENT_RIGHT;
            default:
                throw new IllegalStateException("Unexpected value: " + pos);
        }
    }

    private static class TextViewIMPL extends ViewIMPL implements TextView {
        android.widget.TextView textView;
        protected TextViewIMPL(android.widget.TextView view) {
            super(view);
            this.textView = view;
        }
        @Override
        public TextView setText(String text) {
            AndroidLauncher.getInstance().runOnUiThread(()->
                    textView.setText(text));
            return this;
        }

        @Override
        public String getText() {
            return textView.getText().toString();
        }

        @Override
        public TextView setTextFromHTML(String html) {
            AndroidLauncher.getInstance().runOnUiThread(()->
                    textView.setText(Html.fromHtml(html)));
            return this;
        }
    }

    public static Button toGenericButton(android.widget.Button button) {
        class ButtonIMPL extends TextViewIMPL implements Button {
            ButtonIMPL() { super(button); }
            @Override
            public void onClick(Runnable r) {
                button.setOnClickListener(view->r.run());
            }

            @Override
            public boolean isClickable() {
                return button.isClickable();
            }

            @Override
            public Button setClickable(boolean val) {
                AndroidLauncher.getInstance().runOnUiThread(()->
                        button.setClickable(val));
                return this;
            }
        }
        return new ButtonIMPL();
    }

    public static TextEntry toGenericTextEntry(android.widget.EditText textEdit) {
        class TextEntryIMPL extends ViewIMPL implements TextEntry {
            TextEntryIMPL() { super(textEdit); }
            @Override
            public String getText() {
                return textEdit.getText().toString();
            }
        }
        return new TextEntryIMPL();
    }

    public static TextView toGenericTextView(android.widget.TextView textView) {
        return new TextViewIMPL(textView);
    }

    public static Spinner toGenericSpinner(android.widget.ProgressBar spinner) {
        class SpinnerIMPL extends ViewIMPL implements Spinner {
            SpinnerIMPL() { super(spinner); }
        }
        return new SpinnerIMPL();
    }

//
//    public static int toOrientation(Layout.Grouping grouping) {
//        switch(grouping) {
//            case VERTICAL:
//                return LinearLayout.VERTICAL;
//            case HORIZONTAL:
//                return LinearLayout.HORIZONTAL;
//            default:
//                throw new IllegalStateException("Unexpected value: " + grouping);
//        }
//    }
//
//    public static Layout.Grouping toGrouping(int orientation) {
//        for(Layout.Grouping grouping : Layout.Grouping.values())
//            if(orientation == toOrientation(grouping))
//                return grouping;
//        return null;
//    }

    public static Layout toGenericLayout(RelativeLayout layout) {
        class LayoutIMPL extends ViewIMPL implements Layout {
            LayoutIMPL() { super(layout); }
            @Override
            public Layout addView(View view) {
                AndroidLauncher.getInstance().runOnUiThread(()->
                        layout.addView(getAndroidView(view)));
                return this;
            }

            private RelativeLayout.LayoutParams getDefaultRelativeParams() {
                return new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            }

            @Override
            public Layout addView(View view, Relation relation, View otherView) {
                RelativeLayout.LayoutParams params = getDefaultRelativeParams();
                AndroidLauncher.getInstance().runOnUiThread(()->{
                    params.addRule(getLayoutVerb(relation), otherView.getId());
                    layout.addView(getAndroidView(view), params);
                });

                return this;
            }

            @Override
            public Layout removeView(View view) {
                AndroidLauncher.getInstance().runOnUiThread(()->
                        layout.removeView(getAndroidView(view)));
                return this;
            }

            @Override
            public Layout clear() {
                AndroidLauncher.getInstance().runOnUiThread(()->
                        layout.removeAllViews());
                return this;
            }

            @Override
            public int getHeight() {
                return layout.getHeight();
            }

            @Override
            public int getWidth() {
                return layout.getWidth();
            }

            @Override
            public View setHeight(int height) {
                layout.setMinimumHeight(height);
                return this;
            }

            @Override
            public View setWidth(int width) {
                layout.setMinimumWidth(width);
                return this;
            }
        }
        return new LayoutIMPL();
    }
}
