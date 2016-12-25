package com.game.kolas.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by kolas on 07.11.2015.
 */
public class DrawGame extends Thread {
    private boolean runFlag;
    private  boolean pause;
    private SurfaceHolder surfaceHolder;
    public static Canvas canvas;
    private GamePanel gamePanel;


    public boolean isRunFlag() {
        return runFlag;
    }

    public void setRunFlag(boolean runFlag) {
        this.runFlag = runFlag;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }






    public DrawGame(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {


        while (runFlag) {
            gamePanel.time += 0.5;//0.175666;
            while (pause)
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {

                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            try {
                this.sleep(gamePanel.FSP);
            } catch (Exception e) {
            }


        }

    }
}

