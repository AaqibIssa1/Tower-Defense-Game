package edu.coms.sr2.game.interfacing;

public interface ActivityManager
{
    Activity getActivity(Activities activity);
    default void startActivity(Activities activity) { getActivity(activity).start(); }
}
