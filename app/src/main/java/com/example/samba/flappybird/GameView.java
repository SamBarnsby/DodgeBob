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
import android.widget.ImageView;
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
    private int changePoints;
    boolean pressed = true;
    private View fullView;
    private LinearLayout pointslayout;
    private TextView pointsView;
    private ImageView lifeView;

    private static int BETWEEN_TIME = 50;
    private static int BETWEEN_TIME_POINTS = 60;

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

        lifeView = new ImageView(context);
        lifeView.setImageResource(R.drawable.health1);
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

        int  x = rand.nextInt(getResources().getDisplayMetrics().widthPixels-50) + 50;
        int  y = rand.nextInt(1 - 0 + 1) + 0;
        Drawable drawableEnemy;
        if(y == 0) {
            drawableEnemy = contexte.getResources().getDrawable(R.drawable.birdenemy);
        }
        else {
            drawableEnemy = contexte.getResources().getDrawable(R.drawable.ballenemy);
        }
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

    protected synchronized void checkEnemyPosition(Player enemy) {
        int height = getResources().getDisplayMetrics().heightPixels;
        if(!enemy.isDead()) {
            if (enemy.getCenY() == height + 150) {
                enemy.setDead(true);
                points += 100;
                if (NEXT_ENEMY_SECONDS == 1000) {
                    NEXT_ENEMY_SECONDS = 1500;
                    ENEMY_MOVEMENT_SPEED = 15.00;


                } else if (points == (changePoints + 800)) {
                    changePoints = points;
                    NEXT_ENEMY_SECONDS -= 1000;
                }
                pointsView.setText("POINTS: " + points);
            }
            if(enemy.verifyColision(player)) {
                thread.aturar();
            }
        }
    }


    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        lifeView.draw(canvas);
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
                for(int i = 0; i < enemies.size(); i++) {
                    checkEnemyPosition(enemies.get(i));
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
