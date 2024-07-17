package com.example.youtube.api;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.youtube.entities.CommentItem;

import org.w3c.dom.Comment;

import java.util.List;

@Dao
public interface CommentItemDao {
    @Query("SELECT * FROM CommentItem")
    List<CommentItem> getAllComments();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CommentItem... comment);

    @Query("DELETE FROM CommentItem")
    void deleteAllComments();

    @Delete
    void delete(CommentItem comment);
}
