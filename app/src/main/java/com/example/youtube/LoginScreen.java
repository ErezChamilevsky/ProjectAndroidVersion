package com.example.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

            /*
            Button loginButton = findViewById(R.id.LoginButton);
            loginButton.setOnClickListener(v -> {
                Intent moveToHomeScreen = new Intent(this, //nameOfHomeScreen.class);
                startActivity(moveToHomeScreen);
            });*/

        });



    }
}