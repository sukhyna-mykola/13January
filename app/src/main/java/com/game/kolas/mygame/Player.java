package com.game.kolas.mygame;

/**
 * Created by kolas on 26.09.2015.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static com.game.kolas.mygame.DrawGame.FSP;
import static com.game.kolas.mygame.GameSurface.HEIGHT;


public class Player extends GameObject {

    public static final float DY_JUMP = (float) 0.6;
    private float heightJump = 144;

    boolean pause = false;

    private Animation animation = new Animation();
    private Bitmap spritesheet;

    private int energy;


    public Player(Bitmap res, int w, int h, int numFrames) {

        this.x = 100;
        this.y = h + MIN_Y_POSITION;
        this.height = h;
        this.width = w;
        this.energy = 100;

        Bitmap[] image = new Bitmap[numFrames];
        this.spritesheet = res;
        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(40);
    }


    public void jamp() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                animation.setPause(true);
                float dy = (float) -Math.sqrt(heightJump);

                while (y >= height + MIN_Y_POSITION) {
                    dy += DY_JUMP;
                    Log.d(TAG, "y = " + y);
                    while (pause)
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    //-Math.sqrt(heightJump)<dy<Math.sqrt(heightJump), де heightJump - висота стрибка
                    //графік - парабола
                    y = (height + MIN_Y_POSITION) + heightJump - (int) (Math.pow(dy, 2));

                    try {
                        Thread.sleep(FSP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                y = (height + MIN_Y_POSITION);
                animation.setPause(false);
            }
        });
        thread.start();


    }

    public int getEnergy() {
        return energy;
    }

    public void changeEnergy(int inc) {
        this.energy += inc;
    }

    public void update() {
        animation.update();
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, HEIGHT - y, null);
    }

}