package com.game.kolas.mygame.objects;

/**
 * Created by kolas on 26.09.2015.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import static com.game.kolas.mygame.views.GameSurface.HEIGHT;


public class Player extends GameObject {

    public boolean isJumpFlag() {
        return jumpFlag;
    }

    public static final float DY_JUMP = 1;
    private float heightJump;

    boolean pause = false;

    private Animation animation;
    private Bitmap spritesheet;

    private float energy;
    private Message message;
    private int health;
    private boolean jumpFlag;
    private RectF rect;


    public Player(Bitmap res, int w, int h, int numFrames, int heightJump, int pauseFrame) {

        this.y = h + MIN_Y_POSITION;
        this.height = h;
        this.width = w;
        this.energy = 100;
        this.heightJump = heightJump;
        this.visibility = true;

        animation = new Animation(pauseFrame);


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

    public void setJumpFlag(boolean jumpFlag) {
        this.jumpFlag = jumpFlag;
    }


    public void resetDY() {
        dy = (float) -Math.sqrt(heightJump);

    }

    public Animation getAnimation() {
        return animation;
    }

    public void jump() {

        if (jumpFlag) {
            dy += DY_JUMP;
            //-Math.sqrt(heightJump)<dy<Math.sqrt(heightJump), де heightJump - висота стрибка
            //графік - парабола
            y = (height + MIN_Y_POSITION) + heightJump - (int) (Math.pow(dy, 2));

            if (y <= height + MIN_Y_POSITION) {
                y = (height + MIN_Y_POSITION);
                jumpFlag = false;
                animation.setPause(false);
            }
        }
    }


    public float getEnergy() {
        return energy;
    }

    public void addToEnergy(float inc) {
        this.energy += inc;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void update() {
        if (this.isVisibility()) {
            jump();

            animation.update();
            if (message.isVisibility()) {
                message.setX(x + width / 3 * 2);
                message.setY(y);
            }
        }
    }


    public void draw(Canvas canvas, Paint p) {
        canvas.drawBitmap(animation.getImage(), x, HEIGHT - y, null);

        rect = new RectF(x + (width / 2 - energy / 2), HEIGHT - (y + 30), x + (width / 2 + energy / 2), HEIGHT - (y + 10));

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
        p.setColor(Color.BLACK);
        p.setTextSize(15);
        canvas.drawText((int)(energy) + "%", x + width / 2, HEIGHT - (y + 15), p);
        if (message.isVisibility())
            message.draw(canvas, p);
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void addToX(float inc) {
        x += inc;
    }

    public void addToHealth(float inc) {
        health += inc;
    }
}