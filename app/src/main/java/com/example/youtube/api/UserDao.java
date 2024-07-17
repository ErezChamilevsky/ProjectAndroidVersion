package com.example.youtube.api;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.youtube.entities.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User WHERE _id = :id")
    User getUserById(String id);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM User LIMIT 1")
    User getUser();

    @Insert
    void insert(User... user);

    @Query("DELETE FROM User")
    void deleteAllUsers();

}