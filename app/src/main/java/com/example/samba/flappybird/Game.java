package com.example.samba.flappybird;

import android.app.Activity;
import android.hardware.SensorEventListener;
import android.os.Bundle;

import com.example.samba.flappybird.R;

/**
 * Created by samba on 12/11/2017.
 */

public class Game extends Activity {
    private GameView gameView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        gameView = findViewById(R.id.GameView);
        gameView.setFather(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.getThread().pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.getThread().resumeThread();
    }

    @Override
    protected void onDestroy() {
        gameView.getThread().stopThread();
        super.onDestroy();
    }
}
