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
    private ArrayList<Obstacle> obstacles;
    private Background bg;
    private Player player;
    private Player adversary;


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
    private float obstacleStartTime = (float) r.nextInt(700);

    private Resources resources;

    private int BoxSpeed = 11;

    private float newGoodBox = 10000;

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public Background getBg() {
        return bg;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getAdversary() {
        return adversary;
    }

    public int getProgress() {
        return player.getEnergy();
    }

    public GameModel(Resources resources) {
        this.resources = resources;
        player = new Player(decodeResource(resources, R.drawable.running), 175, 200, 4);
        adversary = new Player(decodeResource(resources, R.drawable.adversary), 200, 200, 4);
        ArrayList<Bitmap> listImages = new ArrayList<>();
        listImages.add(decodeResource(resources, R.drawable.fon));
        listImages.add(decodeResource(resources, R.drawable.fon_start));
        bg = new Background(listImages);
        adversary.setX(0);
        player.setX(200);
        obstacles = new ArrayList<Obstacle>();
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
            player.changeEnergy(-2);
            realTimeForBar = SystemClock.elapsedRealtime();
        }


        long thisTime = SystemClock.elapsedRealtime() - realTime;
        if (thisTime > 20000) {
            if (r.nextBoolean())
                dialog();
            BoxSpeed++;

            realTime = SystemClock.elapsedRealtime();
        }


        if (obstacles.size() == 0) {
            obstacles.add(new Obstacle(decodeResource(resources, R.drawable.per), BoxSpeed, false));
        } else if (obstacles.get(obstacles.size() - 1).getX() < obstacleStartTime) {

            obstacles.add(new Obstacle(decodeResource(resources, R.drawable.per), BoxSpeed, false));
            obstacleStartTime = (float) r.nextInt(800);

        } else if ((obstacles.get(obstacles.size() - 1).isBonus)) {
            if (obstacles.get(obstacles.size() - 2).getX() < obstacleStartTime) {
                obstacles.add(new Obstacle(decodeResource(resources, R.drawable.per), BoxSpeed, false));
                obstacleStartTime = (float) r.nextInt(800);
            }
        }


        long thisTimeForGoodBox = SystemClock.elapsedRealtime() - realTimeForGoodBox;

        if (thisTimeForGoodBox > newGoodBox) {
            obstacles.add(new Obstacle(decodeResource(resources, R.drawable.but), 5, true));
            if (player.getEnergy() < 30)
                newGoodBox = 30 * 100;
            else
                newGoodBox = player.getEnergy() * 100;
            realTimeForGoodBox = SystemClock.elapsedRealtime();
        }


        for (int i = 0; i < obstacles.size(); i++) {
            //update obstacle
            obstacles.get(i).update();
            if (MacroCollision(player, obstacles.get(i))) {
                obstacles.remove(i);
                gameStatus = STATUS_END;
                break;

            }
            if (obstacles.get(i).x < -50)
                obstacles.remove(i);


        }

        if (player.getEnergy() <= 0) {
            gameStatus = STATUS_END;

        }
        player.update();
        adversary.update();
        bg.update();
    }

    public boolean MacroCollision(final GameObject playerObj, GameObject boxObj) {
        boolean XColl = false;
        boolean YColl = false;
        //Зміна метода через зміну системи координат
        if (!boxObj.isBonus) {
            if ((playerObj.getX() + 130 >= boxObj.getX()) && (playerObj.getX() + 50 <= boxObj.getX() + 50))
                XColl = true;
            if ((playerObj.getY() -playerObj.getHeight() <= boxObj.getY()) && (playerObj.getY() >= boxObj.getY() - 30))
                YColl = true;
        } else {
            if ((playerObj.getX() + 140 >= boxObj.getX()) && (playerObj.getX() + 20 <= boxObj.getX() + 100))
                XColl = true;
            if ((playerObj.getY() - playerObj.getHeight() <= boxObj.getY()) && (playerObj.getY() >= boxObj.getY() - 100))
                YColl = true;
        }
        if ((XColl & YColl)) {
            if (!boxObj.isBonus) {
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
                                player.changeEnergy(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });
                thread.start();
                obstacles.remove(boxObj);
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
