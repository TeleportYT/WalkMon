package com.vik.test;

import android.net.Uri;

public class User {

    public User(String username, int kills, int money, String pfpUri) {
        this.username = username;
        this.Kills = kills;
        this.money = money;
        this.pfpUri = pfpUri;
    }

    private String username;

    public String getUsername() {
        return this.username;
    }


    private int Kills=0;
    private int money=0;

    private User(){

    }

    public String getPfpUri() {
        return pfpUri;
    }


    private String pfpUri;




}
