package edu.coms.sr2.objects;

import androidx.annotation.NonNull;

public class Profile
{
    public int id;
    public String userName;
    public String realName;
    public int currentRank;
    public int highestRank;
    public int lowestRank;
    public int wins;
    public int losses;

    public Profile() {}

    public Profile(String userName) {
        this.userName = userName;
    }

    @NonNull
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
}
