package com.game.kolas.mygame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.SoundPool;

import com.game.kolas.mygame.objects.GameObject;
import com.game.kolas.mygame.objects.Obstacle;
import com.game.kolas.mygame.objects.Player;
import com.game.kolas.mygame.objects.Snowball;

import java.util.ArrayList;
import java.util.Random;

import static android.graphics.BitmapFactory.decodeResource;
import static com.game.kolas.mygame.DataGame.levels;

/**
 * Created by mykola on 17.01.17.
 */

public class GameModel {
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Snowball> snowballs;
    private Background bg;
    private Player player;
    private Player adversary;

    private int gameStatus;
    private int countSnowbolls;
    Random r = new Random();

    public static final int SOUND_JUMP_STREAM = 0;
    public static final int SOUND_THROW_STREAM = 1;
    public static final int SOUND_DONE_THROW_STREAM = 2;
    public static final int SOUND_RUN_STREAM = 3;
    public static final int SOUND_BONUS_STREAM = 4;

    public int SOUND_JUMP_ID;
    public int SOUND_THROW_ID;
    public int SOUND_DONE_THROW_ID;
    public int SOUND_RUN_ID;
    public int SOUND_BONUS_ID;

    public static final int STATUS_GAMING = 0;
    public static final int STATUS_END = 1;

    boolean showDialog;
    boolean showbla;
    boolean show_sn;

    boolean showDialogPlayer;
    private String dialogText;
    private int level;

    private float obstacleStartTime;
    boolean catchJamp;
    private long takts;
    long incObstaclesSpeepTime;
    long addNewBonusTime;
    long decEnergyTime;
    private Resources resources;
    private int obstaclesSpeed;
    private float whenNewBonusTime;

    private SoundPool sp;

    public String getDialogText() {
        return dialogText;
    }

    public boolean isCatchJamp() {
        return catchJamp;
    }

    public void setCatchJamp(boolean catchJamp) {
        this.catchJamp = catchJamp;
    }

    public boolean isShowdialog() {

        return showDialog;
    }

    public boolean isShowbla() {
        return showbla;
    }

    public boolean isShow_sn() {
        return show_sn;
    }

    public boolean isShowdialogplayer() {
        return showDialogPlayer;
    }


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

    public ArrayList<Snowball> getSnowballs() {
        return snowballs;
    }

