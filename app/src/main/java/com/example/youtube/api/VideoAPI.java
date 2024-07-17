package com.example.youtube.api;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.youtube.R;
import com.example.youtube.ViewModels.MyApplication;
import com.example.youtube.entities.CommentItem;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.login.LoginScreen;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoAPI {

    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public VideoAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getVideosToPresent(Callback<List<Video>>callback) {
        Call<List<Video>> call = webServiceAPI.getVideosToPresent();
        call.enqueue(callback);
        // the onResponse and onFailure is in the corresponding screen file
    }

    public void getVideo(int videoId, Callback<Video> callback) {
        Call<Video> call = webServiceAPI.getVideo(videoId);
        call.enqueue(callback);
        // the onResponse and onFailure is in the corresponding screen file
    }

}
//tODO where to put the functions to request and etc.