package com.example.youtube.api;

import androidx.lifecycle.MutableLiveData;

import com.example.youtube.R;
import com.example.youtube.ViewModels.MyApplication;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;

import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {

        private MutableLiveData<List<User>> userListData;
        Retrofit retrofit;
        WebServiceAPI webServiceAPI;

        public UserAPI() {
            //create retrofit with access to server
            retrofit = new Retrofit.Builder()
                    .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                    .callbackExecutor(Executors.newSingleThreadExecutor())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            webServiceAPI = retrofit.create(WebServiceAPI.class);
        }

        public void registerServer(User newUser, Callback<ResponseBody> callback) {
            Call<ResponseBody> call = webServiceAPI.createUser(newUser);
            call.enqueue(callback);
            // the onResponse and onFailure is in RegisterScreen.java file
        }

    public void loginServer(User user, Callback<ResponseBody> callback){
        Call<ResponseBody> call = webServiceAPI.login(user);
        call.enqueue(callback);
        // the onResponse and onFailure is in LoginScreen.java file
        }


    public void getUserDetails(String token,String userName, Callback<User> callback){
        Call<User> call = webServiceAPI.getUserDetailsByUserName(token,userName);
        call.enqueue(callback);
    }




    public void createNewVideo(String token, Video newVideo, int id, Callback<Video> callback){
        Call<Video> call = webServiceAPI.createNewVideo(token, newVideo, id);
        call.enqueue(callback);
    }
}

