package com.game.kolas.mygame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Random;

import static android.graphics.BitmapFactory.decodeResource;

/**
 * Created by mykola on 17.01.17.
 */

public class GameModel {
    private ArrayList<Box> missiles;
    private Background bg;
    private Player player;
    private Player vrag;

    private int progress;

    private int gameStatus;
    Random r = new Random();

    float x_sn = 500;
    float y_sn = 100;


    public static final int STATUS_GAMING = 0;
    public static final int STATUS_END = 1;

    boolean showdialogvrag = false;
    boolean showbla = false;
    boolean show_sn = false;
    int nexdialog = 1;
    boolean showdialogplayer = false;

    public boolean isCatchJamp() {
        return catchJamp;
    }

    public void setCatchJamp(boolean catchJamp) {
        this.catchJamp = catchJamp;
    }

    public boolean isShowdialogvrag() {

        return showdialogvrag;
    }

    public boolean isShowbla() {
        return showbla;
    }

    public boolean isShow_sn() {
        return show_sn;
    }

    public boolean isShowdialogplayer() {
        return showdialogplayer;
    }

    public int getNexdialog() {

        return nexdialog;
    }

    boolean catchJamp = false;

    long realTime = SystemClock.elapsedRealtime();
    long realTimeForGoodBox = SystemClock.elapsedRealtime();
    long realTimeForBar = SystemClock.elapsedRealtime();
    private float missileStartTime = (float) r.nextInt(700);

    private Resources resources;

    private int BoxSpeed = 11;

    private float newGoodBox = 10000;

    public ArrayList<Box> getMissiles() {
        return missiles;
    }

    public Background getBg() {
        return bg;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getVrag() {
        return vrag;
    }

    public int getProgress() {
        return progress;
    }

    public GameModel(Resources resources) {
        this.resources = resources;
        player = new Player(decodeResource(resources, R.drawable.running), 175, 200, 4);
        vrag = new Player(decodeResource(resources, R.drawable.vrag), 200, 200, 4);
        ArrayList<Bitmap> listImages = new ArrayList<>();
        listImages.add(decodeResource(resources, R.drawable.fon));
        listImages.add(decodeResource(resources, R.drawable.fon_start));
        bg = new Background(listImages);
        progress = 100;
        vrag.setX(0);
        player.setX(200);
        missiles = new ArrayList<Box>();
        gameStatus = STATUS_GAMING;


    }

    public int getGameStatus() {
        return gameStatus;
    }

    public void update() {
        if (player.getX() < 400)
            player.x++;
        long thisTimeForBar = SystemClock.elapsedRealtime() - realTimeForBar;

        if (thisTimeForBar > 1000) {
            progress = progress - 2;
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
            missiles.add(new Box(decodeResource(resources, R.drawable.per), BoxSpeed, false));
        } else if (missiles.get(missiles.size() - 1).getX() < missileStartTime) {

            missiles.add(new Box(decodeResource(resources, R.drawable.per), BoxSpeed, false));
            missileStartTime = (float) r.nextInt(800);

        } else if ((missiles.get(missiles.size() - 1).BadOrGood)) {
            if (missiles.get(missiles.size() - 2).getX() < missileStartTime) {
                missiles.add(new Box(decodeResource(resources, R.drawable.per), BoxSpeed, false));
                missileStartTime = (float) r.nextInt(800);
            }
        }


        long thisTimeForGoodBox = SystemClock.elapsedRealtime() - realTimeForGoodBox;

        if (thisTimeForGoodBox > newGoodBox) {
            missiles.add(new Box(decodeResource(resources, R.drawable.but), 5, true));
            if (progress < 30)
                newGoodBox = 30 * 100;
            else
                newGoodBox = progress * 100;
            realTimeForGoodBox = SystemClock.elapsedRealtime();
        }


        for (int i = 0; i < missiles.size(); i++) {
            //update missile
            missiles.get(i).update();
            if (MacroCollision(player, missiles.get(i))) {
                missiles.remove(i);
                gameStatus = STATUS_END;

                break;

            }
            if (missiles.get(i).x < -50)
                missiles.remove(i);


        }

        if (progress <= 0) {
            gameStatus = STATUS_END;

        }
        player.update();
        vrag.update();
        bg.update();
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
                gameStatus = STATUS_END;
                return true;
            } else if (catchJamp) {
                if (r.nextBoolean())
                    flybox();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            try {
                                Thread.sleep(16);
                                progress++;
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
}
