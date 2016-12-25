package com.game.kolas.mygame;

/**
 * Created by kolas on 26.09.2015.
 */

import android.graphics.Bitmap;

public class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;
    boolean pause = false;

    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void setDelay(long d) {
        delay = d;
    }

    public void setFrame(int i) {
        currentFrame = i;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void update() {
        if (!pause) {
            long elapsed = (System.nanoTime() - startTime) / 1000000;

            if (elapsed > delay) {
                currentFrame++;
                startTime = System.nanoTime();
            }
            if (currentFrame == frames.length) {
                currentFrame = 0;
                playedOnce = true;
            }
        } else currentFrame = 2;
    }

    public Bitmap getImage() {
        return frames[currentFrame];
    }

    public int getFrame() {
        return currentFrame;
    }

    public boolean playedOnce() {
        return playedOnce;
    }
}