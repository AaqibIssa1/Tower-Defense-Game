package edu.coms.sr2.game.interfacing.views;

public interface Layout extends View {
//    enum Grouping {
//        VERTICAL, HORIZONTAL
//    }
    enum Relation {
        ABOVE, BELOW, LEFT_OF, RIGHT_OF
    }
    //Grouping getGrouping();
    //Layout setGrouping(Grouping grouping);
    Layout addView(View view);
    Layout addView(View view, Relation relation, View otherView);
    Layout removeView(View view);
    Layout clear();
}
