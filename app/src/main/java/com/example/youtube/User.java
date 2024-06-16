package com.example.youtube;

import android.graphics.Bitmap;

public class User {
    private static int nextId = 0;// Static variable to keep track of the next ID
    private int id;
    private String userName;
    private String password;
    private String displayName;
    private Bitmap profileImage;

    public User(String userName, String password, String displayName, Bitmap profileImage){
        this.id = nextId++; // Assign the next ID and then increment it
        this.userName = userName;
        this.password = password;
        this.displayName = displayName;
        this.profileImage = profileImage;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }
}
