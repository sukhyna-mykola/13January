package com.game.kolas.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

import static com.game.kolas.mygame.GameSurface.HEIGHT;

/**
 * Created by kolas on 22.11.2015.
 */
public class Obstacle extends GameObject {
    private int speed;
    private Bitmap image;

    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;


    public Obstacle(Bitmap res, int speed, boolean b) {
        this.isBonus = b;
        this.image = res;
        this.speed = speed;
        this.height = 300;
        this.x = 1000;

        if (isBonus == false) {
            this.y = (height + MIN_Y_POSITION);

        } else {
            this.y = 270 + rand.nextInt(110);
        }

        if (this.speed > 40) this.speed = 40;
    }

    public void update() {
        x -= speed;
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(image, x, HEIGHT - y, null);
        } catch (Exception e) {
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public int getWidth() {
        return width - 10;
    }

}
