package com.game.kolas.mygame;

/**
 * Created by kolas on 26.09.2015.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Player extends GameObject {
    private Bitmap spritesheet;
    private int score;
    private int PlayerJampFSP = 11;
    boolean startJamp = true;
    private boolean up;
    private boolean playing;
    boolean pause = false;
    private Animation animation = new Animation();
    Bitmap bitmap;
    private long startTime;


    public Player(Bitmap res) {
        bitmap = res;
        x = 100;
        y = 542;
        height = 200;
        width = 179;
        dy = 0;
        score = 0;
        up = false;

    }

    public Player(Bitmap res, int w, int h, int numFrames) {

        x = 100;
        y = 542;
        dy = 0;
        score = 0;
        height = h;
        width = w;
        up = true;
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(40);
        startTime = System.nanoTime();

    }

    public void setUp(boolean b) {
        up = b;
    }

    public void jamp() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                animation.setPause(true);
                float dyu = -12;
                // up=false;
                while (y <= 542) {

                    while (pause)
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    y = (int) (Math.pow(dyu, 2)) + 542 - 12 * 12;
                    dyu += 0.35;
                    try {
                        Thread.sleep(PlayerJampFSP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                animation.setPause(false);
            }
        });
        thread.start();


    }

    public void update() {
        animation.update();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

}