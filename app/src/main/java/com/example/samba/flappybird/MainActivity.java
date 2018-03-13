package com.example.samba.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.q42.android.scrollingimageview.ScrollingImageView;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button bScore;
    private Button bPlay;
    private Button bSettings;
    private Button bExit;
    public static HighScoreArray storage = new HighScoreArray();
    MediaPlayer reproductor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bScore = findViewById(R.id.scores);
        bPlay = findViewById(R.id.start);
        bSettings = findViewById(R.id.settings);
        bScore.setOnClickListener(this);
        bPlay.setOnClickListener(this);
        bSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.scores) launchScore();
        if(v.getId() == R.id.start) launchPlay();
        if(v.getId() == R.id.settings) launchSettings();
        //if(v.getId() == R.id.exit) exit();
    }

    /*public void exit() {

    }*/

    public void launchScore() {
        bScore.setBackgroundResource(R.drawable.pressed);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(MainActivity.this,Scores.class);
                startActivity(i);
            }
        }, 200 );
    }

    public void launchSettings() {
        bSettings.setBackgroundResource(R.drawable.pressed);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(i);
            }
        }, 200 );
    }

    public void launchPlay() {
        bPlay.setBackgroundResource(R.drawable.pressed);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(MainActivity.this,Game.class);
                startActivityForResult(i, 1234);
            }
        }, 200 );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234 && resultCode == RESULT_OK && data != null) {
            int score = data.getExtras().getInt("score");
            storage.saveScore(score, "Sam");
            launchScore();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bPlay.setBackgroundResource(R.drawable.boto);
        bSettings.setBackgroundResource(R.drawable.boto);
        bScore.setBackgroundResource(R.drawable.boto);
        //bExit.setBackgroundResource(R.drawable.boto);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("music", true) == true) {
            startService(new Intent(MainActivity.this, MusicService.class));
        }
        else {
            stopService(new Intent(MainActivity.this, MusicService.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, MusicService.class));
    }
}
