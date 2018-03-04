package com.example.samba.flappybird;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by samba on 12/02/2018.
 */

public class Player {
    private Drawable drawable;
    private int cenX, cenY;
    private int amplada, altura;
    private int xAnterior, yAnterior;
    private int colisionRadius;
    private View view;
    private boolean isDead;

    public Player(View view, Drawable drawable) {
        this.view = view;
        this.drawable = drawable;
        amplada = drawable.getIntrinsicWidth();
        altura= drawable.getIntrinsicHeight();
        colisionRadius=(altura+amplada)/4;
        isDead = false;
    }


    public void updatePlayerPosition(double factor) {
        if(!((cenX+factor<30) || (cenX+factor > view.getWidth()-30))) {
            cenX += factor;
        }
    }

    public void updateEnemyPosition(double factor) {
        cenY += factor;
    }

    public void drawPlayer(Canvas canvas) {
        int x = cenX-amplada/2;
        int y = cenY-altura/2;
        drawable.setBounds(x, y, x+amplada, y+altura);
        canvas.save();
        drawable.draw(canvas);
        canvas.restore();
        view.invalidate();
    }

    public double distance(Player g) {
        return Math.hypot(cenX-g.cenX, cenY-g.cenY);
    }

    public boolean verifyColision(Player g) {
        return (distance(g) < (colisionRadius+g.colisionRadius)-25);
    }

    public int getColisionRadius() {
        return colisionRadius;
    }

    public void setColisionRadius(int colisionRadius) {
        this.colisionRadius = colisionRadius;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getCenX() {
        return cenX;
    }

    public void setCenX(int cenX) {
        this.cenX = cenX;
    }

    public int getCenY() {
        return cenY;
    }

    public void setCenY(int cenY) {
        this.cenY = cenY;
    }

    public int getAmplada() {
        return amplada;
    }

    public void setAmplada(int amplada) {
        this.amplada = amplada;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getxAnterior() {
        return xAnterior;
    }

    public void setxAnterior(int xAnterior) {
        this.xAnterior = xAnterior;
    }

    public int getyAnterior() {
        return yAnterior;
    }

    public void setyAnterior(int yAnterior) {
        this.yAnterior = yAnterior;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
