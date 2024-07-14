package com.example.youtube.entities;

import android.net.Uri;

public class User {
    private static int nextId = 1;// Static variable to keep track of the next ID
    private String _id;
    private int userId = 1;
    private String userName;
    private String userPassword;

    private String userConfirmPassword;

    private String displayName;
    private String userImgFile;



    public User(String userName, String userPassword,String userConfirmPassword ,String displayName, String userImgFile){
        this._id = null;
        this.userId = nextId++; // Assign the next ID and then increment it
        this.userName = userName;
        this.userPassword = userPassword;
        this.userConfirmPassword = userConfirmPassword;
        this.displayName = displayName;
        this.userImgFile = userImgFile;


    }

    public int getId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getUserConfirmPassword() {
        return userConfirmPassword;
    }

    public void setUserConfirmPassword(String userConfirmPassword) {
        this.userConfirmPassword = userConfirmPassword;
    }

    public String getUserImgFile() {
        return userImgFile;
    }

    public void setId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUserImgFile(String userImgFile) {
        this.userImgFile = userImgFile;
    }
}
