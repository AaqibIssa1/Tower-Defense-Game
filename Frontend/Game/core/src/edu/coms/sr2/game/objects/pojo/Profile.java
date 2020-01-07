package edu.coms.sr2.game.objects.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Profile
{
    private int id;
    private String userName;
    private String realName;
    private int currentRank;
    private int highestRank;
    private int lowestRank;
    private int wins;
    private int losses;
    @SerializedName("friendListIds")
    private ArrayList<Integer> friendIds;
    private int[] recentPlayerIds;

    public Profile() {}

    public Profile(String userName, String realName) {
        this.userName = userName;
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "Profile #" + id + ":\n" + realName + " (\"" + userName + "\")";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(int currentRank) {
        this.currentRank = currentRank;
    }

    public int getHighestRank() {
        return highestRank;
    }

    public void setHighestRank(int highestRank) {
        this.highestRank = highestRank;
    }

    public int getLowestRank() {
        return lowestRank;
    }

    public void setLowestRank(int lowestRank) {
        this.lowestRank = lowestRank;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public ArrayList<Integer> getFriendIds() {
        return friendIds;
    }

    public void setFriendIds(ArrayList<Integer> friendIds) {
        this.friendIds = friendIds;
    }

    public int[] getRecentPlayerIds() {
        return recentPlayerIds;
    }

    public void setRecentPlayerIds(int[] recentPlayerIds) {
        this.recentPlayerIds = recentPlayerIds;
    }
}
