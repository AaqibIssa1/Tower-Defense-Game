package edu.coms.sr2.game.screens.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.graphics.ScreenPosition;
import edu.coms.sr2.game.http.RestAPI;
import edu.coms.sr2.game.interfacing.Activities;
import edu.coms.sr2.game.interfacing.DynamicGenericScreen;
import edu.coms.sr2.game.interfacing.views.Button;
import edu.coms.sr2.game.interfacing.views.Layout;
import edu.coms.sr2.game.interfacing.views.Spinner;
import edu.coms.sr2.game.interfacing.views.TextView;
import edu.coms.sr2.game.interfacing.views.View;
import edu.coms.sr2.game.objects.pojo.Profile;
import edu.coms.sr2.game.screens.LobbyScreen;
import edu.coms.sr2.game.screens.game.SpectateScreen;
import edu.coms.sr2.game.sockets.SocketController;
import edu.coms.sr2.game.sockets.controllers.CommandController;
import edu.coms.sr2.game.utils.HtmlBuilder;

public class FriendListScreen extends DynamicGenericScreen {
    protected FriendListScreen() {}
    private static FriendListScreen instance = new FriendListScreen();
    public static FriendListScreen getInstance() {
        return instance;
    }

    private HashSet<Integer> outGoingInvites = new HashSet<>();
    private HashSet<Integer> incomingInvites = new HashSet<>();

    private static final String REMOVE_FRIEND_HTML = new HtmlBuilder("Remove Friend").addItalics().addColor("FF0000").build();
    private static final String SPECTATE_HTML = new HtmlBuilder("SPECTATE").addBold().addColor("00a318").build();
    private static final String SEND_INVITE_HTML = new HtmlBuilder("Invite to Game").addBold().addColor("5c008a").build();
    public static final String CANCEL_INVITE_HTML = new HtmlBuilder("Cancel Game Invite").addItalics().addColor("FF0000").build();
    public static final String ACCEPT_INVITE_HTML = new HtmlBuilder("ACCEPT INVITE").addItalics().addBold().addColor("00a318").build();

    public void clearOutgoingInvites() {
        for(Integer inviteId : (Iterable<Integer>) outGoingInvites.clone())
            cancelInvite(inviteId);
    }
    public void clearIncomingInvites() {
        incomingInvites.clear();
    }

    public void sendInvite(int id) {
        outGoingInvites.add(id);
        SocketController.trySend("sendMatchInvite", id);
    }

    public void cancelInvite(int id) {
        outGoingInvites.remove(id);
        SocketController.trySend("cancelMatchInvite", id);
    }

    private static class FriendInfo {
        Profile profile;
        Boolean isOnline;
        Integer gameId;
    }
    private static class FriendView {
        Layout combined;
        TextView nameAndUsername;
        TextView onlineStatus;
        Button removeFriendButton;
        Button spectateOrInviteButton;
        int profileId;

        void setNameAndUsername(String realName, String userName) {
            nameAndUsername.setTextFromHTML(realName + " (" + HtmlBuilder.addBold("AKA")
                    + " \"" + new HtmlBuilder(userName).addItalics().addColor("fa9e00").build() + "\")");
        }

        void setOnlineAndGameStatus(boolean isOnline, int gameId) {
            onlineStatus.setTextFromHTML(HtmlBuilder.addColor(
                    isOnline? "\tOnline" + (gameId != -1? " - In Game" : "") : "\tOffline",
                    isOnline? "60fa00" : "ff524f"));

            if(isOnline) {
                if (gameId != -1)
                    configureSpectateButton(gameId);
                else
                    configureInviteButton();

                checkForInvite();

                spectateOrInviteButton.setClickable(true);
                spectateOrInviteButton.setVisible(true);
            }
            else {
                if(getInstance().outGoingInvites.contains(profileId))
                    getInstance().cancelInvite(profileId);

                spectateOrInviteButton.setClickable(false);
                spectateOrInviteButton.setVisible(false);
            }
        }

        void configureSpectateButton(int gameId) {
            spectateOrInviteButton.setTextFromHTML(SPECTATE_HTML);
            spectateOrInviteButton.onClick(() -> Threading.runAsync(() -> {
                SpectateScreen.setGameId(gameId);
                SpectateScreen.setPlayerPerspective(profileId);
                Application.startActivity(Activities.SPECTATE);
            }));
        }

        void configureInviteButton() {
            if(getInstance().outGoingInvites.contains(profileId)) {
                onlineStatus.setTextFromHTML(
                        HtmlBuilder.addColor("\tOnline", "60fa00")
                                + new HtmlBuilder(" - Sent Game Invite...").addItalics().addItalics().addColor("FFFFFF").build());

                spectateOrInviteButton.setTextFromHTML(CANCEL_INVITE_HTML);
                spectateOrInviteButton.onClick(() -> Threading.runAsync(() -> {
                    getInstance().cancelInvite(profileId);
                    getInstance().update();
                }));
            }
            else {
                spectateOrInviteButton.setTextFromHTML(SEND_INVITE_HTML);
                spectateOrInviteButton.onClick(() -> Threading.runAsync(() -> {
                    getInstance().sendInvite(profileId);
                    LobbyScreen.getInstance().cancelMatchMaking();
                    getInstance().update();
                }));
            }
        }

        void checkForInvite() {
            if(getInstance().incomingInvites.contains(profileId)) {
                onlineStatus.setTextFromHTML(
                        HtmlBuilder.addColor("\tOnline", "60fa00")
                        + new HtmlBuilder(" - INCOMING GAME INVITE").addBold().addItalics().addColor("f5c542").build());

                spectateOrInviteButton.setTextFromHTML(ACCEPT_INVITE_HTML);
                spectateOrInviteButton.onClick(() -> Threading.runAsync(() -> {
                    getInstance().incomingInvites.remove(profileId);
                    SocketController.trySend("startNewGame", Application.getProfileId(), profileId);
                    getInstance().update();
                }));
            }
        }
    }

