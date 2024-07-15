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
        TextView commentsSection = ((WatchingPage) context).findViewById(R.id.commentsSection);
        if (UserRepository.getInstance().getLoggedUser() != null) {

            EditText newCommentBox = ((WatchingPage) context).findViewById(R.id.newCommentBox);
            Button postCommentButton = ((WatchingPage) context).findViewById(R.id.commentButton);
            postCommentButton.setText("Send");

            commentsSection.setOnClickListener(v -> toggleCommentsSection());
//---------------------------------------------------------------------------------------------
//            ---------------------------------------------------------------------------------------------
            postCommentButton.setOnClickListener(v -> {
                String text = newCommentBox.getText().toString();
                if (!text.isEmpty()) {
                        postCommentToServer(text);
//                    comment -> {
//                        commentsList.add(comment);
//                        newCommentBox.setText("");
//                        commentAdapter.notifyItemInserted(commentsList.size() - 1);
//                    });
                }
            });
        }

        setupCommentsRecyclerView();
    }

    void postCommentToServer(String content){

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
                    }
                    else {
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
//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

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

    // Callback interface for comment posting
    private interface CommentCallback {
        void onSuccess(CommentItem comment);
    }
}
