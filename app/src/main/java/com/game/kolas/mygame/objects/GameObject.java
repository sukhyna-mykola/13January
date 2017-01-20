package com.game.kolas.mygame.objects;

/**
 * Created by mykola on 26.09.2015.
 */

public abstract class GameObject {
    public static final int MIN_Y_POSITION = 10;
    protected float x;
    protected float y;
    protected float dy;
    protected float dx;
    protected int width;
    protected int height;
    protected boolean isBonus;

    public void setX(float  x)
    {
        this.x = x;
    }
    public void setY(float y)
    {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getDy() {
        return dy;
    }

    public float getDx() {
        return dx;
    }

    public int getHeight()
    {
        return height;
    }
    public int getWidth()
    {
        return width;
    }

    public boolean isBonus() {
        return isBonus;
    }
}
