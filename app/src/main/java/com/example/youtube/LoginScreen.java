package com.example.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class LoginScreen extends AppCompatActivity {

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
                String password = passwordEditText.getText().toString();

                int userExist = 0;
                List<User> users = UserRepository.getInstance().getUsers(); //get the users array.
                // iterate the List and check if user details actually exist
                for (int i = 0; i < users.size(); i++){
                    if(userName.equals(users.get(i).getUserName()) && password.equals(users.get(i).getPassword())){
                        UserRepository.getInstance().setLoggedUser(users.get(i)); //set the logged user attribute be the user we found that match.
                        Intent moveToHomeScreen = new Intent(this, Homepage.class); //need to connect to home page
                            startActivity(moveToHomeScreen);
                            //break;
                        userExist = 1;
                        Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show(); //need to delete.
                    }
                }
                    // if the user don't appear in users at all.
                    if(userExist == 0) {
                        Toast.makeText(this, "Username or password are wrong", Toast.LENGTH_SHORT).show();
                    }
            });
    }
}