package com.game.kolas.mygame;

import android.annotation.TargetApi;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;

import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.game.kolas.mygame.objects.Obstacle;
import com.game.kolas.mygame.objects.Snowball;

import static android.graphics.BitmapFactory.*;

/**
 * Created by kolas on 07.11.2015.
 */
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    public static int WIDTH;
    public static int HEIGHT;
    public static final int HEIGHT_DIALOG_GISPLAY = 500;

    private Paint p;

    public static final int MOVESPEED = -3;

    private GameModel model;
    private  RectF rect;


    public GameSurface(GameActivity gameActivity, GameModel model) {
        super(gameActivity);

        this.model = model;
        p = new Paint();
        p.setTextSize(20);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTypeface(Typeface.createFromAsset(gameActivity.getAssets(), "fonts/myFont.ttf"));

        WIDTH = 1000;
        HEIGHT = 750;

        getHolder().addCallback(this);


    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void update() {
        Canvas canvas = null;

        try {
            canvas = this.getHolder().lockCanvas();
            synchronized (getHolder()) {
                draw(canvas);
            }
        } catch (Exception e) {
        } finally {
            if (canvas != null) {
                try {
                    getHolder().unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void draw(Canvas canvas) {

        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);


        if (canvas != null) {
            canvas.scale(scaleFactorX, scaleFactorY);

            model.getBg().draw(canvas);

            model.getPlayer().draw(canvas);

            for (Obstacle m : model.getObstacles()) {
                m.draw(canvas);
            }

            if (model.isShow_sn()) {

                for (Snowball snowball : model.getSnowballs()) {
                    p.setColor(new Color().LTGRAY);
                    canvas.drawCircle(snowball.getX(), HEIGHT - snowball.getY(), 12, p);
                    p.setColor(new Color().WHITE);
                    canvas.drawCircle(snowball.getX(), HEIGHT - snowball.getY(), 10, p);

                }


            }
            if (model.getAdversary().getX()+model.getAdversary().getWidth()> 0) {

                model.getAdversary().draw(canvas);
                rect = new RectF(model.getAdversary().getHeight()/2-model.getAdversary().getEnergy()/2, HEIGHT - (model.getAdversary().getY() + 30)
                        , model.getAdversary().getHeight()/2+model.getAdversary().getEnergy()/2, HEIGHT - (model.getAdversary().getY() + 10));

                if (model.getAdversary().getEnergy() < 30) {
                    p.setColor(Color.RED);
                    canvas.drawRoundRect(rect, 5, 5, p);
                } else if (model.getAdversary().getEnergy() >= 30 && model.getAdversary().getEnergy() < 70) {
                    p.setColor(Color.YELLOW);
                    canvas.drawRoundRect(rect, 5, 5, p);
                } else if (model.getAdversary().getEnergy() >= 70) {
                    p.setColor(Color.GREEN);
                    canvas.drawRoundRect(rect, 5, 5, p);
                }

                if (model.isShowbla()) {
                    p.setColor(Color.BLACK);
                    canvas.drawBitmap(decodeResource(getResources(), R.drawable.template_dialog_text), model.getAdversary().getWidth()/2, HEIGHT_DIALOG_GISPLAY, p);
                    canvas.drawText("Тобi пиз*ець", model.getAdversary().getWidth()/2 + 120, HEIGHT_DIALOG_GISPLAY+28, p);
                }

                if(model.showDialog){
                    p.setColor(Color.BLACK);
                    if(model.isShowdialogplayer()){
                        canvas.drawBitmap(decodeResource(getResources(), R.drawable.template_dialog_text), model.getPlayer().getX()+model.getPlayer().getWidth(), HEIGHT_DIALOG_GISPLAY, p);
                        canvas.drawText(model.getDialogText(), model.getPlayer().getX()+model.getPlayer().getWidth()+120, HEIGHT_DIALOG_GISPLAY+28, p);

                    }else {
                        canvas.drawBitmap(decodeResource(getResources(), R.drawable.template_dialog_text), model.getAdversary().getWidth()/2, HEIGHT_DIALOG_GISPLAY, p);
                        canvas.drawText(model.getDialogText(), model.getAdversary().getWidth()/2 + 120, HEIGHT_DIALOG_GISPLAY+28, p);
                    }
                }

            }


        }
    }

}