    private HashMap<Integer, FriendView> friendViews = new HashMap<>();
    private View spinner;
    private Timer refreshTimer;

    @Override
    public void onStart() {
        addSpinner();
        friendViews.clear();
        Threading.runAsync(()-> update());
        startRefreshing();
    }

    @Override
    public void onResume() {
        startRefreshing();
    }

    @Override
    public void onPause() {
        refreshTimer.cancel();
    }

    private void startRefreshing() {
        if(refreshTimer != null)
            refreshTimer.cancel();

        refreshTimer = new Timer();
        refreshTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 2500l, 2500l);
    }

    private void addSpinner() {
        Spinner spinner = makeView(Spinner.class);
        spinner.setGravity(ScreenPosition.CENTER);

        Layout layout = makeView(Layout.class);
        layout.setHeight(1800);
        layout.setWidth(1080);

        this.spinner = layout;
        addView(layout);
        layout.addView(spinner);
        spinner.setGravity(ScreenPosition.CENTER);
    }

    private void removeSpinner() {
        removeView(spinner);
    }

    private void update() {
        update(RestAPI.getProfile().get());
    }

    private void update(Profile selfProfile) {
        if(dynamicLayout == null)
            return;

        ArrayList<FriendInfo> friends = getFriendInformation(selfProfile);
        removeSpinner();
        for(FriendInfo friendInfo : friends) {
            FriendView view = friendViews.computeIfAbsent(friendInfo.profile.getId(),
                    profileId->{
                        FriendView newView = makeFriendView(profileId, selfProfile);
                        addView(newView.combined);
                        return newView;
                    });
            view.setNameAndUsername(friendInfo.profile.getRealName(), friendInfo.profile.getUserName());
            view.setOnlineAndGameStatus(friendInfo.isOnline, friendInfo.gameId);
        }

        for(Integer friendId : new ArrayList<>(friendViews.keySet()))
            if(!infoListContainsId(friends, friendId))
                removeView(friendViews.remove(friendId).combined);
    }

    private boolean infoListContainsId(ArrayList<FriendInfo> list, int id) {
        for(FriendInfo info : list)
            if(info.profile.getId() == id)
                return true;

        return false;
    }


    private ArrayList<FriendInfo> getFriendInformation(Profile selfProfile) {
        ArrayList<FriendInfo> friends = new ArrayList<>();

        for (int friendId : selfProfile.getFriendIds()) {
            FriendInfo info = new FriendInfo();
            friends.add(info);
            RestAPI.getProfile(friendId, profile->info.profile = profile);
            RestAPI.getOnlineStatus(friendId, isOnline->info.isOnline = isOnline);
            RestAPI.getGameIdOfPlayer(friendId, gameId->info.gameId = gameId);
        }

        return waitUntilNoNulls(friends);
    }

    private ArrayList<FriendInfo> waitUntilNoNulls(ArrayList<FriendInfo> list) {
        boolean foundNull;
        do {
            foundNull = false;
            for(FriendInfo info : list)
                if(info.gameId == null || info.isOnline == null || info.profile == null)
                    foundNull = true;

        } while(foundNull);
        return list;
    }

    private FriendView makeFriendView(int profileId, Profile selfProfile) {

        FriendView friendView = new FriendView();
        friendView.profileId = profileId;

        friendView.nameAndUsername = makeView(TextView.class);
        friendView.nameAndUsername.setPadding(20);

        friendView.removeFriendButton = makeView(Button.class);
        friendView.removeFriendButton .setTextFromHTML(REMOVE_FRIEND_HTML);
        friendView.removeFriendButton.onClick(()->Threading.runAsync(()->{
            selfProfile.getFriendIds().remove((Integer) profileId);
            RestAPI.updateProfile(selfProfile);
            update(selfProfile);
        }));

        Layout identifierAndRemoveButton = makeView(Layout.class);
        identifierAndRemoveButton.addView(friendView.nameAndUsername)
                .addView(friendView.removeFriendButton , Layout.Relation.RIGHT_OF, friendView.nameAndUsername);

        friendView.onlineStatus = makeView(TextView.class);
        friendView.onlineStatus.setPadding(30, 5, 5, 5);

        friendView.spectateOrInviteButton = makeView(Button.class);
        friendView.spectateOrInviteButton.setVisible(false);

        Layout onlineAndSpectateButton = makeView(Layout.class);
        onlineAndSpectateButton.addView(friendView.onlineStatus)
                .addView(friendView.spectateOrInviteButton, Layout.Relation.RIGHT_OF, friendView.onlineStatus);

        friendView.combined = makeView(Layout.class);
        friendView.combined.addView(identifierAndRemoveButton)
                .addView(onlineAndSpectateButton, Layout.Relation.BELOW, identifierAndRemoveButton);

        friendView.combined.setPadding(10, 20, 10, 20);

        return friendView;
    }

    public static class FriendListCommandController extends CommandController {
        @SocketCommand
        public void incomingMatchInvite(int profileId) {
            getInstance().incomingInvites.add(profileId);
            getInstance().update();
            RestAPI.getProfile(profileId, profile ->
                    LobbyScreen.getInstance().toast("Game invite from " + profile.getUserName(), ScreenPosition.TOP));
        }

        @SocketCommand
        public void cancelMatchInvite(int profileId) {
            getInstance().incomingInvites.remove(profileId);
            getInstance().update();
        }
    }

}
