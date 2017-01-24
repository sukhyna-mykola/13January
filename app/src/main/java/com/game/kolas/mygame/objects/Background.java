package com.game.kolas.mygame.objects;

/**
 * Created by kolas on 07.11.2015.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Background {
    private Bitmap [] images;
    int pos;
    int max ;
    private int x, dx;
    public Background(ArrayList<Bitmap> listBackgroundImages) {
        images  = new Bitmap[listBackgroundImages.size()];
        listBackgroundImages.toArray(images);
        max = listBackgroundImages.size();
        pos=0;
        dx = -3;
    }


    public void update() {
        x = x + dx;
        if (x < -images[pos].getWidth()) {
            pos++;
            pos%=max;
            x = 0;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(images[pos], x, 0, null);
        canvas.drawBitmap(images[(pos+1)%max], x + images[pos].getWidth(), 0, null);

    }
}
