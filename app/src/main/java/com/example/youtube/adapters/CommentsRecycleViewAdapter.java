package com.example.youtube.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.repositories.CommentRepository;
import com.example.youtube.entities.CommentItem;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.userName.setText(commentsList.get(position).getUserName());
        holder.text.setText(commentsList.get(position).getText());
        holder.profileImage.setImageURI(commentsList.get(position).getProfileImage());

        // Set initial visibility
        holder.itemView.findViewById(R.id.editedBox).setVisibility(View.GONE);
        holder.itemView.findViewById(R.id.editButtonsContainer).setVisibility(View.GONE);
        holder.text.setVisibility(View.VISIBLE);

        //remove an comment, first we remove from the whole repository, than from the video's comments list specific
        //at last we need to notify something has been deleted
        holder.itemView.findViewById(R.id.removeComment).setOnClickListener(v -> {
            CommentRepository.getInstance().getComments().remove(this.commentsList.get(position));
            this.commentsList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, commentsList.size());
        });


        //open the edit section
        holder.editComment.setOnClickListener(v -> {
            holder.editedBox.setVisibility(View.VISIBLE);
            holder.editButtonsContainer.setVisibility(View.VISIBLE);
            holder.text.setVisibility(View.GONE);
            holder.editedBox.setText(commentsList.get(position).getText());
        });

        // Save the edited comment
        holder.saveEdit.setOnClickListener(v -> {
            String editedText = holder.editedBox.getText().toString();
            commentsList.get(position).setText(editedText);
            holder.text.setText(editedText);

            holder.editedBox.setVisibility(View.GONE);
            holder.editButtonsContainer.setVisibility(View.GONE);
            holder.text.setVisibility(View.VISIBLE);

            // Optionally, update the comment in the repository or database TODO need to check why is causing crashing
            CommentRepository.getInstance().editComment(commentsList.get(position).getCommentId(), editedText);
            commentsList.set(position, commentsList.get(position));

            notifyItemChanged(position);
        });

        // Cancel editing
        holder.cancelEdit.setOnClickListener(v -> {
            holder.editedBox.setVisibility(View.GONE);
            holder.editButtonsContainer.setVisibility(View.GONE);
            holder.text.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return this.commentsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView text;
        ImageView profileImage;
        EditText editedBox;
        LinearLayout editButtonsContainer;
        Button removeComment;
        Button editComment;
        Button saveEdit;
        Button cancelEdit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.commentText);
            profileImage = itemView.findViewById(R.id.userImage);
            editedBox = itemView.findViewById(R.id.editedBox);
            editButtonsContainer = itemView.findViewById(R.id.editButtonsContainer);
            removeComment = itemView.findViewById(R.id.removeComment);
            editComment = itemView.findViewById(R.id.editComment);
            saveEdit = itemView.findViewById(R.id.saveEdit);
            cancelEdit = itemView.findViewById(R.id.cancelEdit);
        }
    }

}
