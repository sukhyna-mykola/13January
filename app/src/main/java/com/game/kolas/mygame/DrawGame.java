package com.game.kolas.mygame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by kolas on 07.11.2015.
 */
public class DrawGame extends Thread {
    private boolean runFlag;
    private boolean pause;
    public static int FSP = 16;

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

        while (runFlag) {
            while (pause)
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            gameActivity.update();

            try {
                this.sleep(FSP);
            } catch (Exception e) {
            }


        }

    }
}

