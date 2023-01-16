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
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getKills() {
        return Kills;
    }

    public void setKills(int kills) {
        Kills = kills;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    private int Kills=0;
    private int money=0;

    private User(){

    }

    public String getPfpUri() {
        return pfpUri;
    }

    public void setPfpUri(String pfpUri) {
        this.pfpUri = pfpUri;
    }

    private String pfpUri;




}
