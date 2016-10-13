package com.example.kolas.mygame;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Random;

import static android.graphics.BitmapFactory.*;

/**
 * Created by kolas on 07.11.2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    static int WIDTH;
    static int HEIGHT;
    static int FSP = 16;
    float x_sn = 500;
    float y_sn = 100;

    Paint p = new Paint();
    boolean showdialogvrag = false;
    boolean showbla = false;
    boolean show_sn = false;
    int nexdialog = 1;
    boolean showdialogplayer = false;
    int BoxSpeed = 11;
    public static final int MOVESPEED = -3;
    boolean catchJamp = false;
    private long timeWhenStopped = 0;
    Random r = new Random();
    float newGoodBox = 10000;
    long realTime = SystemClock.elapsedRealtime();
    long realTimeForGoodBox = SystemClock.elapsedRealtime();
    long realTimeForBar = SystemClock.elapsedRealtime();
    private float missileStartTime = (float) r.nextInt(700);
    private ArrayList<Box> missiles;
    private DrawGame thread;
    private Background bg;
    private Player player;
    private ProgressBar progressBar;
    ImageButton imageButton;
    Chronometer chr;
    Player vrag;

    float time;
    MainActivity ma;

    public GamePanel(Context context, Chronometer c, ProgressBar progressBar, MainActivity mainActivity, ImageButton imageButton) {

        super(context);

        this.progressBar = progressBar;
        ma = mainActivity;
        this.imageButton = imageButton;
        progressBar.setProgress(100);
        chr = c;
        p.setTextSize(20);
        WIDTH = 1000;
        HEIGHT = 750;
        thread = new DrawGame(getHolder(), this);
        getHolder().addCallback(this);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                catchJamp = true;

                try {
                    if (player.y > 520) {
                        player.setY(542);
                        player.jamp();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    void start() {
        if (thread.pause) {
            player.pause = false;
            vrag.pause = false;
            thread.pause = false;
            chr.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chr.start();
        }
    }


    void pause() {
        if (!thread.pause) {
            ma.time = String.valueOf(chr.getText());
            thread.pause = true;
            player.pause = true;
            vrag.pause = true;
            chr.stop();
            timeWhenStopped = chr.getBase() - SystemClock.elapsedRealtime();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player = new Player(decodeResource(getResources(), R.drawable.running), 175, 200, 4);
        vrag = new Player(decodeResource(getResources(), R.drawable.vrag), 200, 200, 4);
        bg = new Background(decodeResource(getResources(), R.drawable.fon));
        vrag.setX(0);
        player.setX(200);
        missiles = new ArrayList<Box>();

        thread.setRunning(true);
        thread.start();
        chr.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chr.start();


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }


    public boolean onTouchEvent(MotionEvent event) {
        catchJamp = false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            try {

                if (player.y > 520) {
                    player.setY(542);
                    player.jamp();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return super.onTouchEvent(event);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void update() {
        if (player.getX() < 400)
            player.x++;
        long thisTimeForBar = SystemClock.elapsedRealtime() - realTimeForBar;

        if (thisTimeForBar > 1000) {

            progressBar.incrementProgressBy(-2);
            realTimeForBar = SystemClock.elapsedRealtime();
        }


        long thisTime = SystemClock.elapsedRealtime() - realTime;
        if (thisTime > 20000) {
            if (r.nextBoolean())
                dialog();
            BoxSpeed++;

            realTime = SystemClock.elapsedRealtime();
        }


        if (missiles.size() == 0) {
            missiles.add(new Box(decodeResource(getResources(), R.drawable.per), BoxSpeed, false));
        } else if (missiles.get(missiles.size() - 1).getX() < missileStartTime) {

            missiles.add(new Box(decodeResource(getResources(), R.drawable.per), BoxSpeed, false));
            missileStartTime = (float) r.nextInt(800);
            
        } else if ((missiles.get(missiles.size() - 1).BadOrGood)) {
            if (missiles.get(missiles.size() - 2).getX() < missileStartTime) {
                missiles.add(new Box(decodeResource(getResources(), R.drawable.per), BoxSpeed, false));
                missileStartTime = (float) r.nextInt(800);
            }
        }


        long thisTimeForGoodBox = SystemClock.elapsedRealtime() - realTimeForGoodBox;
        if (thisTimeForGoodBox > newGoodBox) {
            missiles.add(new Box(decodeResource(getResources(), R.drawable.but), 5, true));
            if (progressBar.getProgress() < 30)
                newGoodBox = 30 * 100;
            else
                newGoodBox = progressBar.getProgress() * 100;
            realTimeForGoodBox = SystemClock.elapsedRealtime();
        }


        for (int i = 0; i < missiles.size(); i++) {
            //update missile
            missiles.get(i).update();
            if (MacroCollision(player, missiles.get(i))) {
                missiles.remove(i);
                thread.setRunning(false);
                ma.time = String.valueOf(chr.getText());
                if (StringToInt(ma.time) > StringToInt(ma.BestTime))
                    ma.BestTime = ma.time;

                ma.de.show(ma.getFragmentManager(), "end");
                break;

            }
            if (missiles.get(i).x < -50)
                missiles.remove(i);


        }

        if (progressBar.getProgress() <= 0) {
            thread.setRunning(false);
            ma.time = String.valueOf(chr.getText());
            if (StringToInt(ma.time) > StringToInt(ma.BestTime))
                ma.BestTime = ma.time;
            ma.de.show(ma.getFragmentManager(), "end");
        }
        player.update();
        vrag.update();
        bg.update();
    }

    int StringToInt(String s) {
        int i = Integer.parseInt(s.replace(':', '0'));
        return i;
    }

    public boolean MacroCollision(GameObject player, GameObject boxObj) {
        boolean XColl = false;
        boolean YColl = false;
        if (!boxObj.BadOrGood) {
            if ((player.getX() + 130 >= boxObj.getX()) && (player.getX() + 50 <= boxObj.getX() + 50))
                XColl = true;
            if ((player.getY() + 200 >= boxObj.getY()) && (player.getY() <= boxObj.getY() + 30))
                YColl = true;
        } else {
            if ((player.getX() + 140 >= boxObj.getX()) && (player.getX() + 20 <= boxObj.getX() + 100))
                XColl = true;
            if ((player.getY() + 200 >= boxObj.getY()) && (player.getY() <= boxObj.getY() + 100))
                YColl = true;
        }
        if ((XColl & YColl)) {
            if (!boxObj.BadOrGood) {
                chr.stop();
                return true;
            } else if (catchJamp) {
                if (r.nextBoolean())
                    flybox();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            try {
                                Thread.sleep(FSP);
                                progressBar.incrementProgressBy(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });
                thread.start();
                missiles.remove(boxObj);
                ;
                return false;
            }
        }


        return false;
    }

    void flybox() {
        show_sn = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (x_sn > 100) {

                    x_sn -= 1;
                    y_sn = 500 + (((float) 0.0075) * (x_sn - 300) * (x_sn - 300) - 300);

                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                x_sn = 500;
                show_sn = false;
                showdialogplayer = false;
                showdialogvrag = false;
                showbla = true;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showbla = false;
            }
        });
        thread.start();

    }

    void dialog() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                showdialogvrag = true;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showdialogvrag = false;
                showdialogplayer = true;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showdialogplayer = false;
            }
        });
        thread.start();
        nexdialog = r.nextInt(2);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void draw(Canvas canvas) {

        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            bg.draw(canvas);
            player.draw(canvas);
            vrag.draw(canvas);

            for (Box m : missiles) {
                m.draw(canvas);
            }
            switch (nexdialog) {
                case 0:
                    if (showdialogvrag) {
                        canvas.drawBitmap(decodeResource(getResources(), R.drawable.padl), 100, 500, p);
                    }
                    if (showdialogplayer) { // canvas.drawText("пішов нахуй",600,500,p);
                        canvas.drawBitmap(decodeResource(getResources(), R.drawable.nax), 600, 500, p);
                    }
                    break;
                case 1:
                    if (showdialogvrag) {
                        //  canvas.drawText("щас поліцію визву",100,500,p);
                        canvas.drawBitmap(decodeResource(getResources(), R.drawable.police), 100, 500, p);
                    }
                    if (showdialogplayer) {
                        //  canvas.drawText("та мені похуй",600,500,p);
                        canvas.drawBitmap(decodeResource(getResources(), R.drawable.pox), 600, 500, p);
                    }
                    ;
                    break;
            }
            if (show_sn) {
                p.setColor(new Color().WHITE);
                canvas.drawCircle(x_sn, y_sn, 10, p);
                p.setColor(new Color().BLACK);
            }
            if (showbla) {
                canvas.drawBitmap(decodeResource(getResources(), R.drawable.pizd), 100, 500, p);
            }
            canvas.restoreToCount(savedState);


        }
    }

}