package com.example.mitsichury.jpo2;

/**
 * Created by MITSICHURY on 23/09/2015.
 */
public class PointDessin {
    private int x;
    private int y;

    public PointDessin(int x, int y) {
        this.x = x;
        this.y = y;
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

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
}
