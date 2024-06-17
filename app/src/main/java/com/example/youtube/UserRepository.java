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
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    private User loggedUser;
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

    public User findUserById(int id){
        List<User> users = UserRepository.getInstance().getUsers();
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public void loadUsersFromJson(Context context, String jsonFileName) {
        try {
            InputStream is = context.getAssets().open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

            List<User> userList = new ArrayList<>();

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                int id = obj.get("userId").getAsInt();
                String userName = obj.get("userName").getAsString();
                String password = obj.get("userPassword").getAsString();
                String displayName = obj.get("displayName").getAsString();
                String profileImage = obj.get("userImgFile").getAsString();


                int imgResId = context.getResources().getIdentifier(profileImage, "drawable", context.getPackageName());
                if(imgResId == 0){
                    User user = new User(userName,
                            password,
                            displayName,
                            Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.def));
                    user.setId(id);
                }
                User user = new User(userName,
                        password,
                        displayName,
                        Uri.parse("android.resource://" + context.getPackageName() + "/" + imgResId));

                userList.add(user);
            }

            users.clear();
            users.addAll(userList);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}