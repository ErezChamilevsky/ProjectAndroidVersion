package com.example.youtube.VideoScreen.Comments;

import android.net.Uri;

import com.example.youtube.User;
import com.example.youtube.UserRepository;
import com.example.youtube.Video;

public class CommentItem {

    String text;
    int commentId;
    int videoId;
    int userId;
    private static int nextId = 0;


    Uri profileImage;


    public CommentItem(Video video, String text){
        this.commentId  = nextId++;
        this.videoId = video.getId();
        this.text = text;
        this.userId = UserRepository.getInstance().getLoggedUser().getId();
        this.profileImage = UserRepository.getInstance().getLoggedUser().getProfileImage();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCommentId(){
        return this.commentId;
    }
public int getVideoId(){
        return this.videoId;
}

public String getUserName(){
        return UserRepository.getInstance().findUserById(this.userId).getDisplayName();
}

    public Uri getProfileImage() {
        return profileImage;
    }


}
