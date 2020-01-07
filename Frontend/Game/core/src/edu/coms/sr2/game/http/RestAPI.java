package edu.coms.sr2.game.http;

import java.util.function.Consumer;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.objects.pojo.Profile;
import edu.coms.sr2.game.utils.ThreadSafeFuture;

public class RestAPI {
    public static final String profilesEndpoint = "profiles";
    public static final String leaderboardEndpoint = "leaderboard";

    public static String getProfileEndpoint(int id) {
        return profilesEndpoint + "/" + id;
    }
    public static String getOnlineStatusEndpoint(int id) { return getProfileEndpoint(id) + "/online"; }
    public static String getLeaderboardEndpoint(int numProfiles) { return leaderboardEndpoint + "/" + numProfiles; }

    public static Consumer<Profile> getProfile(int id, Consumer<Profile> consumer) {
        return HttpUtils.GET(Profile.class, getProfileEndpoint(id), consumer);
    }
    public static ThreadSafeFuture<Profile> getProfile(int id) {
        return HttpUtils.GET(Profile.class, getProfileEndpoint(id));
    }
    public static Consumer<Profile> getProfile(Consumer<Profile> consumer) {
        return getProfile(Application.getProfileId(), consumer);
    }
    public static ThreadSafeFuture<Profile> getProfile() {
        return getProfile(Application.getProfileId());
    }
    public static Consumer<Boolean> getOnlineStatus(int playerId, Consumer<Boolean> consumer) {
        return HttpUtils.GET(Boolean.class, getOnlineStatusEndpoint(playerId), consumer);
    }
    public static ThreadSafeFuture<Boolean> getOnlineStatus(int playerId) {
        return HttpUtils.GET(Boolean.class, getOnlineStatusEndpoint(playerId));
    }

    public static String getPlayerGameEndpoint(int playerId) {
        return "games/player/" + playerId;
    }

    public static Consumer<Integer> getGameIdOfPlayer(int playerId, Consumer<Integer> consumer) {
        return HttpUtils.GET(Integer.class, getPlayerGameEndpoint(playerId), consumer);
    }
    public static ThreadSafeFuture<Integer> getGameIdOfPlayer(int playerId) {
        return HttpUtils.GET(Integer.class, getPlayerGameEndpoint(playerId));
    }

    public static Consumer<Profile[]> getLeaderboard(int numProfiles, Consumer<Profile[]> consumer) {
        return HttpUtils.GET(Profile[].class, getLeaderboardEndpoint(numProfiles), consumer);
    }

    public static ThreadSafeFuture<Profile[]> getLeaderboard(int numProfiles) {
        return HttpUtils.GET(Profile[].class, getLeaderboardEndpoint(numProfiles));
    }

    public static void updateProfile(Profile newProfile) {
        HttpUtils.POST("profiles/" + Application.getProfileId(), newProfile);
    }
}
