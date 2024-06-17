package com.example.youtube;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

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
                String userName = obj.get("artist").getAsString();
                int userId = obj.get("user_id").getAsInt();
                String img = obj.get("img").getAsString();
                String videoSrc = obj.get("video_src").getAsString();
                String title = obj.get("title").getAsString();
                String publicationDate = obj.get("publication_date").getAsString();
                String description = obj.get("details").getAsString();

                // Get the drawable resource ID from the img string
                int imgResId = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
                int videoResId = context.getResources().getIdentifier(videoSrc, "raw", context.getPackageName());

                if(videoResId == 0 || imgResId == 0){
                    continue;
                }

                Video video = new Video(userName,
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

}