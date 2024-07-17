package com.example.youtube.userPage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.youtube.R;
import com.example.youtube.ViewModels.MyApplication;
import com.example.youtube.adapters.VideoItemsRecyclerViewAdapter;
import com.example.youtube.api.VideoAPI;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.VideoRepository;
import com.example.youtube.utilities.RecyclerViewInterface;
import com.example.youtube.utilities.VideoItem;
import com.example.youtube.watchingPage.WatchingPage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserVideosPage extends AppCompatActivity implements RecyclerViewInterface {


    private static final String TAG = "UserVideosActivity";

    private RecyclerView recyclerView;
    private ArrayList<VideoItem> videoItemArrayList = new ArrayList<>();
    ;
    private ImageView imageViewAvatar;
    private TextView textViewUsername;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_videos_page);

        int userId = getIntent().getIntExtra("USER_ID", -1);
        Log.d(TAG, "user ID" + userId);
        if (userId == -1) {
            Log.e(TAG, "No valid user ID provided");
            finish(); // Close the activity if no valid user ID
            return;
        }

        getUserFromServer(userId);
    }


    void getUserFromServer(int userId) {

        VideoAPI videoAPI = new VideoAPI();
        videoAPI.getUserDetailsForVideoPage(userId, new Callback<User>() {

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
            this.user = response.body();
            getVideosFromServer();
            Log.d(TAG, "user Success: " + user);
        } else {
            Log.d(TAG, "user Response Code: " + response.code());
            Log.d(TAG, "user Response Message: " + response.message());
        }
    }

    private void handleUserFailure(Throwable t) {
        Log.e(TAG, "user Request Failed: " + t.getMessage());

    }


    void getVideosFromServer() {

        VideoAPI videoAPI = new VideoAPI();
        Log.d(TAG, "getVideoFromServer in userPage");
        videoAPI.getVideoListByUserId(user.getId(), new Callback<List<Video>>() {

            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                Log.d(TAG, "getVideoFromServer in watching1");
                handleVideoResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.d(TAG, "getVideoFromServer in watching2");
                handleVideoFailure(t);
            }
        });

    }

    private void handleVideoResponse(Call<List<Video>> call, Response<List<Video>> response) {
        if (response.isSuccessful()) {

            List<Video> videos = response.body();
            videoItemArrayList.clear();
            videoItemArrayList.addAll(VideoRepository.getInstance().addVideoListToItemList(videos));

            Log.d(TAG, "Success: " + videos);
            setOther();

            Log.d(TAG, "Success: " + videos);
        } else {
            Log.d(TAG, "Response Code: " + response.code());
            Log.d(TAG, "Response Message: " + response.message());
        }
    }


    private void handleVideoFailure(Throwable t) {
        Log.e(TAG, "Request Failed: " + t.getMessage());

    }


    public void setOther() {
        initializeViews();

        setupVideoList();
        setupUserInfo();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewVideos);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewUsername = findViewById(R.id.textViewUsername);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User Videos");
        }
    }


    private void setupUserInfo() {
        if (user != null) {
            File imgFile = new File(user.getUserImgFile());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageViewAvatar.setImageBitmap(myBitmap);
            } else {
                // Set a default image if the file doesn't exist
                imageViewAvatar.setImageResource(R.drawable.def);
            }
            textViewUsername.setText(user.getDisplayName());
        }
    }


    private void setupVideoList() {
        VideoItemsRecyclerViewAdapter adapter = new VideoItemsRecyclerViewAdapter(this, videoItemArrayList, this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MyApplication.context, WatchingPage.class);
        intent.putExtra("VIDEO_ID", videoItemArrayList.get(position).getId());
        startActivity(intent);
    }
}