package com.example.mitsichury.jpo2;

/**
 * Created by MITSICHURY on 23/09/2015.
 */
public class Carre {
    private int x = 50;
    private int y = 50;
    private int taille = 10;
    private int color;

    public Carre(int x, int y, int taille, int color) {
        this.x = x;
        this.y = y;
        this.taille = taille;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
}
