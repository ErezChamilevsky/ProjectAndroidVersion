package com.example.youtube.utilities;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.youtube.R;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.UserRepository;

public class VideoItem extends AppCompatActivity {
    private Uri thumbnail;
    private String title;
    private Uri avatar;
    private String uploaderName;
    private String uploadDate;
    private int views;
    private int id;

    public VideoItem(int id, Uri thumbnail, String title, Uri avatar, String uploaderName, String uploadDate, int views) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.avatar = avatar;
        this.uploaderName = uploaderName;
        this.uploadDate = uploadDate;
        this.views = views;
    }


    public VideoItem(Video video){
        this.thumbnail = video.getImg();
        this.title = video.getTitle();;
        this.uploaderName = video.getUserName();
        this.uploadDate = video.getPublicationDate();
        this.views = 25;
        this.id = video.getId();

    }

    public Uri getThumbnail() {

        return thumbnail;
    }

    public String getItemTitle() {
        return title;
    }

    public String getUploaderName() {
        return uploaderName;
    }
    public String getUploadDate() {
        return uploadDate;
    }

    public Uri getAvatar(){
        return this.avatar;
    }
    public int getViews(){
        return this.views;
    }

    public int getId(){
        return this.id;
    }

    public void setUserProfileImage(int userId){
        User user = UserRepository.getInstance().findUserById(userId);
        if(user == null){
            this.avatar = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.def);
        } else {
            this.avatar = user.getProfileImage();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.video_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}