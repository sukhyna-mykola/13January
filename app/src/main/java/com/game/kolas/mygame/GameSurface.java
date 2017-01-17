package com.game.kolas.mygame;

import android.annotation.TargetApi;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import android.view.SurfaceHolder;
import android.view.SurfaceView;


import static android.graphics.BitmapFactory.*;

/**
 * Created by kolas on 07.11.2015.
 */
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    public static int WIDTH;
    public static int HEIGHT;

    Paint p = new Paint();

    public static final int MOVESPEED = -3;

    private GameModel model;


    public GameSurface(GameActivity gameActivity,GameModel model) {
        super(gameActivity);

        this.model = model;
        p.setTextSize(20);
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
            p.setColor(Color.WHITE);


            model.getBg().draw(canvas);
            model.getPlayer().draw(canvas);
            model.getVrag().draw(canvas);

            for (Box m : model.getMissiles()) {
                m.draw(canvas);
            }
//           if (SystemClock.elapsedRealtime() - chr.getBase() > (LevelActivity.LEVEL + 1) * 5000)
//                canvas.drawText("OPEN NEW LEVEL", 100, 200, p);

            switch (model.getNexdialog()) {
                case 0:
                    if (model.isShowdialogvrag()) {
                        canvas.drawBitmap(decodeResource(getResources(), R.drawable.padl), 100, 500, p);
                    }
                    if (model.isShowdialogplayer()) {
                        canvas.drawBitmap(decodeResource(getResources(), R.drawable.nax), 600, 500, p);
                    }
                    break;
                case 1:
                    if (model.isShowdialogvrag()) {
                        canvas.drawBitmap(decodeResource(getResources(), R.drawable.police), 100, 500, p);
                    }
                    if (model.isShowdialogplayer()) {
                        canvas.drawBitmap(decodeResource(getResources(), R.drawable.pox), 600, 500, p);
                    }
                    ;
                    break;
            }
            if (model.isShow_sn()) {
                p.setColor(new Color().WHITE);
                //  canvas.drawCircle(x_sn, y_sn, 10, p);
                p.setColor(new Color().BLACK);
            }
            if (model.isShowbla()) {
                canvas.drawBitmap(decodeResource(getResources(), R.drawable.pizd), 100, 500, p);
            }


        }
    }

}