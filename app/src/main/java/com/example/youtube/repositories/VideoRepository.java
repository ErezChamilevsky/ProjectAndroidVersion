package com.example.youtube.repositories;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.youtube.api.VideoAPI;
import com.example.youtube.utilities.VideoItem;
import com.example.youtube.entities.Video;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoRepository {

    private static final VideoRepository ourInstance = new VideoRepository();
    private List<Video> videos = new ArrayList<>();
    private ArrayList<VideoItem> videoItemArrayList = new ArrayList<VideoItem>();

    public static VideoRepository getInstance() {
        return ourInstance;
    }

    private VideoRepository() {}

    public List<Video> getVideos() {
        return videos;
    }

    public void addVideo(Video video) {
        videos.add(video);
        VideoItem item = new VideoItem(video);
        item.setUserProfileImage(video.getUserId());
        videoItemArrayList.add(item);
    }

    public void addVideos(List<Video> videoList) { //added in order to add a list gotten from server
        for (Video video : videoList) {
            videos.add(video);
            VideoItem item = new VideoItem(video);
            item.setUserProfileImage(video.getUserId());
            videoItemArrayList.add(item);
        }
    }



    public Video findVideoById(int id){
        List<Video> videos = VideoRepository.getInstance().getVideos();
        for (Video vid : videos) {
            if (vid.getId() == id) {
                return vid;
            }
        }
        return null;
    }

    public void loadVideosFromJson(Context context, String jsonFileName) {
        try {
            InputStream is = context.getAssets().open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

            List<Video> videoList = new ArrayList<>();

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                String displayName = obj.get("displayName").getAsString();
                int userId = obj.get("userId").getAsInt();
                String img = obj.get("img").getAsString();
                String videoSrc = obj.get("videoSrc").getAsString();
                String title = obj.get("title").getAsString();
                String publicationDate = obj.get("publicationDate").getAsString();
                String description = obj.get("description").getAsString();

                // Get the drawable resource ID from the img string
                int imgResId = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
                int videoResId = context.getResources().getIdentifier(videoSrc, "raw", context.getPackageName());

                if(videoResId == 0 || imgResId == 0){
                    continue;
                }

                Video video = new Video(displayName,
                        userId,
                        Uri.parse("android.resource://" + context.getPackageName() + "/" + imgResId),
                        Uri.parse("android.resource://" + context.getPackageName() + "/" + videoResId),
                        title,
                        publicationDate,
                        description);
                videoList.add(video);
            }

            videos.clear();
            videos.addAll(videoList);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    public ArrayList<VideoItem> getVideoItemArrayList(){
        return this.videoItemArrayList;
    }

    public void addItemToVideoItemArrayList(VideoItem videoItem){
        this.videoItemArrayList.add(videoItem);
    }
    public List<VideoItem> addVideoListToItemList(List<Video> videos){ // in use in watching page. but for now in comment
        List<VideoItem> videosItems = new ArrayList<>();
        for(Video video : videos){
            videosItems.add(new VideoItem(video));
        }
        return videosItems;
    }



}