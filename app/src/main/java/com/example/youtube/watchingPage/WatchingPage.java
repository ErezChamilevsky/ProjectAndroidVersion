package com.example.youtube.watchingPage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.adapters.VideoItemsRecyclerViewAdapter;
import com.example.youtube.entities.CommentItem;
import com.example.youtube.repositories.CommentRepository;
import com.example.youtube.adapters.CommentsRecycleViewAdapter;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;
import com.example.youtube.utilities.RecyclerViewInterface;
import com.example.youtube.utilities.VideoItem;

import java.util.ArrayList;

public class WatchingPage extends AppCompatActivity implements RecyclerViewInterface {

    private static final String TAG = "WatchingPage";

    // UI elements
    private RecyclerView recyclerView;
    private RecyclerView commentsRecyclerView;

    // Data
    private Video video;
    private ArrayList<VideoItem> videoItemArrayList = VideoRepository.getInstance().getVideoItemArrayList();
    private ArrayList<CommentItem> commentsList;

    private CommentsRecycleViewAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching_page);

        // Adjust padding based on system insets
        adjustSystemInsets();

        // Retrieve video ID from intent
        int vidId = getIntent().getIntExtra("VIDEO_ID", -1);
        video = VideoRepository.getInstance().findVideoById(vidId);

        if (video == null) {
            // Handle the case where video is not found
            finish();
            return;
        }

        // Initialize UI components
        initializeRecyclerViews();
        setupOtherVideos();
        setupVideoFragment();
        setupUserInteractions();
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
//        for (int i = 0; i < 9; i++) {
//            videoItemArrayList.add(new VideoItem(VideoRepository.getInstance().getVideos().get(i)));
//        }

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
        setupCommentSection();
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

    // Set up comments section
    private void setupCommentSection() {
        TextView commentsSection = findViewById(R.id.commentsSection);
        if(UserRepository.getInstance().getLoggedUser() != null) {


            EditText newCommentBox = findViewById(R.id.newCommentBox);
            Button postCommentButton = findViewById(R.id.commentButton);
            postCommentButton.setText("Send");

            commentsSection.setOnClickListener(v -> toggleCommentsSection());

            postCommentButton.setOnClickListener(v -> {
                String text = newCommentBox.getText().toString();
                if (!text.isEmpty()) {
                    // Add comment to repository
                    CommentItem comment = new CommentItem(video, text);
                    CommentRepository.getInstance().addComment(comment);
                    this.commentsList.add(comment);
                    // Clear the comment box
                    newCommentBox.setText("");

                    // Update comments list and notify adapter
                    commentAdapter.notifyDataSetChanged(); // or notifyItemInserted() if you want to animate insertion
                }
            });
        }

        setCommentsList(); // Initial load
        setupCommentsRecyclerView();
    }

    // Toggle visibility of comments section
    private void toggleCommentsSection() {
        View commentsContainer = findViewById(R.id.commentsContainer);
        commentsContainer.setVisibility(commentsContainer.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    // Set comments list from repository
    private void setCommentsList() {
        commentsList = CommentRepository.getInstance().findCommentByVideoId(video.getId());
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

    private void setupCommentsRecyclerView() {
        this.commentAdapter = new CommentsRecycleViewAdapter(this, commentsList);

        commentsRecyclerView.setAdapter(commentAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
