package com.example.youtube.entities;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Video {
    private static int nextId = 0;// Static variable to keep track of the next ID
    @PrimaryKey
    private int id;
    private String displayName;
    private int userId;
    private Uri img;

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Video.nextId = nextId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(int likeFlag) {
        this.likeFlag = likeFlag;
    }

    private Uri videoSrc;
    private String title;
    private String publicationDate;
    private  String description;
    private int views;
    private int likes;

    private int likeFlag;
    //constructor.
    public Video(String displayName, int userId, Uri img, Uri videoSrc, String title, String publicationDate, String description){
        this.id  = nextId++;
        this.displayName = displayName;
        this.userId = userId;
        this.img = img;
        this.videoSrc = videoSrc;
        this.title = title;
        this.publicationDate = publicationDate;
        this.description = description;
        this.views = 0;
        this.likes = 0;
        this.likeFlag =0;
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

    public Uri getImg() {
        return img;
    }

    public void setImg(Uri img) {
        this.img = img;
    }

    public Uri getVideoSrc() {
        return videoSrc;
    }

    public void setVideoSrc(Uri videoSrc) {
        this.videoSrc = videoSrc;
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
        if(this.likeFlag == 0){
            this.likes++;
            this.likeFlag = 1;
        }
        if(this.likeFlag == -1){
            this.likes++;
            this.likeFlag = 0;
        }
    }


    public void unLikeButtonClicked() {
        if(this.likeFlag == 1){
            this.likes--;
            this.likeFlag = 0;
        }
        if(this.likeFlag == 0){
            this.likes--;
            this.likeFlag = -1;
        }
    }
}