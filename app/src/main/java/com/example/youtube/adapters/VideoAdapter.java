package com.example.youtube.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.entities.Video;
import com.example.youtube.watchingPage.WatchingPage;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {


    private List<Video> videoList;
    private Context context;

    public VideoAdapter(List<Video> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.titleTextView.setText(video.getTitle());

//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, WatchingPage.class);
//            intent.putExtra("VIDEO_ID", video.getId());
//            intent.putExtra("VIDEO_TITLE", video.getTitle());
//            intent.putExtra("VIDEO_SRC", video.getVideoSrc());
//            context.startActivity(intent);
//        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), WatchingPage.class);
            holder.itemView.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.videoTitle);
        }
    }
}
