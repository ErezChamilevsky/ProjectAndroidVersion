package com.example.youtube;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddNewVideoScreen extends AppCompatActivity {


    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_VIDEO_PICK = 2;

    private static final int REQUEST_IMAGE_CAPTURE = 3;
    private static final int REQUEST_IMAGE_PICK = 4;
    private Button chooseVideobtn;
    private Button chooseImgVideobtn;
    private Button changeVideoButton;
    private Button changeImgVideoButton;
    private Button uploadVideoBtn;
    private Uri selectedVideoUri;
    private Uri ImageVideo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_video_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        chooseVideobtn = findViewById(R.id.chooseVideoBtn);
        changeVideoButton = findViewById(R.id.changeVideoButton);
        chooseImgVideobtn = findViewById(R.id.chooseImgVideoBtn);
        changeImgVideoButton = findViewById(R.id.changeImgVideoButton);
        uploadVideoBtn = findViewById(R.id.uploadVideoBtn);

        chooseVideobtn.setOnClickListener(v -> showVideoPickerDialog());
        changeVideoButton.setOnClickListener(v -> showVideoPickerDialog());
        chooseImgVideobtn.setOnClickListener(v -> showImagePickerDialog());
        changeImgVideoButton.setOnClickListener(v -> showImagePickerDialog());
        uploadVideoBtn.setOnClickListener(v -> addVideoToRepo());


        //hide the change video buttons till user choose video.
        changeVideoButton.setVisibility(View.GONE);
        changeImgVideoButton.setVisibility(View.GONE);

    }



    private void addVideoToRepo() {
        VideoRepository video = VideoRepository.getInstance();

        EditText videoTitleEditText = findViewById(R.id.newVideoTitle);
        EditText videoDescriptionEditText = findViewById(R.id.newVideoDescription);

        String videoTitle = videoTitleEditText.getText().toString();
        String videoDescription = videoDescriptionEditText.getText().toString();
        String publicationDate = null;
        String userName = UserRepository.getInstance().getLoggedUser().getUserName();
        int userId = UserRepository.getInstance().getLoggedUser().getId();

        //set The time now to publication Date
        DateTimeFormatter dtf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        }
        LocalDateTime now = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            publicationDate = dtf.format(now);
        }

        Video uploadVideo = new Video(userName,userId,this.ImageVideo,this.selectedVideoUri,videoTitle,publicationDate,videoDescription);
        VideoRepository.getInstance().addVideo(uploadVideo); //add video to ArrayList in the Repo.

        Intent moveToHomePage = new Intent(this, Homepage.class);
        startActivity(moveToHomePage);
        Toast.makeText(this, "Video added successfully", Toast.LENGTH_SHORT).show();

    }


    // Method to display video picker dialog
    private void showVideoPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Video")
                .setItems(new String[]{"Camera", "Gallery"}, (dialog, which) -> {
                    switch (which) {
                        case 0: // Camera
                            Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            startActivityForResult(cameraIntent, REQUEST_VIDEO_CAPTURE );
                            break;
                        case 1: // Gallery
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, REQUEST_VIDEO_PICK);
                            break;
                    }
                })
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
                            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                            break;
                        case 1: // Gallery
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
                            break;
                    }
                })
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_VIDEO_CAPTURE:  //if video is from camera
                    selectedVideoUri = data.getData();
                    playVideo(selectedVideoUri);
                    // Hide the upload button after the video is selected or captured
                    chooseVideobtn.setVisibility(View.GONE);
                    changeVideoButton.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_VIDEO_PICK:  //if video is from gallery
                    selectedVideoUri = data.getData();
                    playVideo(selectedVideoUri);
                    // Hide the upload button after the video is selected or captured
                    chooseVideobtn.setVisibility(View.GONE);
                    changeVideoButton.setVisibility(View.VISIBLE);
                    break;

                case REQUEST_IMAGE_CAPTURE:  //if image video is from camera
                    ImageView imageView1 = findViewById(R.id.VideoImageImageView);
                    ImageVideo = data.getData(); // set the imageVideo field
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageView1.setImageBitmap(imageBitmap);
                    }
                    // Hide the upload button after the image video is selected or captured
                    chooseImgVideobtn.setVisibility(View.GONE);
                    changeImgVideoButton.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_IMAGE_PICK:   //if image video is from gallery.
                    ImageVideo = data.getData(); // set the imageVideo field
                    ImageView imageView2 = findViewById(R.id.VideoImageImageView);
                    imageView2.setImageURI(ImageVideo);
                    // Hide the upload button after the image video is selected or captured
                    chooseImgVideobtn.setVisibility(View.GONE);
                    changeImgVideoButton.setVisibility(View.VISIBLE);
                    break;
            }

        }
    }


    /*
    * this method play the video in the videoView*/
    private void playVideo(Uri videoUri) {
        VideoView chosenVideoView = findViewById(R.id.addVideoView);
        chosenVideoView.setVideoURI(videoUri);
        chosenVideoView.start();
    }
}