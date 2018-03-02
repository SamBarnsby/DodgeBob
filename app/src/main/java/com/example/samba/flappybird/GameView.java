package com.example.samba.flappybird;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;
import java.util.Vector;

/**
 * Created by samba on 30/01/2018.
 */

public class GameView extends View {
    Context contexte;
    private GameThread thread = new GameThread();
    private static double PLAYER_MOVEMENT_SPEED = 0.0;
    private static double ENEMY_MOVEMENT_SPEED = 10.0;
    private int NEXT_ENEMY_SECONDS = 2000;
    boolean pressed = true;
    private View fullView;

    private static int BETWEEN_TIME = 50;
    //Quan es va relaitzar el darrer proces
    private long lastPlayerProcess = 0;
    private long lastEnemyProcess = 0;

    private Vector<Player> enemies;
    private Player player;
    private Player enemy;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        contexte = context;

        //fullView = findViewById(R.id.GameView);
        //fullView.setOnTouchListener(touchListener);
        Drawable drawablePlayer = context.getResources().getDrawable(R.drawable.player);
        player = new Player(this, drawablePlayer);
        enemies = new Vector<>();
        addEnemy();
    }

    protected void onSizeChanged(int ample, int alt, int ample_ant, int alt_ant) {
        super.onSizeChanged(ample, alt, ample_ant, alt_ant);

        player.setCenX (ample / 2);
        player.setCenY (alt - (alt / 10));
        thread.start();
    }

    protected synchronized void addEnemy() {
        Random rand = new Random();

        int  x = rand.nextInt(getResources().getDisplayMetrics().widthPixels) + 0;
        Drawable drawableEnemy = contexte.getResources().getDrawable(R.drawable.birdenemy);
        enemy = new Player(this, drawableEnemy);
        enemy.setCenX(x);
        enemy.setCenY(-100);
        enemies.add(enemy);
    }

    protected synchronized void updatePlayer(double speed) {
        long now = System.currentTimeMillis();
        if(lastPlayerProcess+BETWEEN_TIME > now) {
            return;
        }
        lastPlayerProcess = now;
        player.updatePlayerPosition(speed);
    }

    protected synchronized void updateEnemy(double speed) {
        long now = System.currentTimeMillis();
        if(lastEnemyProcess+BETWEEN_TIME > now) {
            return;
        }
        lastEnemyProcess = now;
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).updateEnemyPosition(speed);
            System.out.println(speed);
        }
    }


    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        player.drawPlayer(canvas);
        for(Player enemy: enemies) {
            enemy.drawPlayer(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                pressed = true;
                if(event.getX() < getResources().getDisplayMetrics().widthPixels / 2) {
                    PLAYER_MOVEMENT_SPEED = -20.0;
                }
                else {
                    PLAYER_MOVEMENT_SPEED = 20;
                }
                break;
            case MotionEvent.ACTION_UP:
                pressed = false;
                PLAYER_MOVEMENT_SPEED = 0.0;
                break;
        }
        return true;
    }

    class GameThread extends Thread {
        private boolean pausa,corrent;

        public synchronized void pausar() {
            pausa=true;
        }

        public synchronized void reanudar() {
            pausa=false;
            notify();
        }

        public synchronized void aturar() {
            corrent = false;
            if(pausa)reanudar();
        }
        public void run() {
            corrent = true;
            while(corrent) {
                if(pressed) {
                    updatePlayer(PLAYER_MOVEMENT_SPEED);
                }
                updateEnemy(ENEMY_MOVEMENT_SPEED);
                synchronized (this) {
                    while(pausa) {
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }
}
