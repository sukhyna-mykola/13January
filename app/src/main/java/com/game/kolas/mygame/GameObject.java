package com.game.kolas.mygame;

/**
 * Created by mykola on 26.09.2015.
 */

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int dy;
    protected int dx;
    protected int width;
    protected int height;
    protected boolean BadOrGood;

    public void setX(int  x)
    {
        this.x = x;
    }
    public void setY(int y)
    {
        this.y = y;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public int getHeight()
    {
        return height;
    }
    public int getWidth()
    {
        return width;
    }

}
