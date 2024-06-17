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

//      init of the user and videos dataBase.
        if(firstInit.getInstance().isInit() == 0){

            UserRepository.getInstance().loadUsersFromJson(this, "users.json");
            VideoRepository.getInstance().loadVideosFromJson(this, "videos.json");
            for (int i = 0; i < 9; i++) {
                VideoItem video = new VideoItem(VideoRepository.getInstance().getVideos().get(i));
                videoItemArrayList.add(video);
                video.setUserProfileImage(VideoRepository.getInstance().getVideos().get(i).getUserId());
            }

            firstInit.getInstance().setInited();

        }

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

        //user button to login page
        onUserButtonClick();
        onClickOfLogOut();



        //updating when ths is new intent only
        updateUserImage(UserRepository.getInstance().getLoggedUser());


        if(UserRepository.getInstance().getLoggedUser() != null){
            onAddNewVideoClick();
        }

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
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
        VideoItemsRecyclerViewAdapter adapter = new VideoItemsRecyclerViewAdapter(this, this.videoItemArrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            userButton.setImageResource(R.drawable.def);
        }
    }

    public void onUserButtonClick(){
        findViewById(R.id.homepage_user_button).setOnClickListener(v ->{
            Intent goToLogin = new Intent(this, LoginScreen.class);
            startActivity(goToLogin);
        });
    }

    public void onAddNewVideoClick(){
        findViewById(R.id.homepage_add_button).setOnClickListener(v ->{
            Intent goToLogin = new Intent(this, AddNewVideoScreen.class);
            startActivity(goToLogin);
        });
    }

    public void onClickOfLogOut(){
        findViewById(R.id.logOutButton).setOnClickListener(v ->{
            UserRepository.getInstance().setLoggedUser(null);
            Intent stayInHomePage = new Intent(this, Homepage.class);
            startActivity(stayInHomePage);
        });
    }


}