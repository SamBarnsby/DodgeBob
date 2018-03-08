package com.example.samba.flappybird;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * Created by samba on 23/01/2018.
 */

public class MusicService extends Service {
    MediaPlayer reproductor;

    @Override
    public void onCreate() {
        super.onCreate();
        //reproductor = MediaPlayer.create(this, R.raw.intro);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int idArranc) {
        super.onStartCommand(intent, flags, idArranc);
        reproductor.start();
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