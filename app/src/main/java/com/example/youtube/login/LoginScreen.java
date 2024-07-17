package com.example.youtube.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.youtube.api.AppDB;
import com.example.youtube.api.UserAPI;
import com.example.youtube.api.UserDao;
import com.example.youtube.homePage.Homepage;
import com.example.youtube.R;
import com.example.youtube.entities.User;
import com.example.youtube.register.RegisterScreen;
import com.example.youtube.repositories.UserRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreen extends AppCompatActivity {
    public static String token = null; //token static variable
    private AppDB appDB;
    private UserDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //this lines move the user to Register screen when he push the button "create an account".
        Button createAnAccountBtn = findViewById(R.id.createAnAccountBtn);
        createAnAccountBtn.setOnClickListener(v -> {
            Intent moveToRegister = new Intent(this, RegisterScreen.class);
            startActivity(moveToRegister);
        });

            Button loginButton = findViewById(R.id.LoginButton);
            loginButton.setOnClickListener(v -> {
                // reference all EditText an imageView into variables.
                EditText userNameEditText = findViewById(R.id.LoginUsername);
                EditText passwordEditText = findViewById(R.id.LoginPassword);

                //extract the text from Edit text.
                String userName = userNameEditText.getText().toString();
                String userPassword = passwordEditText.getText().toString();




                //send request to server to Login.
                UserAPI loginUsersApi = new UserAPI();
                User loginUser = new User(userName, userPassword, null,null,null);

                //this request check if user exist in MongoDB and return token (if exist)
                loginUsersApi.loginServer(loginUser,new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        try {
                            if (response.isSuccessful()) {
                                String status = String.valueOf(response.code());
                                String responseBody = response.body().string();
                                JSONObject jsonResponse = new JSONObject(responseBody);
                                LoginScreen.token = jsonResponse.getString("token"); //extract token from server response.

                                if (status.equals("200")) { //if server return a token.

                                    //this request is for get the details from server of user that login right now.
                                    loginUsersApi.getUserDetails("Bearer " + LoginScreen.token, userName, new Callback<User>() {
                                        @Override
                                        public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                            if (response.isSuccessful()) {
                                                User loginUser = response.body();
                                                UserRepository.getInstance().setLoggedUser(loginUser); // set the logged user that return from server
                                                // Login successful (status code 200)
                                                Intent moveToHomeScreen = new Intent(LoginScreen.this, Homepage.class); //move to HomePage
                                                startActivity(moveToHomeScreen);


                                                //here we define the user ROOM
//                                            appDB = Room.databaseBuilder(getApplicationContext(), AppDB.class, "youtubeDB")
//                                                    .allowMainThreadQueries()
//                                                    .fallbackToDestructiveMigration()
//                                                    .build();
//                                            userDao = appDB.userDao();
//                                            new Thread(() -> userDao.insert(loginUser)).start();

                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                            Toast.makeText(LoginScreen.this, "failed to load the user details", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    //if get user details is fail (server sena a error)
                                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                    String errorMsg = jsonObject.getString("errors"); // extract the error msg from json that server returned us
                                    runOnUiThread(() -> {
                                        Toast.makeText(LoginScreen.this, errorMsg, Toast.LENGTH_SHORT).show();
                                    });
                                }

                            } else {
                                //if get user details is fail (server sena a error and not the token)
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                String errorMsg = jsonObject.getString("message"); // extract the error msg from json that server returned us
                                runOnUiThread(() -> {
                                    Toast.makeText(LoginScreen.this, errorMsg, Toast.LENGTH_SHORT).show();
                                });
                            }
                        } catch(IOException | JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast.makeText(LoginScreen.this ,"Invalid server call!", Toast.LENGTH_SHORT).show();
                    }
                });

            });
    }
}