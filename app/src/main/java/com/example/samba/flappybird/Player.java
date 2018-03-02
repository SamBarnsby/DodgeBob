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
    private View view;

    public Player(View view, Drawable drawable) {
        this.view = view;
        this.drawable = drawable;
        amplada = drawable.getIntrinsicWidth();
        altura= drawable.getIntrinsicHeight();
    }


    public void updatePlayerPosition(double factor) {
        cenX += factor;
        //Si sortim de la pantalla corregim la posicio
        /*if(cenX<0) {cenX = view.getWidth();}
        if(cenX>view.getWidth()) {cenX=0;}
        if(cenY<0) {cenY = view.getHeight();}
        if(cenY>view.getHeight()) {cenY=0;}*/
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
