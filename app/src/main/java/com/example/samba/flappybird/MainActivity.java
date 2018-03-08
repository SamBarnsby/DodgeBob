package com.example.samba.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.q42.android.scrollingimageview.ScrollingImageView;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button bScore;
    private Button bPlay;
    public static HighScoreArray storage = new HighScoreArray();
    MediaPlayer reproductor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bScore = findViewById(R.id.scores);
        bPlay = findViewById(R.id.start);
        bScore.setOnClickListener(this);
        bPlay.setOnClickListener(this);

        //startService(new Intent(MainActivity.this, MusicService.class));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.scores) launchScore();
        if(v.getId() == R.id.start) launchPlay();
    }

    public void launchScore() {
        Intent i = new Intent(this, Scores.class);
        startActivity(i);
    }
    public void launchPlay() {
        bPlay.setBackgroundResource(R.drawable.pressed);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(MainActivity.this,Game.class);
                startActivity(i);
            }
        }, 200 );
    }

    @Override
    protected void onResume() {
        super.onResume();
        bPlay.setBackgroundResource(R.drawable.boto);
    }
}
