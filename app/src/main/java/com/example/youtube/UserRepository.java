package com.example.youtube;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final UserRepository ourInstance = new UserRepository();
    private List<User> users = new ArrayList<>();

    public static UserRepository getInstance() {
        return ourInstance;
    }

    private UserRepository() {}

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }
}
