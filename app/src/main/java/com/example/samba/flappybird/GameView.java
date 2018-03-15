package com.example.samba.flappybird;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    SharedPreferences pref;
    private static double PLAYER_MOVEMENT_SPEED = 0.0;
    private static double ENEMY_MOVEMENT_SPEED = 10.0;
    private int NEXT_ENEMY_SECONDS = 2000;
    private int sprite = 1;
    private boolean right = true;
    private long lastenemyspawn;
    private int points;
    private int changePoints;
    boolean pressed = true;
    private View fullView;
    private LinearLayout pointslayout;
    private TextView pointsView;
    private ImageView lifeView;
    private GameView gameView;
    private Activity father;
    private static int BETWEEN_TIME = 50;
    private static int BETWEEN_TIME_POINTS = 60;

    private long lastPlayerProcess = 0;
    private long lastEnemyProcess = 0;
    private long lastPointProcess = 0;

    private Vector<Player> enemies;
    private Player player;
    private Drawable drawablePlayer;
    private Player enemy;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        contexte = context;
        PLAYER_MOVEMENT_SPEED = 0.0;
        ENEMY_MOVEMENT_SPEED = 10.0;
        NEXT_ENEMY_SECONDS = 2000;
        BETWEEN_TIME = 50;
        BETWEEN_TIME_POINTS = 60;
        pref = PreferenceManager.getDefaultSharedPreferences(contexte);
        fullView = findViewById(R.id.GameView);
        pointslayout = new LinearLayout(context);
        pointsView = new TextView(context);
        pointsView.setText("POINTS: 0");
        pointsView.setTextSize(32);
        pointsView.setTextColor(Color.WHITE);
        pointslayout.addView(pointsView);

        gameView = findViewById(R.id.GameView);
        lifeView = new ImageView(context);
        lifeView.setImageResource(R.drawable.health1);
        drawablePlayer = context.getResources().getDrawable(R.drawable.player_right);
        enemies = new Vector<>();

        if(pref.getString("background", "1").equals("1")) {
            gameView.setBackgroundResource(R.drawable.backgroundgame);
        }
        else if(pref.getString("background", "1").equals("2")) {
            gameView.setBackgroundResource(R.drawable.cliff_background);
        }
        else if(pref.getString("background", "1").equals("3")) {
            gameView.setBackgroundResource(R.drawable.cave_background);
        }
        else if(pref.getString("background", "1").equals("4")) {
            gameView.setBackgroundResource(R.drawable.desert_background);
        }

        if(pref.getString("player", "1").equals("2")) {
           drawablePlayer  = context.getResources().getDrawable(R.drawable.knight_right);
           sprite = 2;
        }
        else if(pref.getString("player", "1").equals("3")) {
            drawablePlayer  = context.getResources().getDrawable(R.drawable.frog_right);
            sprite = 3;
        }

        player = new Player(this, drawablePlayer);
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
            if (enemy.getCenY() > height + 150) {
                enemy.setDead(true);
                points += 100;
                if (NEXT_ENEMY_SECONDS == 1000) {
                    ENEMY_MOVEMENT_SPEED = 15.00;


                } else if (points == (changePoints + 800)) {
                    changePoints = points;
                    NEXT_ENEMY_SECONDS -= 1000;
                    System.out.println(NEXT_ENEMY_SECONDS);
                }
                pointsView.setText("POINTS: " + points);
            }
            if(enemy.verifyColision(player)) {
                endGame();
            }
        }
    }

    private void endGame() {
        Bundle bundle = new Bundle();
        bundle.putInt("score", points);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        father.setResult(Activity.RESULT_OK, intent);
        father.finish();
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
                    right = false;
                }
                else {
                    PLAYER_MOVEMENT_SPEED = 20;
                    right = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                pressed = false;
                PLAYER_MOVEMENT_SPEED = 0.0;
                break;
        }
        return true;
    }

    public void setFather(Activity father) {
        this.father = father;
    }

    class GameThread extends Thread {
        private boolean pause,running;

        public synchronized void pause() {
            pause=true;
        }

        public synchronized void resumeThread() {
            pause=false;
            notify();
        }

        public synchronized void stopThread() {
            running = false;
            if(pause)resumeThread();
        }
        public void run() {
            running = true;
            while(running) {
                if(pressed) {
                    if(right) {
                        if(sprite == 1) {
                            drawablePlayer  = contexte.getResources().getDrawable(R.drawable.player_right);
                        }
                        else if(sprite == 2) {
                            drawablePlayer  = contexte.getResources().getDrawable(R.drawable.knight_right);
                        }
                        else if(sprite == 3) {
                            drawablePlayer  = contexte.getResources().getDrawable(R.drawable.frog_right);
                        }
                    }
                    else {
                        if(sprite == 1) {
                            drawablePlayer  = contexte.getResources().getDrawable(R.drawable.player_left);
                        }
                        else if(sprite == 2) {
                            drawablePlayer  = contexte.getResources().getDrawable(R.drawable.knight_left);
                        }
                        else if(sprite == 3) {
                            drawablePlayer  = contexte.getResources().getDrawable(R.drawable.frog_left);
                        }
                    }
                    player.setDrawable(drawablePlayer);
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
                    while(pause) {
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    public GameThread getThread() {
        return thread;
    }
}
