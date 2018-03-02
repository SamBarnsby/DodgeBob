package com.example.samba.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity implements View.OnClickListener {
    private ImageButton bScore;
    private ImageButton bPlay;
    public static HighScoreArray storage = new HighScoreArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bScore = findViewById(R.id.scores);
        bPlay = findViewById(R.id.start);
        bScore.setOnClickListener(this);
        bPlay.setOnClickListener(this);
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
        Intent i = new Intent(this, Game.class);
        startActivity(i);
    }
}
