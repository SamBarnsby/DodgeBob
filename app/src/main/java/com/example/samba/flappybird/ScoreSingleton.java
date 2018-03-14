package com.example.samba.flappybird;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by samue on 14/03/2018.
 */

public class ScoreSingleton {
    private List<Score> scoreArray;
    private HighScoreAdapter adapter;
    private static ScoreSingleton myInstance;
    private FirebaseAuth auth;

    private final static String DB_NAME = "dodgebobscore";
    private DatabaseReference databaseReference;

    private ScoreSingleton(Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        databaseReference = database.getReference().child(DB_NAME);
        auth = FirebaseAuth.getInstance();
        adapter = new HighScoreAdapter(context, databaseReference);
    }

    public static ScoreSingleton getInstance(Context context) {
        if(myInstance == null) {
            myInstance = new ScoreSingleton(context);
        }
        return myInstance;
    }

    public HighScoreAdapter getAdapter() {
        return adapter;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

}
