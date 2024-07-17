package com.example.youtube.watchingPage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.youtube.R;
import com.example.youtube.adapters.VideoItemsRecyclerViewAdapter;
import com.example.youtube.api.VideoAPI;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;
import com.example.youtube.utilities.RecyclerViewInterface;
import com.example.youtube.utilities.VideoItem;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchingPage extends AppCompatActivity implements RecyclerViewInterface {

    private static final String TAG = "WatchingPage";

    // UI elements
    private RecyclerView recyclerView;
    private RecyclerView commentsRecyclerView;

    // Data
    private Video video;
    private ArrayList<VideoItem> videoItemArrayList;

    private CommentsManager commentsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching_page);

        // Adjust padding based on system insets
        adjustSystemInsets();

        // Retrieve video ID from intent
        int vidId = getIntent().getIntExtra("VIDEO_ID", -1);
        //getting video details from server
        getVideoFromServer(vidId);
//        video = VideoRepository.getInstance().findVideoById(vidId);

        //adding to get from server videoItems -
        getVideosItemListFromServer();


        if (video == null) {
            // Handle the case where video is not found
        }
        // Initialize UI components - moving all the functions here to be called from the getVideoItemListFromServer
        //because of asynchronicity

    }

    void getVideoFromServer(int videoId) {

        VideoAPI videoAPI = new VideoAPI();
        Log.d(TAG, "getVideoFromServer in watching");
        videoAPI.getVideo(videoId,new Callback<Video>() {

            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                Log.d(TAG, "getVideoFromServer in watching1");
                handleVideoResponse(call, response);
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Log.d(TAG, "getVideoFromServer in watching2");
                handleVideoFailure(t);
            }
        });

    }

    private void handleVideoResponse(Call<Video> call, Response<Video> response) {
        if (response.isSuccessful()) {
            Video video = response.body();
            setVideo(video);
            Log.d(TAG, "Success: " + video);
        } else {
            Log.d(TAG, "Response Code: " + response.code());
            Log.d(TAG, "Response Message: " + response.message());
        }
    }

    private void setVideo(Video video) {
        Log.d(TAG, "setVideo " + video);
        this.video = video;
    }

    private void handleVideoFailure(Throwable t) {
        Log.e(TAG, "Request Failed: " + t.getMessage());

    }


    void getVideosItemListFromServer() {

        VideoAPI videoAPI = new VideoAPI();
        videoAPI.getVideosToPresent(new Callback<List<Video>>() {

            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                Log.d(TAG, "List Response");
                handleResponse(call, response);
                initializeRecyclerViews();
                setupOtherVideos();
                setupVideoFragment();
                setupUserInteractions();
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.d(TAG, "List fail");
                handleFailure(t);
            }
        });

    }

    private void handleResponse(Call<List<Video>> call, Response<List<Video>> response) {
        if (response.isSuccessful()) {
            List<Video> videos = response.body();
//            VideoRepository.getInstance().addVideoListToItemList(videos); //because there are no change in the list it should be the same
            this.videoItemArrayList = VideoRepository.getInstance().getVideoItemArrayList();
            Log.d(TAG, "List Success: " + videos);
        } else {
            Log.d(TAG, "List Response Code: " + response.code());
            Log.d(TAG, "List Response Message: " + response.message());
        }
    }

    private void handleFailure(Throwable t) {
        Log.e(TAG, "List Request Failed: " + t.getMessage());

    }

    // Adjust padding based on system insets
    private void adjustSystemInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Extract individual insets as integers
            int systemBarLeft = insets.getInsets(WindowInsetsCompat.Type.systemBars()).left;
            int systemBarTop = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int systemBarRight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).right;
            int systemBarBottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

            // Set padding using extracted insets
            v.setPadding(systemBarLeft, systemBarTop, systemBarRight, systemBarBottom);
            return insets.consumeSystemWindowInsets(); // Consume insets to prevent further propagation
        });
    }

    // Initialize RecyclerViews for videos and comments
    private void initializeRecyclerViews() {
        recyclerView = findViewById(R.id.otherVideosLibrary);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
    }

    // Setup list of other videos in RecyclerView
    private void setupOtherVideos() {
        recyclerView.setAdapter(new VideoItemsRecyclerViewAdapter(this, videoItemArrayList, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Setup video fragment
    private void setupVideoFragment() {
        VideoDisplay fragment = VideoDisplay.newInstance(video.getVideoSrc().toString(), null);
        setVideoFragment(fragment);
        setVideoInPage(video);
        setUserCard(video);
    }

    // Set up user interactions (like, dislike, comment)
    private void setupUserInteractions() {
        setupLikeDislikeButtons();
        commentsManager = new CommentsManager(this, video, commentsRecyclerView);
    }

    // Set up like and dislike buttons
    private void setupLikeDislikeButtons() {
        Button likeButton = findViewById(R.id.likeButton);
        Button unLikeButton = findViewById(R.id.unLikeButton);

        likeButton.setOnClickListener(v -> {
            video.likeButtonClicked();
            setLikes(video);
        });

        unLikeButton.setOnClickListener(v -> {
            video.unLikeButtonClicked();
            setLikes(video);
        });

        setLikes(video); // Initial setup
    }

    // Set video fragment in the activity
    private void setVideoFragment(VideoDisplay fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.videoFragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    // Set video details in the UI
    private void setVideoInPage(Video video) {
        setTitle(video);
        setDescription(video);
        setDateAndViews(video);
    }

    // Set title of the video
    private void setTitle(Video video) {
        TextView title = findViewById(R.id.videoTitle);
        title.setText(video.getTitle());
    }

    // Set description of the video
    private void setDescription(Video video) {
        TextView text = findViewById(R.id.descriptionSection);
        text.setText(video.getDescription());
    }

    // Set date and views of the video
    private void setDateAndViews(Video video) {
        TextView text = findViewById(R.id.uploadDate);
        text.setText(video.getViews() + " views  Uploaded on " + video.getPublicationDate());
    }

    // Set likes count of the video
    private void setLikes(Video video) {
        Button likeButton = findViewById(R.id.likeButton);
        likeButton.setText(video.getLikes() + " likes");
        Button unLikeButton = findViewById(R.id.unLikeButton);
        unLikeButton.setText("dislike");
    }

    // Set user card (uploader details) in the UI
    private void setUserCard(Video video) {
        TextView uploaderName = findViewById(R.id.uploaderName);
        ImageView uploaderAvatar = findViewById(R.id.uploaderAvatar);

        User user = UserRepository.getInstance().findUserById(video.getUserId());
        if (user != null) {
            uploaderName.setText(user.getDisplayName());
            uploaderAvatar.setImageURI(Uri.parse(user.getUserImgFile()));
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(WatchingPage.this, WatchingPage.class);
        intent.putExtra("VIDEO_ID", videoItemArrayList.get(position).getId());
        startActivity(intent);
    }
}
