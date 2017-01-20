package com.game.kolas.mygame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.game.kolas.mygame.objects.Level;
import com.game.kolas.mygame.objects.Snowball;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static com.game.kolas.mygame.DBHelper.BEST_TIME_KEY;
import static com.game.kolas.mygame.DBHelper.ID_KEY;
import static com.game.kolas.mygame.DBHelper.LEVEL_TABLE;
import static com.game.kolas.mygame.DBHelper.OPEN_KEY;
import static com.game.kolas.mygame.DialogSetting.sound;
import static com.game.kolas.mygame.GameModel.STATUS_END;
import static com.game.kolas.mygame.GameModel.STATUS_GAMING;
import static com.game.kolas.mygame.StartActivity.FALSE;
import static com.game.kolas.mygame.StartActivity.TRUE;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    private GameSurface gameView;
    private String time;
    private String bestTime;

    DialogFragment endDialog;
    MediaPlayer mPlayer;
    DialogFragment pauseDialog;
    public static final String LEVEL = "LEVEL";

    private GameModel model;
    private DrawGame thread;
    private Chronometer chronometer;
    private ImageButton throwButton;
    private TextView countSnowballs;
    private ProgressBar progressBar;

    public static final String TAG = "TAG";

    private long timeWhenStopped = 0;
    private int level;

    private SoundPool sp;


    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlayer = MediaPlayer.create(this, R.raw.pogon);
        mPlayer.setLooping(true);
        if (sound)
            mPlayer.start();

        level = getIntent().getIntExtra(LEVEL, 0);
        bestTime = DataGame.levels.get(level).getBestTime();

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

        model = new GameModel(this, level, sp);
        gameView = new GameSurface(this, model);
        gameView.setOnTouchListener(this);

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.game_menu, null);

        chronometer = (Chronometer) view.findViewById(R.id.timer);
        throwButton = (ImageButton) view.findViewById(R.id.throww_snowball);
        countSnowballs = (TextView) view.findViewById(R.id.snowballs_count);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        throwButton.setOnLongClickListener(this);
        progressBar.setProgress(model.getProgress());
        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chronometer.start();

        FrameLayout game = new FrameLayout(this);
        game.addView(gameView);
        game.addView(view);
        setContentView(game);


        pauseDialog = new DialogPause();
        endDialog = new DialogEnd();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        thread = new DrawGame(this);
        thread.setRunning(true);
        thread.start();

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause:
                pause();
                pauseDialog.show(getFragmentManager(), "PAUSE_DIALOG");
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
        progressBar.setProgress(model.getProgress());
        if (model.getCountSnowbolls() == 0 || model.getAdversary().getEnergy() < 0) {
            if (model.getAdversary().getEnergy() < 0) {
                try {
                    if (!DataGame.levels.get(level + 1).isOpen()) {
                        DataGame.levels.get(level + 1).setOpen(true);
                        updateDB(DataGame.levels.get(level + 1));
                        DialogOpenNewLevel.newInstance(level + 1).show(getSupportFragmentManager(), "NEW_LEVEL_DIALOG");
                    }
                } catch (Exception e) {

                }
            }
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
        switch (model.getGameStatus()) {
            case STATUS_GAMING: {
                gameView.update();
                break;
            }
            case STATUS_END: {
                end();
                break;
            }
        }
    }

    private void start() {
        if (thread.isPause()) {
            sp.autoResume();
            model.getPlayer().setPause(false);
            model.getAdversary().setPause(false);
            thread.setPause(false);
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chronometer.start();
        }
    }


    private void pause() {
        if (!thread.isPause()) {
            sp.autoPause();
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
        time = String.valueOf(chronometer.getText());
        if (StringToInt(time) > StringToInt(bestTime)) {
            bestTime = time;
            DataGame.levels.get(level).setBestTime(bestTime);
            updateDB(DataGame.levels.get(level));
        }
        endDialog.show(getFragmentManager(), "END_DIALOG");
    }

    private void newGame() {
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
    }

    @Override
    public boolean onLongClick(View view) {
        for (Snowball snowball : model.getSnowballs()) {
            if (!snowball.isThrowed()) {
                snowball.setThrowed(true);
                model.throwSnowball(snowball);

            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class DialogPause extends DialogFragment implements View.OnClickListener {

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.pause_layout, null);
            v.findViewById(R.id.play).setOnClickListener(this);
            v.findViewById(R.id.newgame).setOnClickListener(this);
            v.findViewById(R.id.mainmenu).setOnClickListener(this);
            setThistime((CustomFontTextView) v.findViewById(R.id.textView));

            pauseDialog.setStyle(STYLE_NO_TITLE, 0);
            pauseDialog.setCancelable(false);

            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().setCanceledOnTouchOutside(false);//заборона закриття діалогу коли натиснуто поза ним
            return v;
        }

        void setThistime(TextView textView) {
            textView.setText("Ваш час - " + time);
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.play:
                    start();
                    dismiss();
                    break;
                case R.id.newgame:
                    newGame();
                    dismiss();
                    break;
                case R.id.mainmenu:
                    finish();
                    break;

            }
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class DialogEnd extends DialogFragment implements View.OnClickListener {
        View v;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            v = inflater.inflate(R.layout.end_game_layout, null);

            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            v.findViewById(R.id.newgameE).setOnClickListener(this);
            v.findViewById(R.id.mainmenuE).setOnClickListener(this);
            setBestime((CustomFontTextView) v.findViewById(R.id.besttime));
            setThistime((CustomFontTextView) v.findViewById(R.id.timetext));
            getDialog().setCanceledOnTouchOutside(false);//заборона закриття діалогу коли натиснуто поза ним
            return v;
        }


        void setBestime(TextView textView) {
            textView.setText("Кращий час - " + bestTime);
        }

        void setThistime(TextView textView) {

            textView.setText("Ваш час - " + time);
        }

        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.newgameE:
                    newGame();
                    dismiss();
                    break;
                case R.id.mainmenuE:
                    finish();
                    break;

            }
        }

    }

    public GameActivity() {
        super();
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
        thread.setRunning(false);
        mPlayer.stop();
        sp.release();
        super.onDestroy();
    }
}
