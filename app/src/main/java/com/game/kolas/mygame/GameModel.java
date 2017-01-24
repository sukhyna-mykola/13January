package com.game.kolas.mygame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.game.kolas.mygame.objects.Background;
import com.game.kolas.mygame.objects.GameObject;
import com.game.kolas.mygame.objects.Message;
import com.game.kolas.mygame.objects.Obstacle;
import com.game.kolas.mygame.objects.Player;
import com.game.kolas.mygame.objects.Snowball;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static android.graphics.BitmapFactory.decodeResource;
import static com.game.kolas.mygame.data.DataGame.dialogs;
import static com.game.kolas.mygame.data.DataGame.levels;
import static com.game.kolas.mygame.views.GameSurface.HEIGHT;
import static com.game.kolas.mygame.views.GameSurface.WIDTH;

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

    private int level;

    private float obstacleStartTime;
    boolean catchJamp;
    private long takts;
    long incObstaclesSpeepTime;
    long addNewBonusTime;
    long decEnergyTime;
    private Resources resources;
    private float obstaclesSpeed;
    private float whenNewBonusTime;
    private Message moon;
    private Message newLevel;
    private SoundPool sp;
    private Context context;

    public boolean isCatchJamp() {
        return catchJamp;
    }

    public void setCatchJamp(boolean catchJamp) {
        this.catchJamp = catchJamp;
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

    public Message getNewLevel() {
        return newLevel;
    }

    public Message getMoon() {
        return moon;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameModel(Context context, int level) {
        this.context = context;
        this.resources = context.getResources();
        this.level = level;


        newLevel = new Message(decodeResource(resources, R.drawable.template_new_level_text), WIDTH / 2 - 200, HEIGHT / 2, 400, 200, 0, 0);
        newLevel.setText("Вiдкрито рiвень :" + (level + 2));

        moon = new Message(decodeResource(resources, R.drawable.moon), 0, HEIGHT - 200, 175, 200, 0, 0);
        moon.setVisibility(true);

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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initGameData() {
        if ((android.os.Build.VERSION.SDK_INT) >= 21) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            sp = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .setMaxStreams(5)
                    .build();
        } else {
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        SOUND_JUMP_ID = sp.load(context, R.raw.jump, SOUND_JUMP_STREAM);
        SOUND_BONUS_ID = sp.load(context, R.raw.bonus, SOUND_BONUS_STREAM);
        SOUND_THROW_ID = sp.load(context, R.raw.throw_snowball, SOUND_THROW_STREAM);
        SOUND_DONE_THROW_ID = sp.load(context, R.raw.done_throw, SOUND_DONE_THROW_STREAM);

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                if (i == SOUND_RUN_ID)
                    soundPool.play(SOUND_RUN_ID, 0.8f, 0.8f, 5, -1, 1);
            }
        });

        switch (level) {
            case 0:
                adversary = new Player(decodeResource(resources, R.drawable.adversary_level1), 200, 200, 4, 100);
                player = new Player(decodeResource(resources, R.drawable.player_level1), 175, 200, 4, 144);
                SOUND_RUN_ID = sp.load(context, R.raw.run, SOUND_RUN_STREAM);
                break;
            case 1:
                adversary = new Player(decodeResource(resources, R.drawable.adversary_level2), 266, 200, 3, 0);
                player = new Player(decodeResource(resources, R.drawable.player_level2), 175, 200, 4, 144);

                SOUND_RUN_ID = sp.load(context, R.raw.bike, SOUND_RUN_STREAM);
                break;
            case 2:
                adversary = new Player(decodeResource(resources, R.drawable.adversary_level3), 200, 200, 8, 100);
                player = new Player(decodeResource(resources, R.drawable.player_level3), 175, 200, 4, 144);
                SOUND_RUN_ID = sp.load(context, R.raw.run, SOUND_RUN_STREAM);
                break;
            case 3:
                adversary = new Player(decodeResource(resources, R.drawable.adversary_level4), 375, 300, 8, 100);
                player = new Player(decodeResource(resources, R.drawable.player_level1), 175, 200, 4, 144);
                SOUND_RUN_ID = sp.load(context, R.raw.horse, SOUND_RUN_STREAM);
                break;

        }

        adversary.setX(0);
        player.setX(adversary.getWidth());

        player.setMessage(new Message(decodeResource(resources, R.drawable.template_dialog_text), (int) (player.getX() + player.getWidth() / 3 * 2), (int) (player.getY()), 200, 50, 15, 0));
        adversary.setMessage(new Message(decodeResource(resources, R.drawable.template_dialog_text), (int) (adversary.getX() + adversary.getWidth() / 3 * 2), (int) (adversary.getY()), 200, 50, 15, 0));

        ArrayList<Bitmap> listImages = new ArrayList<>();
        listImages.add(decodeResource(resources, R.drawable.first));
        listImages.add(decodeResource(resources, R.drawable.two));
        listImages.add(decodeResource(resources, R.drawable.three));
        bg = new Background(listImages);

        obstacles = new ArrayList<>();
        snowballs = new ArrayList<>();
        gameStatus = STATUS_GAMING;

        obstaclesSpeed = 14 - level;
        player.setHealth(levels.size() - level);

        obstacleStartTime = (float) r.nextInt(700);
        catchJamp = false;
        takts = 0;
        incObstaclesSpeepTime = 0;
        addNewBonusTime = 0;
        decEnergyTime = 0;
        whenNewBonusTime = 0;
        countSnowbolls = 0;
        newLevel.setVisibility(false);
        moon.setX(0);


    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public SoundPool getSp() {
        return sp;
    }

    public void update() {
        moon.setX((float) (moon.getX() + 0.1));

        if (player.getX() < player.getEnergy() * 2 + adversary.getWidth())
            player.incX(1);
        else
            player.incX(-1);

        //зменшення еергії кожну секунду
        long checkTime = takts - decEnergyTime;
        if (checkTime > 17) {//~1s
            player.changeEnergy(-1);
            decEnergyTime = takts;
        }

        //збільшення швидкості перешкод кожні 20с.
        checkTime = takts - incObstaclesSpeepTime;
        if (checkTime > 400) {//~20s.
            if (r.nextBoolean() && adversary.isVisibility())
                dialog();


            if (obstaclesSpeed >= 10)
                obstaclesSpeed -= 0.5;
            incObstaclesSpeepTime = takts;
        }

        //Додавання нової перешкоди
        if (obstacles.size() == 0) {
            obstacles.add(new Obstacle(decodeResource(resources, R.drawable.obstacle), obstaclesSpeed, false));
        } else {
            int k = obstacles.size() - 1;
            while (obstacles.get(k).isBonus() != false)
                k--;
            if (k >= 0 && obstacles.get(k).getX() < obstacleStartTime) {
                if (r.nextBoolean())
                    obstacles.add(new Obstacle(decodeResource(resources, R.drawable.obstacle), obstaclesSpeed, false));
                else
                    obstacles.add(new Obstacle(decodeResource(resources, R.drawable.wood), obstaclesSpeed, false));

                obstacleStartTime = (float) r.nextInt(WIDTH - player.getWidth() - 50);
            }
        }

        //бонус
        checkTime = takts - addNewBonusTime;
        if (checkTime > whenNewBonusTime) {
            obstacles.add(new Obstacle(decodeResource(resources, R.drawable.beer), 5, true));
            whenNewBonusTime = player.getEnergy() * 2 + r.nextInt(50);
            addNewBonusTime = takts;
        }


        for (int i = 0; i < obstacles.size(); i++) {
            //стрибок суперника
            if (level != 1) {
                if (obstacles.get(i).getX() < adversary.getWidth() / 2 + 40 && obstacles.get(i).isVisibility()) {
                    if (adversary.getY() <= adversary.getHeight() + 30) {
                        adversary.setY(adversary.getHeight() + 10);
                        jump(adversary);
                    }


                }
            }
            //update obstacle
            obstacles.get(i).update();
            if (obstacles.get(i).isVisibility())
                if (MacroCollision(player, obstacles.get(i))) {

                    if (obstacles.get(i).isBonus()) {
                        if (catchJamp) {
                            obstacles.get(i).setVisibility(false);
                            sp.play(SOUND_BONUS_ID, 1, 1, 1, 0, 1);

                            player.changeEnergy(5 + level);
                            int count = r.nextInt(5 + level);

                            for (int j = 0; j < count; j++) {
                                snowballs.add(new Snowball());
                            }
                            countSnowbolls = snowballs.size();

                        }
                    } else {
                        obstacles.get(i).setVisibility(false);
                        player.changeEnergy(-(player.getHealth() + 5));
                        if (player.getHealth() > 0) {
                            player.setHealth(player.getHealth() - 1);
                            sp.play(SOUND_DONE_THROW_ID, 0.5f, 0.5f, 1, 0, 1);
                        } else {
                            gameStatus = STATUS_END;
                            break;
                        }
                    }
                }
            if (obstacles.get(i).getX() < -obstacles.get(i).getWidth())
                obstacles.remove(i);

        }

        //якщо закінчилая енергія в гравця
        if (player.getEnergy() <= 0) {
            gameStatus = STATUS_END;

        }
        //якщо закінчилася енергія в суперника
        if (adversary.getEnergy() < 0) {
            adversary.incX(-1);
            if (!levels.get(level + 1).isOpen()) {
                newLevel.setVisibility(true);
            }
            //якщо суперник зник з екрану
            if (adversary.getX() + adversary.getWidth() < 0) {
                adversary.setVisibility(false);
                newLevel.setVisibility(false);
            }
        }

        player.update();
        adversary.update();
        bg.update();

        if (getGameStatus() == STATUS_END) {
            sp.play(SOUND_DONE_THROW_ID, 0.5f, 0.5f, 1, 0, 1);
            sp.release();

        }
        takts++;
    }

    public int getCountSnowbolls() {
        return countSnowbolls;
    }

    private boolean MacroCollision(final GameObject playerObj, Obstacle boxObj) {
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
        if ((XColl & YColl))
            return true;
        else
            return false;
    }

    private void dialog() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String value = (new ArrayList<String>(dialogs.keySet())).get(r.nextInt(dialogs.size()));
                adversary.getMessage().setText(value);
                adversary.getMessage().setVisibility(true);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                adversary.getMessage().setVisibility(false);
                player.getMessage().setText(dialogs.get(value));
                player.getMessage().setVisibility(true);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                player.getMessage().setVisibility(false);

            }
        });
        thread.start();

    }


    public void throwSnowball(final Snowball snowball) {
        countSnowbolls--;
        sp.play(SOUND_THROW_ID, 1, 1, 1, 0, 1);
        snowball.setVisibility(true);
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
                adversary.changeEnergy(-(4 - level));
                snowballs.remove(snowball);

                String value = (new ArrayList<String>(dialogs.keySet())).get(r.nextInt(dialogs.size()));
                adversary.getMessage().setText(value);
                adversary.getMessage().setVisibility(true);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                adversary.getMessage().setVisibility(false);
            }
        });
        thread.start();

    }
}
