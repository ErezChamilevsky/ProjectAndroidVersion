package com.example.youtube.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.utilities.RecyclerViewInterface;
import com.example.youtube.utilities.VideoItem;

import java.util.ArrayList;

public class VideoItemsRecyclerViewAdapter extends RecyclerView.Adapter<VideoItemsRecyclerViewAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    ArrayList<VideoItem> videoItemArrayList;

    ArrayList<VideoItem> searchResults;
    Context context;
    public VideoItemsRecyclerViewAdapter(Context context, ArrayList<VideoItem> videoItemArrayList, RecyclerViewInterface recyclerViewInterface){

        this.context=context;
        this.videoItemArrayList=videoItemArrayList;
        this.recyclerViewInterface = recyclerViewInterface;

    }
    @NonNull
    @Override
    public VideoItemsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.video_item, parent, false);

        return new VideoItemsRecyclerViewAdapter.MyViewHolder(view, this.recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoItemsRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.videoTitle.setText(videoItemArrayList.get(position).getItemTitle());
        holder.videoDescription.setText(videoItemArrayList.get(position).getUploaderName() + " • " + videoItemArrayList.get(position).getViews() + "views • " + videoItemArrayList.get(position).getUploadDate());
        holder.videoDescription.setText(videoItemArrayList.get(position).getUploaderName());
        holder.avatar.setImageURI(videoItemArrayList.get(position).getAvatar());
        holder.thumbnail.setImageURI(videoItemArrayList.get(position).getThumbnail());
    }

    @Override
    public int getItemCount() {
        return this.videoItemArrayList.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        ImageView thumbnail;

        ImageView avatar;

        TextView videoTitle;
        TextView videoDescription;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.videoThumbnail);
            avatar = itemView.findViewById(R.id.uploaderAvatar);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoDescription = itemView.findViewById(R.id.videoDetails);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public void filter(String text) {
        searchResults.clear();
        if (text.isEmpty()) {
            searchResults.addAll(videoItemArrayList);
        } else {
            text = text.toLowerCase();
            for (VideoItem item : videoItemArrayList) {
                if (item.getItemTitle().toLowerCase().contains(text)) {
                    searchResults.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
