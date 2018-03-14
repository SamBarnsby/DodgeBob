package com.example.samba.flappybird;

/**
 * Created by samue on 14/03/2018.
 */

public class Score {
    private int score;
    private String name;

    Score(){}

    public Score(int score, String name) {
        this.score = score;
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return score + " " + name;
    }
}
