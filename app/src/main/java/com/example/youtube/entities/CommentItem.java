package com.example.youtube.entities;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.youtube.repositories.UserRepository;

@Entity
public class CommentItem {
   
    @PrimaryKey
    public int commentId;
    public int videoId;
    public int userId;
    public String content;
    private static int nextId = 0;

    public CommentItem(){

    }


    public CommentItem(Video video, String text) {
        this.commentId = nextId++;
        this.videoId = video.getId();
        this.content = text;
        this.userId = UserRepository.getInstance().getLoggedUser().getId();
    }

    public String getText() {
        return content;
    }

    public void setText(String text) {
        this.content = text;
    }

    public int getCommentId() {
        return this.commentId;
    }

    public int getVideoId() {
        return this.videoId;
    }

    public String getUserName() {
        return UserRepository.getInstance().findUserById(this.userId).getDisplayName();
    }

    public Uri getProfileImage() {
        return Uri.parse(UserRepository.getInstance().findUserById(this.userId).getUserImgFile());
    }

    public void onRemoveCommentClick() {

    }


}
