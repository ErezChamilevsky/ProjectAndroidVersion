package com.example.youtube.api;

import com.example.youtube.R;
import com.example.youtube.ViewModels.MyApplication;
import com.example.youtube.entities.CommentItem;
import com.example.youtube.entities.User;
import com.example.youtube.login.LoginScreen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentAPI {

    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public CommentAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getCommentsByVideoId(int videoId, Callback<List<CommentItem>> callback) {
        Call<List<CommentItem>> call = webServiceAPI.getCommentsByVideoId(videoId);
        call.enqueue(callback);
        // the onResponse and onFailure is in the corresponding screen file
    }

    public void createComment(CommentItem comment, Callback<ResponseBody> callback) {


        // Assuming token is a Bearer token
        String authToken = "Bearer " + LoginScreen.token;

        Call<ResponseBody> call = webServiceAPI.createComment(authToken, comment.getVideoId(), comment);
        call.enqueue(callback);
        // The onResponse and onFailure is in the corresponding screen file
    }

    public void getCommentById(int videoId, int commentId, Callback<CommentItem> callback) {
        Call<CommentItem> call = webServiceAPI.getCommentById(videoId, commentId);
        call.enqueue(callback);
        // the onResponse and onFailure is in the corresponding screen file
    }

    public void deleteCommentById(int videoId, int commentId, Callback<Void> callback) {
        Call<Void> call = webServiceAPI.deleteCommentById(videoId, commentId);
        call.enqueue(callback);
        // the onResponse and onFailure is in the corresponding screen file
    }

    public void editCommentByIdPatch(int videoId, int commentId, CommentItem comment, Callback<CommentItem> callback) {
        Call<CommentItem> call = webServiceAPI.editCommentByIdPatch(commentId, comment);
        call.enqueue(callback);
        // the onResponse and onFailure is in the corresponding screen file
    }

    public void editCommentByIdPut(int videoId, int commentId, CommentItem comment, Callback<CommentItem> callback) {
        Call<CommentItem> call = webServiceAPI.editCommentByIdPut(videoId, commentId, comment);
        call.enqueue(callback);
        // the onResponse and onFailure is in the corresponding screen file
    }


}
//tODO where to put the functions to request and etc.