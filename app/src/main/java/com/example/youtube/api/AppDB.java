package com.example.youtube.api;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


import com.example.youtube.entities.CommentItem;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;

@Database(entities = {User.class, Video.class, CommentItem.class},version =3)
@TypeConverters(Convertors.class)
public abstract class AppDB extends RoomDatabase {

    public abstract VideoDao videoDaoDao();
    public abstract UserDao userDao();
    public abstract CommentItemDao commentDao();
    public void clearAllTables() {
    }

}