    public GameModel(Context context, int level, SoundPool sp) {
        this.resources = context.getResources();
        this.level = level;
        this.sp = sp;
        SOUND_JUMP_ID = sp.load(context, R.raw.jump, SOUND_JUMP_STREAM);
        SOUND_BONUS_ID = sp.load(context, R.raw.bonus, SOUND_BONUS_STREAM);
        SOUND_THROW_ID = sp.load(context, R.raw.throw_snowball, SOUND_THROW_STREAM);
        SOUND_DONE_THROW_ID = sp.load(context, R.raw.done_throw, SOUND_DONE_THROW_STREAM);

        switch (level) {
            case 0:
                SOUND_RUN_ID = sp.load(context, R.raw.run, SOUND_RUN_STREAM);
                break;
            case 1:
                SOUND_RUN_ID = sp.load(context, R.raw.bike, SOUND_RUN_STREAM);
                break;
            case 2:
                SOUND_RUN_ID = sp.load(context, R.raw.run, SOUND_RUN_STREAM);
                break;
        }
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                if (i == SOUND_RUN_ID)
                    soundPool.play(SOUND_RUN_ID, 0.8f, 0.8f, 5, -1, 1);
            }
        });

        initGameData();

    }

    public int getGameStatus() {
        return gameStatus;
    }

    public void jump(Player obj) {
        sp.play(SOUND_JUMP_ID, 1, 1, 1, 0, 1);
        try {
            //умова для попередження безкінечного стрибка
            if (obj.getY() < obj.getHeight() + 30) {
                obj.setY(obj.getHeight() + 10);
                obj.jump();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void initGameData() {

        switch (level) {
            case 0:
                adversary = new Player(decodeResource(resources, R.drawable.adversary_level1), 200, 200, 4, 100);
                player = new Player(decodeResource(resources, R.drawable.player_level1), 175, 200, 4, 144);
                adversary.setX(0);
                player.setX(200);
                break;
            case 1:
                adversary = new Player(decodeResource(resources, R.drawable.adversary_level2), 266, 200, 3, 0);
                player = new Player(decodeResource(resources, R.drawable.player_level2), 175, 200, 4, 144);
                adversary.setX(0);
                player.setX(266);
                break;
            case 2:
                adversary = new Player(decodeResource(resources, R.drawable.adversaty_level3), 220, 200, 4, 100);
                player = new Player(decodeResource(resources, R.drawable.player_level3), 175, 200, 4, 144);
                adversary.setX(0);
                player.setX(200);
                break;
        }

        ArrayList<Bitmap> listImages = new ArrayList<>();
        listImages.add(decodeResource(resources, R.drawable.first));
        listImages.add(decodeResource(resources, R.drawable.two));
        listImages.add(decodeResource(resources, R.drawable.three));
        bg = new Background(listImages);

        obstacles = new ArrayList<>();
        snowballs = new ArrayList<>();
        gameStatus = STATUS_GAMING;

        showDialog = false;
        showbla = false;
        show_sn = false;

        showDialogPlayer = false;
        obstacleStartTime = (float) r.nextInt(700);
        catchJamp = false;
        takts = 0;
        incObstaclesSpeepTime = 0;
        addNewBonusTime = 0;
        decEnergyTime = 0;
        obstaclesSpeed = 15 - level;
        whenNewBonusTime = 0;
        countSnowbolls = 0;

    }

    public void update() {

        if (player.getX() < player.getEnergy() * 2 + 200)
            player.incX(1);
        else
            player.incX(-1);

        //зменшення еергії кожну секунду
        long checkTime = takts - decEnergyTime;
        if (checkTime > 13) {//~1s
            player.changeEnergy(-1);
            decEnergyTime = takts;
        }

        //збільшення швидкості перешкод кожні 20с.
        checkTime = takts - incObstaclesSpeepTime;
        if (checkTime > 800) {//~20s.
            dialog();
            if (obstaclesSpeed > 10)
                obstaclesSpeed--;
            incObstaclesSpeepTime = takts;
        }

        //Додавання нової перешкоди
        if (obstacles.size() == 0) {
            obstacles.add(new Obstacle(decodeResource(resources, R.drawable.obstacle), obstaclesSpeed, false));
        } else if (obstacles.get(obstacles.size() - 1).getX() < obstacleStartTime) {
            obstacles.add(new Obstacle(decodeResource(resources, R.drawable.obstacle), obstaclesSpeed, false));
            obstacleStartTime = (float) r.nextInt(800);

        } else if ((obstacles.get(obstacles.size() - 1).isBonus())) {
            if (obstacles.get(obstacles.size() - 2).getX() < obstacleStartTime) {
                obstacles.add(new Obstacle(decodeResource(resources, R.drawable.wood), obstaclesSpeed, false));
                obstacleStartTime = (float) r.nextInt(800);
            }
        }

        //бонус
        checkTime = takts - addNewBonusTime;
        if (checkTime > whenNewBonusTime) {

            obstacles.add(new Obstacle(decodeResource(resources, R.drawable.beer), 5, true));
            if (player.getEnergy() < 30)
                whenNewBonusTime = 200;
            else
                whenNewBonusTime = player.getEnergy() * 2 + 200;
            addNewBonusTime = takts;
        }


        for (int i = 0; i < obstacles.size(); i++) {
            //стрибок суперника
            if (level != 1) {
                if (obstacles.get(i).getX() < adversary.getWidth() / 2 + 30) {

                    if (adversary.getY() < adversary.getHeight() + 30) {
                        adversary.setY(adversary.getHeight() + 10);
                        jump(adversary);
                    }


                }
            }
            //update obstacle
            obstacles.get(i).update();
            if (MacroCollision(player, obstacles.get(i))) {
                obstacles.remove(i);
                gameStatus = STATUS_END;
                break;

            }
            if (obstacles.get(i).getX() < -50)
                obstacles.remove(i);
        }

        if (player.getEnergy() <= 0) {
            gameStatus = STATUS_END;
        }
        if (adversary.getEnergy() < 0) {
            adversary.incX(-1);
        }

        player.update();
        adversary.update();
        bg.update();

        if (getGameStatus() == STATUS_END)
            sp.play(SOUND_DONE_THROW_ID, 0.5f, 0.5f, 1, 0, 1);
        takts++;
    }

    public int getCountSnowbolls() {
        return countSnowbolls;
    }

    private boolean MacroCollision(final GameObject playerObj, GameObject boxObj) {
        boolean XColl = false;
        boolean YColl = false;
        //Зміна метода через зміну системи координат
        if (!boxObj.isBonus()) {
            if ((playerObj.getX() + 130 >= boxObj.getX()) && (playerObj.getX() + 50 <= boxObj.getX() + 45))
                XColl = true;
            if ((playerObj.getY() - playerObj.getHeight() <= boxObj.getY()) && (playerObj.getY() >= boxObj.getY() - 25))
                YColl = true;
        } else {
            if ((playerObj.getX() + 140 >= boxObj.getX()) && (playerObj.getX() + 20 <= boxObj.getX() + 100))
                XColl = true;
            if ((playerObj.getY() - playerObj.getHeight() <= boxObj.getY()) && (playerObj.getY() >= boxObj.getY() - 100))
                YColl = true;
        }
        if ((XColl & YColl)) {
            if (!boxObj.isBonus()) {
                gameStatus = STATUS_END;
                return true;
            } else if (catchJamp) {
                sp.play(SOUND_BONUS_ID, 1, 1, 1, 0, 1);
                int count = r.nextInt(10 - level);
                for (int i = 0; i < count; i++) {
                    snowballs.add(new Snowball());
                }

                countSnowbolls = snowballs.size();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10 + level; i++) {
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
                return false;
            }
        }


        return false;
    }

    private void dialog() {
        showDialog = true;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                showDialogPlayer = false;
                int dialog = r.nextInt(2);
                switch (dialog) {
                    case 0:
                        dialogText = "Cтiй";
                        break;
                    case 1:
                        dialogText = "ТобI пиздець";
                        break;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showDialogPlayer = true;
                switch (dialog) {
                    case 0:
                        dialogText = "Iди в жопу";
                        break;
                    case 1:
                        dialogText = "Iди нах*й";
                        break;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showDialog = false;

            }
        });
        thread.start();

    }


    public void throwSnowball(final Snowball snowball) {
        countSnowbolls--;
        sp.play(SOUND_THROW_ID, 1, 1, 1, 0, 1);
        show_sn = true;
        //100 - тут координа суперника
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                snowball.setX((player.getX() + player.getWidth()));
                float x_adversary = adversary.getWidth() / 2;
                float y = player.getY() - 40;
                float center = (snowball.getX() - x_adversary - 10) / 2;
                float shift = (snowball.getX() + x_adversary - 10) / 2;
                float k = (float) (1 / Math.pow((center), 2));


                while (snowball.getX() > x_adversary) {

                    snowball.setX((float) (snowball.getX() - (2.5 - snowball.getAngle())));
                    snowball.setY((float) (y + snowball.getAngle() * 200 * (1 - Math.pow((snowball.getX() - shift), 2) * k)));

                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                sp.play(SOUND_DONE_THROW_ID, 0.5f, 0.5f, 1, 0, 1);
                adversary.changeEnergy(-(levels.size() - level));
                snowballs.remove(snowball);
                if (snowballs.size() == 0)
                    show_sn = false;

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
