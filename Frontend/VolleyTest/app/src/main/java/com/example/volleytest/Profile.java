package com.example.volleytest;

import androidx.annotation.NonNull;

public class Profile
{
    private int id;
    private String userName;
    private String password;
    private String email;
    private String realName;
    private int currentRank;
    private int highestRank;
    private int lowestRank;
    private int wins;
    private int losses;

    @NonNull
    @Override
    public String toString() {
        return realName + "'s profile";
    }
}
