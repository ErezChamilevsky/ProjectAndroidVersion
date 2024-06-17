package com.example.youtube;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.EditText;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.VideoScreen.Comments.CommentItem;
import com.example.youtube.VideoScreen.Comments.CommentRepository;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView recyclerView;

    private ArrayList<VideoItem> videoItemArrayList = VideoRepository.getInstance().getVideoItemArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

//---------------------

//        init of the user and videos dataBase.
        UserRepository.getInstance().loadUsersFromJson(this, "users.json");
        VideoRepository.getInstance().loadVideosFromJson(this, "videos.json");

        for (int i = 0; i < 9; i++) {
            VideoItem video = new VideoItem(VideoRepository.getInstance().getVideos().get(i));
            videoItemArrayList.add(video);
            video.setUserProfileImage(VideoRepository.getInstance().getVideos().get(i).getUserId());
        }


        UserRepository.getInstance().setLoggedUser(UserRepository.getInstance().findUserById(1));


//        -----------------------
        ImageButton homeButton = findViewById(R.id.homepage_home_button);
        ImageButton subButton = findViewById(R.id.homepage_sub_button);
        ImageButton userButton = findViewById(R.id.homepage_user_button);
        ImageButton searchButton = findViewById(R.id.homepage_search_button);

        final EditText searchBar = findViewById(R.id.homepage_search_bar);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchBar.getVisibility() == View.GONE) {
                    searchBar.setVisibility(View.VISIBLE); // Show the search bar if it is hidden
                }
                searchBar.requestFocus(); // Focus the search bar
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT); // Show the keyboard
            }
        });

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    performSearch(searchBar.getText().toString());

                    String txt = searchBar.getText().toString();
                    Toast.makeText(getApplicationContext(),txt , Toast.LENGTH_SHORT).show();
                    searchBar.setVisibility(View.GONE);

                    //search bar works
                    return true;
                }
                return false;
            }
        });


        //recycleview starts

        recyclerView = findViewById(R.id.homepage_otherVideosLibrary);
//        for (int i = 0; i < 9; i++) {
//            VideoItem videoItem = new VideoItem(i,
//                    Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.chadlogo),
//                    "title",
//                    Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.chadlogo),
//                    "Erez", "20.20.2020",
//                    32);
//
//            this.videoItemArrayList.add(videoItem);
//        }
        VideoItemsRecyclerViewAdapter adapter = new VideoItemsRecyclerViewAdapter(this, this.videoItemArrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //recycleview ends

        // Reference the button

        // Set a click listener on the button

        homeButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when the ImageButton is clicked
                Intent intent = new Intent(Homepage.this, Homepage.class);
                finish();
                startActivity(intent);
            }
        }));

    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(Homepage.this, WatchingPage.class);
        // Pass data to the new activity
        intent.putExtra("VIDEO_ID", videoItemArrayList.get(position).getId());
        // Add more extras if needed
        startActivity(intent);
    }

    public void updateUserImage(User user) {
        ImageButton userButton = findViewById(R.id.homepage_user_button);

        if (user != null && user.getProfileImage() != null) {
            userButton.setImageURI(user.getProfileImage());
        } else {
            userButton.setImageResource(R.drawable.chadlogo);
        }
    }


}