package com.example.youtube.homePage;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.EditText;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.adapters.VideoItemsRecyclerViewAdapter;
import com.example.youtube.addNewVideoScreen.AddNewVideoScreen;
import com.example.youtube.api.VideoAPI;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.login.LoginScreen;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;
import com.example.youtube.utilities.RecyclerViewInterface;
import com.example.youtube.utilities.VideoItem;
import com.example.youtube.utilities.firstInit;
import com.example.youtube.watchingPage.WatchingPage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Homepage extends AppCompatActivity implements RecyclerViewInterface {
    private static final String TAG = "Home";
    private RecyclerView recyclerView;

    private ArrayList<VideoItem> videoItemArrayList = VideoRepository.getInstance().getVideoItemArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

//---------------------

//      init of the user and videos dataBase.
//        if(firstInit.getInstance().isInit() == 0){
//
//            //UserRepository.getInstance().loadUsersFromJson(this, "users.json");
//            VideoRepository.getInstance().loadVideosFromJson(this, "videos.json");
//            for (int i = 0; i < 9; i++) {
//                VideoItem video = new VideoItem(VideoRepository.getInstance().getVideos().get(i));
//                videoItemArrayList.add(video);
//                video.setUserProfileImage(VideoRepository.getInstance().getVideos().get(i).getUserId());
//            }
//
//            firstInit.getInstance().setInited();
//
//        }
        getVideosListFromServer();
    }
    public void settingThingsUp(){
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

        if (user != null && user.getUserImgFile() != null) {
            userButton.setImageURI(Uri.parse(user.getUserImgFile()));
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
            LoginScreen.token = null;
            Intent stayInHomePage = new Intent(this, Homepage.class);
            startActivity(stayInHomePage);
        });
    }


    void getVideosListFromServer() {

        VideoAPI videoAPI = new VideoAPI();
        videoAPI.getVideosToPresent(new Callback<List<Video>>() {

            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                Log.d(TAG, "List Response");
                handleResponse(call, response);
                settingThingsUp();

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
            VideoRepository.getInstance().addVideos(videos);
            Log.d(TAG, "Init Success: " + videos);
        } else {
            Log.d(TAG, "List Response Code: " + response.code());
            Log.d(TAG, "List Response Message: " + response.message());
        }
    }

    private void handleFailure(Throwable t) {
        Log.e(TAG, "List Request Failed: " + t.getMessage());

    }

}