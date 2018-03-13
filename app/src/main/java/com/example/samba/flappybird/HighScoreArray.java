package com.example.samba.flappybird;

import java.util.Vector;

/**
 * Created by samba on 06/12/2017.
 */

public class HighScoreArray {
    private Vector<String> scores;

    public HighScoreArray() {
        scores = new Vector<String>();
    }

    public void saveScore(int points, String name) {
        scores.add(0,points+" "+ name);
    }

    public Vector<String> pointList(int quantity) {
        return scores;
    }
}
