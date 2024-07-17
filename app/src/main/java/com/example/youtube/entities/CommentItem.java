package com.example.youtube.entities;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.youtube.repositories.UserRepository;

@Entity
public class CommentItem {

    String text;
    @PrimaryKey
    public int commentId;
    public int videoId;
    public int userId;
    private static int nextId = 0;


    public Uri profileImage;

    public CommentItem(){

    }

    public CommentItem(Video video, String text) {
        this.commentId = nextId++;
        this.videoId = video.getId();
        this.text = text;
        this.userId = UserRepository.getInstance().getLoggedUser().getId();
        this.profileImage = Uri.parse(UserRepository.getInstance().getLoggedUser().getUserImgFile());
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        return profileImage;
    }

    public void onRemoveCommentClick() {

    }


}
