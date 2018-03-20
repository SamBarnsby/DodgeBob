package com.example.samba.flappybird;

import android.app.Activity;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.samba.flappybird.R;

/**
 * Created by samba on 12/11/2017.
 */

public class Game extends Activity implements View.OnClickListener {
    private GameView gameView;
    private Button pause;
    private boolean isPaused;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        gameView = findViewById(R.id.GameView);
        gameView.setFather(this);
        pause = findViewById(R.id.pause);
        pause.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        if(v.getId() ==R.id.pause){
            if(!isPaused) {
                pause.setText("Resume");
                gameView.getThread().pause();
                isPaused = true;
            }
            else {
                pause.setText("Pause");
                gameView.getThread().resumeThread();
                isPaused = false;
            }
        }
    }
}
