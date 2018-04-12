package com.example.travelmalaysia.model;

import java.io.Serializable;

public class User implements Serializable {
    private int userid;
    private String username;
    private double wallet;

    public User(int userid, String username, double wallet) {
        this.userid = userid;
        this.username = username;
        this.wallet = wallet;
    }

    public User(User user) {
        this.userid = user.userid;
        this.username = user.username;
        this.wallet = user.wallet;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }
}
