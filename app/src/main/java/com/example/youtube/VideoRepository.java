package com.example.youtube;

import java.util.ArrayList;
import java.util.List;

public class VideoRepository {
    private static final VideoRepository ourInstance = new VideoRepository();
    private List<Video> videos = new ArrayList<>();

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
}
