package com.example.kolas.mygame;

/**
 * Created by kolas on 07.11.2015.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Background {
    private Bitmap image;
    private int x, dx;
    public Background(Bitmap res) {
        image = res;
        dx = GamePanel.MOVESPEED;
    }


    public void update() {
        x = x + dx;
        if (x < -image.getWidth()) {
            x = 0;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, 0, null);
        canvas.drawBitmap(image, x + image.getWidth(), 0, null);


    }
}
