package com.example.youtube.api;

import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @POST("users")
    Call<ResponseBody> createUser(@Body User user);
    @POST("tokens")
    Call<ResponseBody> login(@Body User user);

    @GET("users/{id}")
    Call<User> getUserDetailsByUserName(@Header("Authorization") String token, @Path("id") String userName);
    @PATCH("users/{id}")
    Call<ResponseBody> updateUserDetails(@Header("Authorization") String token, @Path("id") int id);

    @DELETE("users/{id}")
    Call<ResponseBody> deleteUser(@Header("Authorization") String token, @Path("id") int id);


    @POST("users/{id}/videos") //this path is to create new video by the user
    Call<Video> createNewVideo(@Header("Authorization") String token, @Body Video newVideo, @Path("id") int id);
}
