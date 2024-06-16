package com.example.youtube;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final UserRepository ourInstance = new UserRepository();
    private List<User> users;
    private User loggedUser;

    public static UserRepository getInstance() {
        return ourInstance;
    }

    private UserRepository() {
        this.users = new ArrayList<>();
        this.loggedUser = new User("","","",null);
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }
    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

}
