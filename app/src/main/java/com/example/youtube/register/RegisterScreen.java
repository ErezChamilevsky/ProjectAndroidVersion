package com.example.youtube.register;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

import com.example.youtube.login.LoginScreen;
import com.example.youtube.R;
import com.example.youtube.entities.User;
import com.example.youtube.repositories.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegisterScreen extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 3;
    private static final int CAPTURE_IMAGE_REQUEST = 4;

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
            showImagePickerDialog();
        });

        /*
         * this lines implement the logic when "Register" button pressed
         * */
        Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(v -> {

            // Reference all EditText and ImageView into variables
            EditText userNameEditText = findViewById(R.id.registerUsername);
            EditText passwordEditText = findViewById(R.id.registerPassword);
            EditText confirmPasswordEditText = findViewById(R.id.registerConfirmPassword);
            EditText displayNameEditText = findViewById(R.id.registerDisplayName);
            ImageView profileImageImageView = findViewById(R.id.profileImageView);

            // Extract the text from EditText
            String userName = userNameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();
            String displayName = displayNameEditText.getText().toString();

            Bitmap profileImageBitmap = Bitmap.createBitmap(profileImageImageView.getWidth(), profileImageImageView.getHeight(), Bitmap.Config.ARGB_8888);

            // Step 2: Create a Canvas and associate it with the Bitmap
            Canvas canvas = new Canvas(profileImageBitmap);

            // Step 3: Draw the ImageView onto the Canvas, which will render it onto the Bitmap
            profileImageImageView.draw(canvas);


            if (!isValidPassword(password)) { //Checks if the password criteria are met
                showInvalidPasswordDialog();
            } else {
                if (password.equals(confirmPassword)) { //Checks if the password and confirm password fields
                    int isSuccess = addUserToArray(userName, password, displayName, profileImageBitmap);  //if user add successfully to users array ,isSuccess equals 1
                    if (isSuccess == 1) {
                        Toast.makeText(this, "Registration made successfully", Toast.LENGTH_SHORT).show();
                         Intent moveToLogin = new Intent(this, LoginScreen.class);
                         startActivity(moveToLogin);///this lines suppose to move user to login cut there has a problem
                        //,when user try to login immediately after he registered its not working.
                    }
                } else {
                    Toast.makeText(this, "Confirm password doesn't match the password", Toast.LENGTH_SHORT).show();
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

    // Method to show alert for invalid password
    private void showInvalidPasswordDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Invalid Password")
                .setMessage("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and the rest digits.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Method to display image picker dialog
    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image")
                .setItems(new String[]{"Camera", "Gallery"}, (dialog, which) -> {
                    switch (which) {
                        case 0: // Camera
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST);
                            break;
                        case 1: // Gallery
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                            break;
                    }
                })
                .show();
    }

    // Method to display the selected image in ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri profileImage = data.getData();
            ImageView imageView = findViewById(R.id.profileImageView);

            if (requestCode == PICK_IMAGE_REQUEST) {  //if selected image is from gallery
                imageView.setImageURI(profileImage);
            } else if (requestCode == CAPTURE_IMAGE_REQUEST) { //if selected image is from camera
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(imageBitmap);
                }
            }
        }
    }

    // Method to add the user that registers into the users list
    private int addUserToArray(String userName, String password, String displayName, Bitmap profileImageBitmap) {
        if (userName.isEmpty() || password.isEmpty() || displayName.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return 0;
        } else {
            User user = new User(userName, password, displayName, bitmapToUriConverter(this,profileImageBitmap));
            UserRepository.getInstance().addUser(user); // Add user to users List
            return 1;
        }
    }

    public Uri bitmapToUriConverter(Context context, Bitmap bitmap) {
        // Get the context's content resolver
        ContentResolver contentResolver = context.getContentResolver();

        // Define a URI where the image will be stored
        Uri imageUri = null;

        // Initialize a file to store the bitmap
        File imageFile = new File(context.getCacheDir(), "imageFileName.jpg");

        try {
            // Convert bitmap to JPEG format (you can change PNG to JPEG or other formats)
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // Get the absolute file path
            String absolutePath = imageFile.getAbsolutePath();

            // Convert file path into Uri
            imageUri = Uri.parse("file://" + absolutePath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageUri;
    }

}
