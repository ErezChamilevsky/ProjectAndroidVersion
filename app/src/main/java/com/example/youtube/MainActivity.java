package com.example.youtube;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube.VideoScreen.Comments.CommentItem;
import com.example.youtube.VideoScreen.Comments.CommentRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        User user = new User("Chad",
                "123456",
                "chad",
                Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.chadlogo)
        );
        user.setId(1);
        UserRepository.getInstance().addUser(user);

        UserRepository.getInstance().setLoggedUser(user);




        Button watchButton = findViewById(R.id.watchButton);
        watchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure there's at least one video in the repository
                if (!VideoRepository.getInstance().getVideos().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, WatchingPage.class);
                    // Pass the first video's ID (index 0)
                    intent.putExtra("VIDEO_ID", VideoRepository.getInstance().getVideos().get(0).getId());
                    startActivity(intent);
                }
            }
        });
    }
}
