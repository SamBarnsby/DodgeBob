package com.example.samba.flappybird;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by samba on 23/01/2018.
 */

public class MusicService extends Service {
    MediaPlayer reproductor;
    private boolean isRunning;

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int idArranc) {
        super.onStartCommand(intent, flags, idArranc);
        if(!isRunning) {
            isRunning = true;
            String song;
            Bundle extras = intent.getExtras();
            if (extras == null) {
                song = null;
            } else {
                song = extras.getString("song");
            }
            reproductor = MediaPlayer.create(this, getResources().getIdentifier(song,
                    "raw", getPackageName()));
            reproductor.setLooping(true);
            reproductor.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reproductor.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}