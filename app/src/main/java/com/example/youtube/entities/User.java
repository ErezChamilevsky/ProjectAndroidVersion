package com.example.youtube.entities;

import android.net.Uri;

public class User {
    private static int nextId = 1;// Static variable to keep track of the next ID
    private int userId = 1;
    private String userName;
    private String password;
    private String displayName;
    private Uri userImgFile;

    public User(String userName, String password, String displayName, Uri profileImage){
        this.userId = nextId++; // Assign the next ID and then increment it
        this.userName = userName;
        this.password = password;
        this.displayName = displayName;
        this.userImgFile = profileImage;
    }

    public int getId() {
        return userId;
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

    public Uri getProfileImage() {
        return userImgFile;
    }

    public void setId(int id) {
        this.userId = id;
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

    public void setProfileImage(Uri profileImage) {
        this.userImgFile = profileImage;
    }
}
