package com.example.youtube;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        /*
        * this lines implement the logic when "upload profile image" button pressed
        * */
        Button uploadProfileImage = findViewById(R.id.registerUplProImgBtn);
        uploadProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 3);
        });


        /*
         * this lines implement the logic when "Register" button pressed
         * */
        Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(v -> {

            // reference all EditText an imageView into variables.
            EditText userNameEditText = findViewById(R.id.registerUsername);
            EditText passwordEditText = findViewById(R.id.registerPassword);
            EditText confirmPasswordEditText = findViewById(R.id.registerConfirmPassword);
            EditText displayNameEditText = findViewById(R.id.registerDisplayName);
            ImageView profileImageImageView = findViewById(R.id.profileImageView);


            //extract the text from Edit text
            String userName = userNameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();
            String displayName = displayNameEditText.getText().toString();
            Uri profileImage=null;
            //profileImageImageView.setImageURI(profileImage);



            if (!isValidPassword(password)) { //Checks if the password criteria are met
                showInvalidPasswordDialog();
            } else {
                if (password.equals(confirmPassword)) { //Checks if the password and confirm password fields are same
                    int isSuccess = addUserToArray(userName, password, displayName, profileImage); //if user add successfully to users array ,isSuccess equals 1
                    if (isSuccess == 1) {
                        Toast.makeText(this, "registration made successfully", Toast.LENGTH_SHORT).show();
                        /*Intent moveToLogin = new Intent(this, LoginScreen.class);
                        startActivity(moveToLogin);*///this lines suppose to move user to login cut there has a problem
                        //,when user register an immediately after that try to login its not working.
                    }
                }else {
                    Toast.makeText(this, "confirm password don't match to password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    /*
     * this method check if user's input password meets the criteria.
     */
    public boolean isValidPassword(String password) {
        if (password.length() < 8) return false;

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isLowerCase(c)) hasLowercase = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        return hasUppercase && hasLowercase && hasDigit;
    }

    /**
     *
     * this method alert when user press invalid password.
     */
    private void showInvalidPasswordDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Invalid Password")
                .setMessage("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and the rest digits.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /*
    * this method display the image who chosen from gallery in imageView
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            Uri profileImage = data.getData();
            ImageView imageView = findViewById(R.id.profileImageView);
            imageView.setImageURI(profileImage);

        }
    }


    /**
     * this method add the user that register into users list.
     */
    private int addUserToArray(String userName, String password, String displayName, Uri profileImage) {
        if(userName.isEmpty() || password.isEmpty() || displayName.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return 0;
        }else{
            User user = new User(userName,password,displayName, profileImage);
            UserRepository.getInstance().addUser(user); //add user to users List
            return 1;
        }
    }
}