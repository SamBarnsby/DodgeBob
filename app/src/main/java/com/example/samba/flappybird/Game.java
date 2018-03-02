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
        gameView = (GameView) findViewById(R.id.GameView);
    }
}
