package com.game.kolas.mygame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
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

import com.game.kolas.mygame.level.Level;

import static com.game.kolas.mygame.DBHelper.BEST_TIME_KEY;
import static com.game.kolas.mygame.DBHelper.ID_KEY;
import static com.game.kolas.mygame.DBHelper.LEVEL_TABLE;
import static com.game.kolas.mygame.DBHelper.OPEN_KEY;
import static com.game.kolas.mygame.GameModel.STATUS_END;
import static com.game.kolas.mygame.GameModel.STATUS_GAMING;
import static com.game.kolas.mygame.StartActivity.FALSE;
import static com.game.kolas.mygame.StartActivity.TRUE;

public class GameActivity extends Activity implements View.OnClickListener, View.OnTouchListener {
    private GameSurface gameView;
    String time;
    String bestTime;
    SharedPreferences sPref;
    DialogFragment endDialog;
    MediaPlayer mPlayer;
    DialogFragment pauseDialog;
    public static final String MY_SETTINGS = "my_settings";
    final String CHECK = "sound";
    private GameModel model;
    private DrawGame thread;
    private Chronometer chronometer;
    private ImageButton pauseButton;
    private ImageButton catchButton;
    private ProgressBar progressBar;

    public static final String TAG = "TAG";

    private long timeWhenStopped = 0;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sPref = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        FrameLayout game = new FrameLayout(this);

        mPlayer = MediaPlayer.create(this, R.raw.pogon);
        if (sPref.getBoolean(CHECK, true))
            mPlayer.start();

        bestTime = DataGame.levels.get(LevelActivity.LEVEL).getBestTime();

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.bar, null);

        chronometer = (Chronometer) view.findViewById(R.id.timer);
        pauseButton = (ImageButton) view.findViewById(R.id.pause);
        catchButton = (ImageButton) view.findViewById(R.id.catch_bootle);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        catchButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);

        pauseDialog = new DialogPause();
        endDialog = new DialogEnd();

        model = new GameModel(getResources());
        gameView = new GameSurface(this, model);
        gameView.setOnTouchListener(this);

        progressBar.setProgress(model.getProgress());

        thread = new DrawGame(this);
        thread.setRunning(true);
        thread.start();

        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chronometer.start();

        game.addView(gameView);
        game.addView(view);

        setContentView(game);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                jump();
                break;
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        model.setCatchJamp(false);
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            jump();
        }
        return super.onTouchEvent(motionEvent);

    }

    public void update() {
        model.update();
        progressBar.setProgress(model.getProgress());

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
            model.getPlayer().pause = false;
            model.getVrag().pause = false;
            thread.setPause(false);
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chronometer.start();
        }
    }


    private void pause() {
        if (!thread.isPause()) {
            time = String.valueOf(chronometer.getText());
            thread.setPause(true);
            model.getPlayer().pause = true;
            model.getVrag().pause = true;
            chronometer.stop();
            timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
        }
    }

    private void end() {
        chronometer.stop();
        thread.setRunning(false);
        thread.setRunning(false);
        time = String.valueOf(chronometer.getText());
        if (StringToInt(time) > StringToInt(bestTime)) {
            bestTime = time;
            DataGame.levels.get(LevelActivity.LEVEL).setBestTime(bestTime);
            updateDB(DataGame.levels.get(LevelActivity.LEVEL));
        }
        endDialog.show(getFragmentManager(), "END_DIALOG");
    }


    private void jump() {
        try {

            if (model.getPlayer().getY() > 520) {
                model.getPlayer().setY(542);
                model.getPlayer().jamp();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private int StringToInt(String s) {
        int res = 0;
        String[] list = s.split(":");
        for (int i = list.length - 1; i >= 0; i--) {
            res = (res + ((int) Math.pow(60, i) * Integer.parseInt(list[i])));

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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class DialogPause extends DialogFragment implements View.OnClickListener {

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.liner, null);
            v.findViewById(R.id.play).setOnClickListener(this);
            v.findViewById(R.id.newgame).setOnClickListener(this);
            v.findViewById(R.id.mainmenu).setOnClickListener(this);
            setThistime((TextView) v.findViewById(R.id.textView));

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
                    finish();
                    Intent intent = new Intent(getActivity(), GameActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mainmenu:
                    intent = new Intent(getActivity(), MenuActivity.class);
                    startActivity(intent);
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

            v = inflater.inflate(R.layout.endgame, null);

            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            v.findViewById(R.id.newgameE).setOnClickListener(this);
            v.findViewById(R.id.mainmenuE).setOnClickListener(this);
            setBestime((TextView) v.findViewById(R.id.besttime));
            setThistime((TextView) v.findViewById(R.id.timetext));
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
                    finish();
                    Intent intent = new Intent(getActivity(), GameActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mainmenuE:
                    intent = new Intent(getActivity(), MenuActivity.class);
                    startActivity(intent);
                    finish();
                    break;

            }
        }

    }

    public GameActivity() {
        super();
    }

    public void onDestroy() {

        mPlayer.stop();
        super.onDestroy();
    }
}
