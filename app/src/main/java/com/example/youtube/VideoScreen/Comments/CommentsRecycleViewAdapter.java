package com.example.youtube.VideoScreen.Comments;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.VideoAdapter;

import java.util.ArrayList;

public class CommentsRecycleViewAdapter extends RecyclerView.Adapter<CommentsRecycleViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<CommentItem> commentsList;
    public CommentsRecycleViewAdapter(Context context, ArrayList<CommentItem> commentsList){
        this.context = context;
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentsRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);

        return new CommentsRecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsRecycleViewAdapter.MyViewHolder holder, int position) {
        holder.userName.setText(commentsList.get(position).getUserName());
        holder.text.setText(commentsList.get(position).getText());
        holder.profileImage.setImageURI(commentsList.get(position).getProfileImage());
    }

    @Override
    public int getItemCount() {
        return this.commentsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView text;
        TextView userName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
             profileImage = itemView.findViewById(R.id.userImage);
             text = itemView.findViewById(R.id.commentText);
             userName = itemView.findViewById(R.id.username);

        }
    }
}
