package com.example.youtube.ViewModels;

import androidx.lifecycle.LiveData;

import com.example.youtube.entities.User;
import com.example.youtube.repositories.UserRepository;

import java.util.List;

public class UsersViewModel {

    private LiveData<List<User>> users;
    public UsersViewModel(){
        users = (LiveData<List<User>>) UserRepository.getInstance().getUsers();
    }
    public LiveData<List<User>> get() {return users; }
    public void setUsers(List<User> u) {
        UserRepository.getInstance().setUsers(u);
    }
}
