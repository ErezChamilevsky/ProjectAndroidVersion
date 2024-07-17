package com.example.youtube.entities;

import static android.content.ContentValues.TAG;
import static com.example.youtube.ViewModels.MyApplication.context;

import android.net.Uri;
import android.util.Log;

import com.example.youtube.watchingPage.WatchingPage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Video {
    private static int nextId = 0;// Static variable to keep track of the next ID
    private int id;
    private String displayName;
    private int userId;
    private String img; //changing filed to string
    private String videoSrc; //changing filed to string
    private String title;
    private String publicationDate;
    private int views;
    private  String description;

    private int likes;

    private static int likeFlag;
    //constructor.
    public Video(String userName, int userId, Uri img, Uri videoSrc, String title, String publicationDate, String description){
        this.id  = nextId++;
        this.displayName = userName;
        this.userId = userId;
        this.img = img.toString(); //changing filed to string, but we want the constructor still be relevant
        this.videoSrc = videoSrc.toString();
        this.title = title;
        this.publicationDate = publicationDate;
        this.description = description;
        this.views = 0;
        this.likes = 0;
        likeFlag =0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return displayName;
    }

    public void setUserName(String userName) {
        this.displayName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Uri getImg() { //convert to uri
        return Uri.parse("android.resource://" +
                context.getPackageName() + "/"
                +context.getResources().getIdentifier(img, "drawable", context.getPackageName()));
    }

    public Uri getVideoSrc() { //convert to uri

        return Uri.parse("android.resource://" +
                context.getPackageName() + "/"
                +context.getResources().getIdentifier(videoSrc, "raw", context.getPackageName()));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void likeButtonClicked(){
        if(likeFlag == 0){
            this.likes++;
            likeFlag = 1;
        }
        if(likeFlag == -1){
            this.likes++;
            likeFlag = 0;
        }
    }


    public void unLikeButtonClicked() {
        if(likeFlag == 1){
            this.likes--;
            likeFlag = 0;
        }
        if(likeFlag == 0){
            this.likes--;
            likeFlag = -1;
        }
    }





}