package com.game.kolas.mygame;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;

import android.os.Bundle;
import android.os.SystemClock;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;


import com.game.kolas.mygame.data.DBHelper;
import com.game.kolas.mygame.data.DataGame;
import com.game.kolas.mygame.dialogs.DialogEnd;
import com.game.kolas.mygame.dialogs.DialogMethods;
import com.game.kolas.mygame.dialogs.DialogPause;
import com.game.kolas.mygame.objects.Level;
import com.game.kolas.mygame.objects.Snowball;
import com.game.kolas.mygame.views.CustomFontTextView;
import com.game.kolas.mygame.views.GameSurface;

import java.util.Arrays;
import java.util.Collections;

import static com.game.kolas.mygame.data.DBHelper.BEST_TIME_KEY;
import static com.game.kolas.mygame.data.DBHelper.ID_KEY;
import static com.game.kolas.mygame.data.DBHelper.LEVEL_TABLE;
import static com.game.kolas.mygame.data.DBHelper.OPEN_KEY;
import static com.game.kolas.mygame.dialogs.DialogSetting.sound;
import static com.game.kolas.mygame.GameModel.STATUS_END;
import static com.game.kolas.mygame.GameModel.STATUS_GAMING;
import static com.game.kolas.mygame.StartActivity.FALSE;
import static com.game.kolas.mygame.StartActivity.TRUE;

