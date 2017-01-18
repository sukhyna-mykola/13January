package com.game.kolas.mygame.level;

import com.game.kolas.mygame.GameObject;

import java.util.Random;

/**
 * Created by mykola on 18.01.17.
 */

public class Snowball extends GameObject {
    private  float angle;
    private boolean throwed;

    public boolean isThrowed() {
        return throwed;
    }

    public void setThrowed(boolean throwed) {
        this.throwed = throwed;
    }

    public Snowball() {
        angle = new Random().nextFloat();
    }

    public float getAngle() {
        return angle;
    }
}
