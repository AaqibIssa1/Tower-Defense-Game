package edu.coms.sr2.game.screens.social;

import java.util.ArrayList;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.Threading;
import edu.coms.sr2.game.http.RestAPI;
import edu.coms.sr2.game.objects.pojo.Profile;
import edu.coms.sr2.game.utils.ThreadSafeFuture;

public class RecentPlayersScreen extends FriendAddingScreen {
    private static RecentPlayersScreen instance = new RecentPlayersScreen();
    public static RecentPlayersScreen getInstance() { return instance; }
    public static final int numRecentPlayers = 10;

    protected RecentPlayersScreen() { super(numRecentPlayers); }

    @Override
    public void onResume() {
        Threading.runAsync(()->showRecentPlayers());
    }

    private void showRecentPlayers() {
        Profile selfProfile = RestAPI.getProfile().get();
        Profile[] recentPlayers = getProfiles(selfProfile.getRecentPlayerIds());
        updateViews(recentPlayers,
                (index, isFriend)-> {
                    Profile profile = recentPlayers[index];
                    return getUsernameString(profile.getUserName(), isFriend, profile.getId() == Application.getProfileId());
                }, ()->showRecentPlayers());
    }

    private Profile[] getProfiles(int[] ids) {
        Profile[] profiles = new Profile[ids.length];
        ArrayList<ThreadSafeFuture<Profile>> futures = new ArrayList<>(ids.length);
        for(int i = 0; i < ids.length; ++i)
            futures.add(RestAPI.getProfile(ids[i]));
        for(int i = 0; i < ids.length; ++i)
            profiles[i] = futures.get(i).get();
        return profiles;
    }
}
