package com.game.kolas.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by kolas on 22.11.2015.
 */
public class Box extends GameObject {
    private int speed;
    Bitmap image;


    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Box(Bitmap res, int speed, boolean b) {
        BadOrGood = b;
        image = res;
        super.x = x;
        super.y = y;
//
        if (BadOrGood == false) {
            y = (710);
            x = 1000;
            this.speed = speed;

            if (speed > 40) speed = 40;

        } else {
            x = 1000;
            y = 270 + rand.nextInt(110);
            this.speed = speed;
            if (speed > 40) speed = 40;
        }

    }

    public void update() {
        x -= speed;
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(image, x, y, null);
        } catch (Exception e) {
        }
    }

    @Override
    public int getWidth() {
        return width - 10;
    }

}
