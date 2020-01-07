package edu.coms.sr2.game.screens.social;

import java.util.function.BiFunction;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.http.RestAPI;
import edu.coms.sr2.game.interfacing.GenericScreen;
import edu.coms.sr2.game.interfacing.views.Button;
import edu.coms.sr2.game.interfacing.views.TextView;
import edu.coms.sr2.game.objects.pojo.Profile;
import edu.coms.sr2.game.utils.HtmlBuilder;

public abstract class FriendAddingScreen extends GenericScreen {
    protected int numRows;
    protected ViewRow[] rows;

    protected FriendAddingScreen(int numRows) {
        this.numRows = numRows;
        this.rows = new ViewRow[numRows];
    }

    protected static class ViewRow {
        TextView text;
        Button button;

        public ViewRow(TextView text, Button button) {
            this.text = text;
            this.button = button;
        }
    }

    @Override
    public void onStart() {
        for(int i = 0; i < numRows; ++i) {
            rows[i] = new ViewRow(
                    (TextView) retriever.apply(TextView.class, getTextViewName(i)),
                    (Button) retriever.apply(Button.class, getButtonName(i)));
            rows[i].text.setVisible(false);
            rows[i].button.setVisible(false);
            rows[i].button.setClickable(false);
        }
    }

    protected String getViewPrefix(int num) {
        return "rank" + (num + 1) + "_";
    }

    protected String getTextViewName(int num) {
        return getViewPrefix(num) + "text";
    }

    protected String getButtonName(int num) {
        return getViewPrefix(num) + "friend_button";
    }

    protected String getUsernameColor(boolean isFriend, boolean isSelf) {
        if(isSelf)
            return "cf38f5";
        else
            return isFriend? "22e3e3" : "f58142";
    }

    protected String getUsernameString(String username, boolean isFriend, boolean isSelf) {
        return new HtmlBuilder(username).addItalics()
                .addColor(getUsernameColor(isFriend, isSelf)).build();
    }

    protected void updateViews(Profile[] profiles,
                               BiFunction<Integer, Boolean, String> htmlGenerator, /*index, isFriend -> ???*/
                               Runnable extraOnClick) {
        updateViews(RestAPI.getProfile().get(), profiles, htmlGenerator, extraOnClick);
    }

    protected void updateViews(Profile selfProfile, Profile[] profiles,
                               BiFunction<Integer, Boolean, String> htmlGenerator, /*index, isFriend -> ???*/
                               Runnable extraOnClick) {
        for(int i = 0; i < profiles.length && i < numRows; ++i){
            Profile profile = profiles[i];
            TextView text = rows[i].text;
            Button button = rows[i].button;

            boolean isFriend = selfProfile.getFriendIds().contains(profile.getId());

            text.setTextFromHTML("   " + htmlGenerator.apply(i, isFriend));
            text.setVisible(true);

            if(!isFriend && profile.getId() != Application.getProfileId()) {
                button.onClick(()-> Threading.runAsync(()->{
                    selfProfile.getFriendIds().add(profile.getId());
                    RestAPI.updateProfile(selfProfile);
                    extraOnClick.run();
                }));
                button.setClickable(true);
                button.setVisible(true);
            }
            else {
                button.setClickable(false);
                button.setVisible(false);
            }
        }
    }


}
