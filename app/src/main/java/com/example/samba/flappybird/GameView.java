package com.example.samba.flappybird;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private int NEXT_ENEMY_SECONDS = 4000;
    private long lastenemyspawn;
    private int points;
    boolean pressed = true;
    private View fullView;
    private LinearLayout pointslayout;
    private TextView pointsView;

    private static int BETWEEN_TIME = 50;
    private static int BETWEEN_TIME_POINTS = 30;

    private long lastPlayerProcess = 0;
    private long lastEnemyProcess = 0;
    private long lastPointProcess = 0;

    private Vector<Player> enemies;
    private Player player;
    private Player enemy;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        contexte = context;
        fullView = findViewById(R.id.GameView);
        pointslayout = new LinearLayout(context);
        pointsView = new TextView(context);
        pointsView.setText("POINTS: 0");
        pointsView.setTextSize(32);
        pointsView.setTextColor(Color.WHITE);
        pointslayout.addView(pointsView);

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
        lastenemyspawn = System.currentTimeMillis();
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
        }
    }

    protected synchronized void checkEnemyPosition() {
        long now = System.currentTimeMillis();
        if(lastPointProcess+BETWEEN_TIME_POINTS > now) {
            return;
        }
        lastPointProcess = now;
        int height = getResources().getDisplayMetrics().heightPixels;
        for(int i = 0; i < enemies.size(); i++) {
            if(enemies.get(i).getCenY() == height + 150) {
                points += 100;
                pointsView.setText("POINTS: " + points);
                System.out.println(points);
            }
        }
    }


    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        player.drawPlayer(canvas);
        for(Player enemy: enemies) {
            enemy.drawPlayer(canvas);
        }
        pointslayout.removeView(pointsView);
        pointslayout.addView(pointsView);
        pointslayout.measure(canvas.getWidth(), canvas.getHeight());
        pointslayout.layout(0,0,canvas.getWidth(), canvas.getHeight());
        pointslayout.draw(canvas);
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
                if((System.currentTimeMillis() - lastenemyspawn) > NEXT_ENEMY_SECONDS) {
                    addEnemy();
                }
                checkEnemyPosition();
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
