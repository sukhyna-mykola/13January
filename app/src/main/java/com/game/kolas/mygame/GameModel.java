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
import java.util.Random;

import static android.graphics.BitmapFactory.decodeResource;
import static com.game.kolas.mygame.DrawGame.FPS;
import static com.game.kolas.mygame.data.DataGame.dialogs;
import static com.game.kolas.mygame.data.DataGame.levels;
import static com.game.kolas.mygame.views.GameSurface.HEIGHT;
import static com.game.kolas.mygame.views.GameSurface.WIDTH;
import static java.lang.Thread.sleep;

/**
 * Created by mykola on 17.01.17.
 */

public class GameModel {


    private ArrayList<Obstacle> obstacles;
    private ArrayList<Obstacle> bonuses;
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

    long lastBonusTime;

    private Resources resources;
    private float obstaclesSpeed;
    private float nextBonusTime;

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
        return (int) player.getEnergy();
    }

    public ArrayList<Snowball> getSnowballs() {
        return snowballs;
    }

    public Message getNewLevel() {
        return newLevel;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameModel(Context context, int level) {
        this.context = context;
        this.resources = context.getResources();
        this.level = level;

        newLevel = new Message(decodeResource(resources, R.drawable.img_button), WIDTH / 2 - 200, HEIGHT / 2 + 100, 400, 100, 0, 0);

        initGameData();

    }

    public int getGameStatus() {
        return gameStatus;
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
                adversary = new Player(decodeResource(resources, R.drawable.adversary_level1), 200, 200, 4, 100, 3);
                player = new Player(decodeResource(resources, R.drawable.player_level1), 175, 200, 4, 144, 2);
                SOUND_RUN_ID = sp.load(context, R.raw.run, SOUND_RUN_STREAM);
                break;
            case 1:
                adversary = new Player(decodeResource(resources, R.drawable.adversary_level2), 266, 200, 3, 0, 0);
                player = new Player(decodeResource(resources, R.drawable.player_level2), 175, 200, 4, 144, 2);
                SOUND_RUN_ID = sp.load(context, R.raw.bike, SOUND_RUN_STREAM);
                break;
            case 2:
                adversary = new Player(decodeResource(resources, R.drawable.adversary_level3), 200, 200, 8, 100, 2);
                player = new Player(decodeResource(resources, R.drawable.player_level3), 175, 200, 4, 144, 2);
                SOUND_RUN_ID = sp.load(context, R.raw.run, SOUND_RUN_STREAM);
                break;
            case 3:
                adversary = new Player(decodeResource(resources, R.drawable.adversary_level4), 375, 300, 8, 150, 6);
                player = new Player(decodeResource(resources, R.drawable.player_level1), 175, 200, 4, 144, 2);
                SOUND_RUN_ID = sp.load(context, R.raw.horse, SOUND_RUN_STREAM);
                break;

        }

        player.setX(adversary.getWidth());

        player.setMessage(new Message(decodeResource(resources, R.drawable.template_dialog_text), (int) (player.getX() + player.getWidth() / 3 * 2), (int) (player.getY()), 200, 50, 15, 0));
        adversary.setMessage(new Message(decodeResource(resources, R.drawable.template_dialog_text), (int) (adversary.getX() + adversary.getWidth() / 3 * 2), (int) (adversary.getY()), 200, 50, 15, 0));

        ArrayList<Bitmap> listImages = new ArrayList<>();
        listImages.add(decodeResource(resources, R.drawable.background_first));
        listImages.add(decodeResource(resources, R.drawable.background_two));
        listImages.add(decodeResource(resources, R.drawable.background_three));
        bg = new Background(listImages);

        obstacles = new ArrayList<>();
        bonuses = new ArrayList<>();
        snowballs = new ArrayList<>();

        gameStatus = STATUS_GAMING;

        player.setHealth(2);

        obstaclesSpeed = (float) (12 - this.level / 2.0);

        catchJamp = false;
        takts = 0;
        lastBonusTime = 0;
        nextBonusTime = 0;
        countSnowbolls = 0;

    }

    public ArrayList<Obstacle> getBonuses() {
        return bonuses;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public SoundPool getSp() {
        return sp;
    }

    public void jump(Player obj) {
        sp.play(SOUND_JUMP_ID, 1, 1, 1, 0, 1);
        if (!obj.isJumpFlag() || obj.getY() - obj.getHeight() < 30) {
            obj.resetDY();
            obj.getAnimation().setPause(true);
            obj.setJumpFlag(true);
        }


    }

    //Метод оновлення  параметрів гри
    public void update() {


        if (player.getX() < player.getEnergy() * 2 + adversary.getWidth())
            player.addToX(1);
        else
            player.addToX(-1);

        //зменшення енергії кожну 1c.
        if (takts % FPS == 0) {//~1s
            player.addToEnergy(-1.5f);
        }

        //додати 1 health кожну хвилину
        if (takts % (FPS * 60) == 0) {//~60s
            player.addToHealth(1);
        }

        //збільшення швидкості перешкод кожні 20с.
        if (takts % (FPS * 20) == 0) {//~20s.
            if (adversary.isVisibility())
                dialog();

            if (obstaclesSpeed >= 6)
                obstaclesSpeed -= 0.4;

        }

        //Додавання нової перешкоди
        if (obstacles.size() >= 1) {
            if (obstacles.get(obstacles.size() - 1).getX() < obstacleStartTime) {
                addNewObstacle();
            }
        } else addNewObstacle();

        //бонус
        long checkTime = takts - lastBonusTime;
        if (checkTime > nextBonusTime) {
            bonuses.add(new Obstacle(decodeResource(resources, R.drawable.bonus_beer), 5, true));
            nextBonusTime = player.getEnergy() * 2 + r.nextInt(50);
            lastBonusTime = takts;
        }


        for (int i = 0; i < obstacles.size(); i++) {
            //стрибок суперника
            if (level != 1) {
                if (obstacles.get(i).getX() < adversary.getWidth() / 2 + 40 && obstacles.get(i).isVisibility() && !adversary.isJumpFlag())
                    jump(adversary);
            }
            //update obstacle_snow
            obstacles.get(i).update();

            if (obstacles.get(i).isVisibility())
                if (MacroCollision(player, obstacles.get(i))) {
                    obstacles.get(i).setVisibility(false);
                    player.addToEnergy(-(level + 5));
                    if (player.getHealth() > 0) {
                        player.setHealth(player.getHealth() - 1);
                        sp.play(SOUND_DONE_THROW_ID, 0.5f, 0.5f, 1, 0, 1);
                    } else {
                        gameStatus = STATUS_END;
                        break;
                    }

                }
            if (obstacles.get(i).getX() < -obstacles.get(i).getWidth())
                obstacles.remove(i);

        }

        for (int i = 0; i < bonuses.size(); i++) {
            //update bonuses
            bonuses.get(i).update();

            if (bonuses.get(i).isVisibility())
                if (MacroCollision(player, bonuses.get(i))) {
                    if (catchJamp) {
                        bonuses.get(i).setVisibility(false);
                        sp.play(SOUND_BONUS_ID, 1, 1, 1, 0, 1);

                        player.addToEnergy(5 + level);
                        int count = r.nextInt(levels.size()) + 1;

                        for (int j = 0; j < count; j++) {
                            snowballs.add(new Snowball());
                        }
                        countSnowbolls = snowballs.size();
                    }
                }
            if (bonuses.get(i).getX() < -bonuses.get(i).getWidth())
                bonuses.remove(i);

        }
        //якщо закінчилая енергія в гравця
        if (player.getEnergy() <= 0) {
            gameStatus = STATUS_END;

        }
        //якщо закінчилася енергія в суперника
        if (adversary.getEnergy() < 0) {
            adversary.addToX(-1);

            if (level + 1 != levels.size()) {
                if (!levels.get(level + 1).isOpen()) {
                    newLevel.setVisibility(true);
                    newLevel.setText(context.getString(R.string.open_level, (level + 2)));
                }
            } else {
                newLevel.setVisibility(true);
                newLevel.setText(context.getString(R.string.levels_complete));
            }
            //якщо суперник зник з екрану
            if (adversary.getX() + adversary.getWidth() < 0) {
                adversary.setVisibility(false);
                newLevel.setVisibility(false);
            }
        }

        if (getGameStatus() == STATUS_END) {
            sp.play(SOUND_DONE_THROW_ID, 0.5f, 0.5f, 1, 0, 1);
            sp.release();

        }

        player.update();
        adversary.update();
        bg.update();

        takts++;
    }


    private void addNewObstacle() {
        if (r.nextBoolean())
            obstacles.add(new Obstacle(decodeResource(resources, R.drawable.obstacle_snow), obstaclesSpeed, false));
        else
            obstacles.add(new Obstacle(decodeResource(resources, R.drawable.obstacle_wood), obstaclesSpeed, false));
        obstacleStartTime = (float) r.nextInt(WIDTH / 2) + (player.getWidth() + 50);
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
                try {
                    String value = (new ArrayList<String>(dialogs.keySet())).get(r.nextInt(dialogs.size()));
                    adversary.getMessage().setText(value);
                    adversary.getMessage().setVisibility(true);

                    sleep(2000);

                    adversary.getMessage().setVisibility(false);
                    player.getMessage().setText(dialogs.get(value));
                    player.getMessage().setVisibility(true);

                    sleep(2000);

                    player.getMessage().setVisibility(false);

                } catch (Exception e) {
                }
            }
        });
        thread.setDaemon(true);

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
                try {
                    snowball.setX((player.getX() + player.getWidth()));
                    float x_adversary = adversary.getWidth() / 2;
                    float y = player.getY() - 40;
                    float center = (snowball.getX() - x_adversary - 10) / 2;
                    float shift = (snowball.getX() + x_adversary - 10) / 2;
                    float k = (float) (1 / Math.pow((center), 2));


                    while (snowball.getX() > x_adversary) {

                        snowball.setX((float) (snowball.getX() - (2.5 - snowball.getAngle())));
                        snowball.setY((float) (y + snowball.getAngle() * 200 * (1 - Math.pow((snowball.getX() - shift), 2) * k)));

                        sleep(5);

                    }
                    sp.play(SOUND_DONE_THROW_ID, 0.5f, 0.5f, 1, 0, 1);
                    adversary.addToEnergy(-(levels.size() - level));
                    snowballs.remove(snowball);

                    String value = (new ArrayList<String>(dialogs.keySet())).get(r.nextInt(dialogs.size()));
                    adversary.getMessage().setText(value);
                    adversary.getMessage().setVisibility(true);

                    sleep(2000);

                    adversary.getMessage().setVisibility(false);

                } catch (Exception e) {
                }
            }
        });
        thread.setDaemon(true);

        thread.start();


    }
}
