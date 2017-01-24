package com.game.kolas.mygame.objects;

/**
 * Created by kolas on 26.09.2015.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static com.game.kolas.mygame.DrawGame.FSP;
import static com.game.kolas.mygame.views.GameSurface.HEIGHT;


public class Player extends GameObject {

    public static final float DY_JUMP = (float) 0.6;
    private float heightJump;

    boolean pause = false;

    private Animation animation = new Animation();
    private Bitmap spritesheet;

    private int energy;
    private Message message;
    private int health;


    public Player(Bitmap res, int w, int h, int numFrames,int heightJump) {

        this.y = h + MIN_Y_POSITION;
        this.height = h;
        this.width = w;
        this.energy = 100;
        this.heightJump = heightJump;
        this.visibility = true;
       

        Bitmap[] image = new Bitmap[numFrames];
        this.spritesheet = res;
        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(40);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void jump() throws InterruptedException {
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

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void update() {
        if(this.isVisibility()){
        animation.update();
            if(message.isVisibility()){
                message.setX(x+width/3*2);
                message.setY(y);
            }
        }
    }


    public void draw(Canvas canvas, Paint p) {
        canvas.drawBitmap(animation.getImage(), x, HEIGHT - y, null);


       RectF rect = new RectF(x+(width / 2 - energy / 2), HEIGHT - (y + 30), x+(width / 2 + energy / 2), HEIGHT - (y + 10));

        if (energy < 30) {
            p.setColor(Color.RED);
            canvas.drawRoundRect(rect, 5, 5, p);
        } else if (energy >= 30 && energy < 70) {
            p.setColor(Color.YELLOW);
            canvas.drawRoundRect(rect, 5, 5, p);
        } else if (energy >= 70) {
            p.setColor(Color.GREEN);
            canvas.drawRoundRect(rect, 5, 5, p);
        }
        if(message.isVisibility())
            message.draw(canvas,p);
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void incX(int inc){
        x+=inc;
    }
}