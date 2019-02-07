package com.game.kolas.mygame.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static android.graphics.BitmapFactory.decodeResource;
import static com.game.kolas.mygame.views.GameSurface.HEIGHT;


/**
 * Created by mykola on 21.01.17.
 */

public class Message extends GameObject {
    private Bitmap background;
    private String text;
    private  int shiftX;
    private  int shiftY;

    public Message(Bitmap background, int x, int y, int width, int height, int shiftX,int shiftY) {
        this.background = background;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.shiftX = shiftX;
        this.shiftY = shiftY;

        visibility = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public void draw(Canvas canvas,Paint p) {
        p.setColor(Color.BLACK);
        p.setTextSize(10);


        while (p.measureText(text)<(width-shiftX-35))
            p.setTextSize((float) (p.getTextSize()+0.5));


        canvas.drawBitmap(background, x, HEIGHT-(y+height), null);
        canvas.drawText(text, (x+width/2+shiftX), HEIGHT - (y+height/2+shiftY), p);
    }

}
