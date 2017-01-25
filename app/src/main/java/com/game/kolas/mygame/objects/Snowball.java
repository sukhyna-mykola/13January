package com.game.kolas.mygame.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

import static com.game.kolas.mygame.views.GameSurface.HEIGHT;

/**
 * Created by mykola on 18.01.17.
 */

public class Snowball extends GameObject {
    private float angle;
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

    public void draw(Canvas canvas, Paint p) {
        p.setColor(new Color().LTGRAY);
        canvas.drawCircle(x, HEIGHT - y, 12, p);
        p.setColor(new Color().WHITE);
        canvas.drawCircle(x, HEIGHT - y, 10, p);

    }

}
