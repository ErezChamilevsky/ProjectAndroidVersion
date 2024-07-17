package com.example.youtube.watchingPage;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.ViewModels.MyApplication;
import com.example.youtube.adapters.CommentsRecycleViewAdapter;
import com.example.youtube.api.CommentAPI;
import com.example.youtube.api.UserAPI;
import com.example.youtube.entities.CommentItem;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.login.LoginScreen;
import com.example.youtube.register.RegisterScreen;
import com.example.youtube.repositories.CommentRepository;
import com.example.youtube.repositories.UserRepository;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsManager {

    private final Context context;
    private final Video video;
    private final ArrayList<CommentItem> commentsList;
    private CommentsRecycleViewAdapter commentAdapter;
    private final RecyclerView commentsRecyclerView;

    public CommentsManager(Context context, Video video, RecyclerView commentsRecyclerView) {
        this.context = context;
        this.video = video;
        this.commentsRecyclerView = commentsRecyclerView;
        this.commentsList = CommentRepository.getInstance().findCommentByVideoId(video.getId());

        setupCommentSection();
    }

    // Set up comments section
    private void setupCommentSection() {
        getCommentsFromServer();
        TextView commentsSection = ((WatchingPage) context).findViewById(R.id.commentsSection);
        if (UserRepository.getInstance().getLoggedUser() != null) {

            EditText newCommentBox = ((WatchingPage) context).findViewById(R.id.newCommentBox);
            Button postCommentButton = ((WatchingPage) context).findViewById(R.id.commentButton);
            postCommentButton.setText("Send");

            commentsSection.setOnClickListener(v -> toggleCommentsSection());


            postCommentButton.setOnClickListener(v -> {
                String text = newCommentBox.getText().toString();
                if (!text.isEmpty()) {
                        postCommentToServer(text);

                        newCommentBox.setText("");
//                        commentAdapter.notifyItemInserted(commentsList.size() - 1);
                }
            });
        }

        setupCommentsRecyclerView();
    }


    // Toggle visibility of comments section
    private void toggleCommentsSection() {
        View commentsContainer = ((WatchingPage) context).findViewById(R.id.commentsContainer);
        commentsContainer.setVisibility(commentsContainer.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    // Set up RecyclerView for comments
    private void setupCommentsRecyclerView() {
        commentAdapter = new CommentsRecycleViewAdapter(context, commentsList);
        commentsRecyclerView.setAdapter(commentAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }


    void postCommentToServer(String content) {

        CommentAPI commentAPI = new CommentAPI();
        CommentItem comment = new CommentItem(video, content);

        commentAPI.createComment(comment, new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                int statusCode = response.code();
                try {

                    if (statusCode == 200) {
                        // Registration successful (status code 200)
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String successMsg = jsonObject.getString("message");  // extract the success msg from json that server returned us
                    } else {
                        // Extract error message from JSON response
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String errorMsg = jsonObject.getString("message"); // extract the error msg from json that server returned us
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "heee");
            }
        });
    }

    void getCommentsFromServer() {

        CommentAPI commentAPI = new CommentAPI();
        commentAPI.getCommentsByVideoId(video.getId(), new Callback<List<CommentItem>>() {

            @Override
            public void onResponse(@NonNull Call<List<CommentItem>> call, @NonNull Response<List<CommentItem>> response) {
                handleResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<CommentItem>> call, Throwable t) {
                handleFailure(t);
            }
        });

    }

    private void handleResponse(Call<List<CommentItem>> call, Response<List<CommentItem>> response) {
        if (response.isSuccessful()) {
            List<CommentItem> comments = response.body();
            commentsList.addAll(comments);
            Log.d("API_CALL", "Success: " + comments);
        } else {
            Log.d("API_CALL", "Response Code: " + response.code());
            Log.d("API_CALL", "Response Message: " + response.message());
        }
    }

    private void handleFailure(Throwable t) {
        Log.e("API_CALL", "Request Failed: " + t.getMessage());

    }
}
