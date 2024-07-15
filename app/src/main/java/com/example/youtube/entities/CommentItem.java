package com.example.youtube.entities;

import android.net.Uri;

import com.example.youtube.repositories.UserRepository;

public class CommentItem {


    int videoId;
    int userId;
    String content;
    int commentId;

    private static int nextId = 0;

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
