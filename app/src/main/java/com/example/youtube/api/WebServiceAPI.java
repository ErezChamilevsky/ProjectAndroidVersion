package com.example.youtube.api;

import com.example.youtube.entities.CommentItem;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    // Get comments for a specific video
    @GET("videos/{pid}/comments")
    Call<List<CommentItem>> getCommentsByVideoId(@Path("pid") int pid);

    // Create a new comment for a specific video
    @POST("videos/{pid}/comments")
    Call<ResponseBody> createComment(
            @Header("Authorization") String token,
            @Path("pid") int pid,
            @Body CommentItem commentData
    );

    // Get a specific comment by comment ID
    @GET("{pid}/comments/{cid}")
    Call<CommentItem> getCommentById(@Path("pid") int pid, @Path("cid") int cid);

    // Delete a specific comment by comment ID
    @DELETE("{pid}/comments/{cid}")
    Call<Void> deleteCommentById(@Path("pid") int pid, @Path("cid") int cid);

    // Partially update a specific comment by comment ID
    @PATCH("{pid}/comments/{cid}")
    Call<CommentItem> editCommentByIdPatch(@Path("cid") int cid, @Body CommentItem comment);

    // Fully update a specific comment by comment ID
    @PUT("{pid}/comments/{cid}")
    Call<CommentItem> editCommentByIdPut(@Path("pid") int pid, @Path("cid") int cid, @Body CommentItem comment);


    @GET("videos")
    Call<List<Video>> getVideosToPresent();

    @GET("videos/{pid}")
    Call<Video> getVideo(@Path("pid") int videoId);

    @GET("videos/users/{id}")
    Call<User> getUserDetailsForVideoPage(@Path("id") int id);

    @GET("videos/users/{id}/uploads")
    Call<List<Video>> getVideoListByUserId(@Path("id") int id);

    @POST("users/{id}/videos") //this path is to create new video by the user
    Call<Video> createNewVideo(@Header("Authorization") String token, @Body Video newVideo, @Path("id") int id);

}
