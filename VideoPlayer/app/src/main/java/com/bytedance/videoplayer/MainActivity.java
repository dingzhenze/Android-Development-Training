package com.bytedance.videoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private Button buttonPlay;
    private Button buttonPause;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594733641900&di=f88d8148c5376b042d2839dc9ad12795&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20190119%2F6fb0afd7e3c44d2ba6fe6dd946c98f7a.jpeg";
//        Glide.with(this)
//                .load(url)
//                .transition(withCrossFade())
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.error)
//                .into(imageView);
        MyGride.GlideWithPlaceHolder(MainActivity.this, url).into(imageView);

        buttonPause = findViewById(R.id.buttonPause);
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
            }
        });

        buttonPlay = findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
            }
        });

        videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(getVideoPath(R.raw.chapter1));
        videoView.setMediaController(new MediaController(this));
    }

    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }
}
