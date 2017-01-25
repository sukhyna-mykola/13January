package com.game.kolas.mygame;

import android.graphics.Canvas;
import android.icu.text.SymbolTable;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

import static android.content.ContentValues.TAG;

/**
 * Created by kolas on 07.11.2015.
 */
public class DrawGame extends Thread {
    private static final String TAG = "TAG";
    private boolean runFlag;
    private boolean pause;
    public static int FPS = 40;


    private GameActivity gameActivity;


    public DrawGame(GameActivity gameActivity) {
        super();
        this.gameActivity = gameActivity;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (runFlag) {
            startTime = System.currentTimeMillis();
            gameActivity.update();
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            Log.d(TAG,"sleeTime = "+sleepTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
            } catch (Exception e) {}

            while (pause) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startTime = System.currentTimeMillis();
            }

        }
        Log.d(TAG,"end");

    }
}