public class GameActivity extends AppCompatActivity implements DialogMethods, View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    private GameSurface gameView;
    private String time;
    private String bestTime;


    private MediaPlayer mPlayer;

    public static final String LEVEL = "LEVEL";
    public static final String PAUSE_DIALOG_TAG = "PAUSE_DIALOG";
    public static final String END_DIALOG_TAG = "END_DIALOG";

    private GameModel model;
    private DrawGame thread;
    private Chronometer chronometer;
    private ImageButton throwButton;
    private CustomFontTextView countSnowballs;
    private CustomFontTextView countHearths;


    public static final String TAG = "TAG";

    private long timeWhenStopped = 0;
    private int level;


    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        level = getIntent().getIntExtra(LEVEL, 0);
        bestTime = DataGame.levels.get(level).getBestTime();


        model = new GameModel(this, level);
        gameView = new GameSurface(this, model);
        gameView.setOnTouchListener(this);

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.game_menu, null);

        chronometer = (Chronometer) view.findViewById(R.id.timer);
        throwButton = (ImageButton) view.findViewById(R.id.throww_snowball);
        countSnowballs = (CustomFontTextView) view.findViewById(R.id.snowballs_count);

        countHearths = (CustomFontTextView) view.findViewById(R.id.health_text);

        throwButton.setOnLongClickListener(this);

        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chronometer.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/myFont.ttf"));


        FrameLayout game = new FrameLayout(this);
        game.addView(gameView);
        game.addView(view);
        setContentView(game);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPlayer = MediaPlayer.create(this, R.raw.pogon);
        mPlayer.setLooping(true);


        thread = new DrawGame(this);
        thread.setRunning(true);

        if (sound)
            mPlayer.start();
        chronometer.start();
        thread.start();

        overridePendingTransition(R.anim.activity_down_up_enter, R.anim.activity_down_up_exit);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause:
                pause();
                if (getFragmentManager().findFragmentByTag(PAUSE_DIALOG_TAG) == null)
                    DialogPause.newInstance(time).show(getSupportFragmentManager(), PAUSE_DIALOG_TAG);
                break;
            case R.id.catch_bootle:
                model.setCatchJamp(true);
                model.jump(model.getPlayer());

                break;
            case R.id.throww_snowball:
                for (Snowball snowball : model.getSnowballs()) {
                    if (!snowball.isThrowed()) {
                        snowball.setThrowed(true);
                        model.throwSnowball(snowball);
                        break;
                    }
                }
                break;
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        model.setCatchJamp(false);
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            model.jump(model.getPlayer());

        }
        return super.onTouchEvent(motionEvent);

    }

    public void update() {

        model.update();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countHearths.setText(String.valueOf(model.getPlayer().getHealth()));
            }
        });

        if (model.getCountSnowbolls() == 0 || !model.getAdversary().isVisibility()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    throwButton.setVisibility(View.GONE);
                    countSnowballs.setText("");
                }
            });
        } else
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    throwButton.setVisibility(View.VISIBLE);
                    countSnowballs.setText(String.valueOf(model.getCountSnowbolls()));
                }
            });

        if (model.getAdversary().getEnergy() < 0) {
            try {
                if (!DataGame.levels.get(level + 1).isOpen()) {
                    DataGame.levels.get(level + 1).setOpen(true);
                    updateDB(DataGame.levels.get(level + 1));
                }
            } catch (Exception e) {
            }
        }


        switch (model.getGameStatus()) {
            case STATUS_GAMING: {
                gameView.update();
                break;
            }
            case STATUS_END: {
                end();
                if (getFragmentManager().findFragmentByTag(END_DIALOG_TAG) == null)
                    getSupportFragmentManager().beginTransaction().add(DialogEnd.newInstance(time, bestTime), END_DIALOG_TAG).commit();
                break;
            }
        }
    }

    public void start() {
        if (thread.isPause()) {
            model.getSp().autoResume();

            if (!mPlayer.isPlaying() && sound)
                mPlayer.start();

            model.getPlayer().setPause(false);
            model.getAdversary().setPause(false);
            thread.setPause(false);
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chronometer.start();
        }
    }

    @Override
    public void exitGame() {
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }


    private void pause() {
        if (!thread.isPause()) {
            model.getSp().autoPause();

            if (mPlayer.isPlaying() && sound)
                mPlayer.pause();

            time = String.valueOf(chronometer.getText());
            thread.setPause(true);
            model.getPlayer().setPause(true);
            model.getAdversary().setPause(true);
            chronometer.stop();
            timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
        }
    }

    private void end() {

        chronometer.stop();
        thread.setPause(true);
        model.getPlayer().setPause(true);
        model.getAdversary().setPause(true);
        model.getSp().autoPause();
        mPlayer.release();
        time = String.valueOf(chronometer.getText());
        if (StringToInt(time) > StringToInt(bestTime)) {
            bestTime = time;
            DataGame.levels.get(level).setBestTime(bestTime);
            updateDB(DataGame.levels.get(level));
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void newGame() {
        mPlayer = MediaPlayer.create(this, R.raw.pogon);
        mPlayer.setLooping(true);
        if (sound)
            mPlayer.start();

        timeWhenStopped = 0;
        model.initGameData();
        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chronometer.start();
        thread.setPause(false);
    }


    private int StringToInt(String s) {
        int res = 0;
        String[] list = s.split(":");
        Collections.reverse(Arrays.asList(list));
        for (int i = 0; i < list.length; i++) {
            res += ((int) Math.pow(60, i) * Integer.parseInt(list[i]));
        }
        return res;
    }

    private void updateDB(Level l) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID_KEY, l.getId());
        cv.put(BEST_TIME_KEY, l.getBestTime());
        cv.put(OPEN_KEY, l.isOpen() ? TRUE : FALSE);
        db.update(LEVEL_TABLE, cv, ID_KEY + " = ?", new String[]{String.valueOf(l.getId())});
        db.close();
        dbHelper.close();
    }

    @Override
    public boolean onLongClick(View view) {
        for (Snowball snowball : model.getSnowballs()) {
            if (!snowball.isThrowed()) {
                snowball.setThrowed(true);
                model.throwSnowball(snowball);

            }
        }
        throwButton.setVisibility(View.GONE);
        countSnowballs.setText("");
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    public void onDestroy() {
        super.onDestroy();
        thread.setPause(false);
        thread.setRunning(false);
        mPlayer.release();

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MenuActivity.class));
    }
}
