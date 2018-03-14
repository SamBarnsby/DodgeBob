package com.example.samba.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.q42.android.scrollingimageview.ScrollingImageView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bScore;
    private Button bPlay;
    private Button bSettings;
    private Button bExit;

   private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bScore = findViewById(R.id.scores);
        bPlay = findViewById(R.id.start);
        bSettings = findViewById(R.id.settings);
        bExit = findViewById(R.id.exit);
        bScore.setOnClickListener(this);
        bPlay.setOnClickListener(this);
        bSettings.setOnClickListener(this);
        startAnimations();
        auth = ScoreSingleton.getInstance(this).getAuth();
    }

    public void startAnimations() {
        ImageView imageView = findViewById(R.id.logo);
        ImageView player = findViewById(R.id.player);
        Animation logoAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
        Animation buttonLeftAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_left);
        Animation buttonRightAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_right);
        Animation playerAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.player_run);
        imageView.startAnimation(logoAnimation);
        bPlay.startAnimation(buttonLeftAnimation);
        bSettings.startAnimation(buttonLeftAnimation);
        bScore.startAnimation(buttonRightAnimation);
        bExit.startAnimation(buttonRightAnimation);
        player.setVisibility(View.VISIBLE);
        player.startAnimation(playerAnimation);
        player.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.scores) launchScore();
        if(v.getId() == R.id.start) launchPlay();
        if(v.getId() == R.id.settings) launchSettings();
        //if(v.getId() == R.id.exit) exit();
    }

    /*public void exit() {

    }*/

    public void launchScore() {
        bScore.setBackgroundResource(R.drawable.pressed);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(MainActivity.this,Scores.class);
                startActivity(i);
            }
        }, 200 );
    }

    public void launchSettings() {
        bSettings.setBackgroundResource(R.drawable.pressed);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(i);
            }
        }, 200 );
    }

    public void launchPlay() {
        bPlay.setBackgroundResource(R.drawable.pressed);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(MainActivity.this,Game.class);
                startActivityForResult(i, 1234);
            }
        }, 200 );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234 && resultCode == RESULT_OK && data != null) {
            int score = data.getExtras().getInt("score");
            saveScore(score);
        }
        else if(requestCode == 123) {
            if(resultCode == ResultCodes.OK) {

            }
        }
    }

    public void saveScore(int score) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null) {
            String name = firebaseUser.getDisplayName();
            String email = firebaseUser.getEmail();
            String provider = firebaseUser.getProviders().get(0);
            SharedPreferences pref = getSharedPreferences("com.example.samba.flappybird_internal", MODE_PRIVATE);
            pref.edit().putString("provider", provider).commit();
            pref.edit().putString("name", name).commit();
            if(email != null) {
                pref.edit().putString("email", email).commit();
            }
            ScoreSingleton.getInstance(this).getDatabaseReference().push().setValue(new Score(score, name));
            Intent i = new Intent(this, Scores.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        } else {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())).setIsSmartLockEnabled(true).build(), 123);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bPlay.setBackgroundResource(R.drawable.boto);
        bSettings.setBackgroundResource(R.drawable.boto);
        bScore.setBackgroundResource(R.drawable.boto);
        //bExit.setBackgroundResource(R.drawable.boto);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("music", true) == true) {
            startService(new Intent(MainActivity.this, MusicService.class));
        }
        else {
            stopService(new Intent(MainActivity.this, MusicService.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, MusicService.class));
    }
}
