package com.example.youtube;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoDisplay extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String videoSrc;

    public static VideoDisplay newInstance(String param1, String param2) {
        VideoDisplay fragment = new VideoDisplay();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.videoSrc = param1;
        fragment.setArguments(args);
        return fragment;
    }

    public VideoDisplay() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_display, container, false);
       if(videoSrc == null){
           return view;
       }
        displayVideo(Uri.parse(videoSrc), view);
//        TextView txt = view.findViewById(R.id.text);
//        txt.setText(videoSrc);
        return view;
    }

    private void displayVideo(Uri videoSrc, View view) {


//        TextView txt = view.findViewById(R.id.text);
//        txt.setText(videoSrc.toString());
           VideoView videoView = view.findViewById(R.id.videoBox);
        videoView.setVideoURI(videoSrc);

        // Create MediaController
        MediaController mediaController = new MediaController(requireContext());
        mediaController.setAnchorView(videoView);

        // Set media controller to VideoView
        videoView.setMediaController(mediaController);

        // Start the video
        videoView.start();
    }
}
