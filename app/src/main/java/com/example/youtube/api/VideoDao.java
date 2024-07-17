package com.example.youtube.api;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.youtube.entities.Video;

import java.util.List;

@Dao
public interface VideoDao {

    @Query("SELECT * FROM Video")
    List<Video> getAllPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Video... video);

    @Query("DELETE FROM Video")
    void deleteAllPosts();

    @Delete
    void delete(Video post);
}