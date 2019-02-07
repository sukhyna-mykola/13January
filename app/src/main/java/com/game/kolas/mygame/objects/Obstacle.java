package com.game.kolas.mygame.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

import static com.game.kolas.mygame.views.GameSurface.HEIGHT;

/**
 * Created by kolas on 22.11.2015.
 */
public class Obstacle extends GameObject {
    private float speed;
    private Bitmap image;
    protected boolean isBonus;

    private Random rand = new Random();


    public Obstacle(Bitmap res, float speed, boolean b) {
        this.isBonus = b;
        this.image = res;
        this.speed = speed;
        this.height = res.getHeight();
        this.x = 1000;
        this.visibility = true;

        if (isBonus == false) {
            this.y = (height + MIN_Y_POSITION);

        } else {
            this.y = 300 + rand.nextInt(150);
        }

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

    public boolean isBonus() {
        return isBonus;
    }
    @Override
    public int getWidth() {
        return width - 10;
    }

}
