package com.example.mitsichury.jpo2;

/**
 * Created by MITSICHURY on 23/09/2015.
 */
public class PointDessin {
    private int x;
    private int y;
    private int color;

    public PointDessin(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {

        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getColor() {
        return color;
    }
}
