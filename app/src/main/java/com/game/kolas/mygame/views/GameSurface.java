package com.game.kolas.mygame.views;

import android.annotation.TargetApi;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.game.kolas.mygame.GameActivity;
import com.game.kolas.mygame.GameModel;
import com.game.kolas.mygame.objects.Obstacle;
import com.game.kolas.mygame.objects.Snowball;

import static android.graphics.BitmapFactory.*;

/**
 * Created by kolas on 07.11.2015.
 */
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 750;

    private Paint p;

    private GameModel model;


    public GameSurface(GameActivity gameActivity, GameModel model) {
        super(gameActivity);

        this.model = model;
        p = new Paint();
        p.setTextAlign(Paint.Align.CENTER);
        p.setTypeface(Typeface.createFromAsset(gameActivity.getAssets(), "fonts/myFont.ttf"));
       // p.setTypeface(Typeface.MONOSPACE);
        p.setAntiAlias(true);

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



            for (Obstacle obstacle  : model.getObstacles()) {
                if (obstacle .isVisibility())
                    obstacle .draw(canvas);
            }
            for (Obstacle bonus : model.getBonuses()) {
                if (bonus.isVisibility())
                    bonus.draw(canvas);
            }

            if (model.getPlayer().isVisibility())
                model.getPlayer().draw(canvas, p);

            if (model.getAdversary().isVisibility())
                model.getAdversary().draw(canvas, p);




            for (Snowball snowball : model.getSnowballs()) {
                if (snowball.isVisibility())
                    snowball.draw(canvas, p);
            }

            if (model.getNewLevel().isVisibility()) {
                model.getNewLevel().draw(canvas, p);
            }


        }
    }

}