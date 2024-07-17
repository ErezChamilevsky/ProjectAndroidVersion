package com.example.youtube.utilities;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.youtube.R;
import com.example.youtube.api.VideoAPI;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.UserRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    public VideoItem(Video video) {
        this.thumbnail = video.getImg();
        this.title = video.getTitle();
        ;
        this.uploaderName = video.getUserName();
        this.uploadDate = video.getPublicationDate();
        this.views = video.getViews();
        this.id = video.getId();
        getUserFromServer();

    }

    void getUserFromServer() {

        VideoAPI videoAPI = new VideoAPI();
        videoAPI.getUserDetailsForVideoPage(this.id, new Callback<User>() {

            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                Log.d(TAG, "user Response");
                handleUserResponse(call, response);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "user fail");
                handleUserFailure(t);
            }
        });

    }

    private void handleUserResponse(Call<User> call, Response<User> response) {
        if (response.isSuccessful()) {
            User user = response.body();

            this.uploaderName = user.getDisplayName();
            this.avatar = Uri.parse(user.getUserImgFile());


            Log.d(TAG, "user Success: " + user);
        } else {
            Log.d(TAG, "user Response Code: " + response.code());
            Log.d(TAG, "user Response Message: " + response.message());
        }
    }

    private void handleUserFailure(Throwable t) {
        Log.e(TAG, "user Request Failed: " + t.getMessage());

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

    public Uri getAvatar() {
        return this.avatar;
    }

    public int getViews() {
        return this.views;
    }

    public int getId() {
        return this.id;
    }

    public void setUserProfileImage(int userId) {
        User user = UserRepository.getInstance().findUserById(userId);
        if(user == null){
            this.avatar = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.def);
        } else {
            this.avatar = Uri.parse(user.getUserImgFile());
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