package com.example.kolas.mygame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    GamePanel gameView;
    String time;
    String BestTime;
    SharedPreferences sPref;
    DialogFragment de;
    MediaPlayer mPlayer;
    DialogFragment gff;
    boolean b = false;
    private static final String MY_SETTINGS = "my_settings";
    final String SAVED_TEXT = "saved_text";
    final String CHECK = "sound";

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

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.bar, null);
        view.findViewById(R.id.pause).setOnClickListener(this);
        gff = new DialogF();


        gameView = new GamePanel(this, (Chronometer) view.findViewById(R.id.chron), (ProgressBar) view.findViewById(R.id.progressBar), this, (ImageButton) view.findViewById(R.id.catchdrinc));

//load


        BestTime = sPref.getString(SAVED_TEXT, "");
        game.addView(gameView);
        game.addView(view);

        setContentView(game);

        de = new DialogEnd();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onClick(View v) {
        gameView.pause();


        gff.show(getFragmentManager(), "bhs");

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class DialogF extends DialogFragment implements View.OnClickListener {

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.liner, null);
            v.findViewById(R.id.play).setOnClickListener(this);
            v.findViewById(R.id.newgame).setOnClickListener(this);
            v.findViewById(R.id.mainmenu).setOnClickListener(this);
            setThistime((TextView) v.findViewById(R.id.textView));
            gff.setStyle(STYLE_NO_TITLE, 0);
            gff.setCancelable(false);
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
                    gameView.start();
                    dismiss();
                    break;
                case R.id.newgame:
                    finish();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mainmenu:
                    intent = new Intent(getActivity(), MainMenu.class);
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


            //  Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            v.findViewById(R.id.newgameE).setOnClickListener(this);
            v.findViewById(R.id.mainmenuE).setOnClickListener(this);
            setBestime((TextView) v.findViewById(R.id.besttime));
            setThistime((TextView) v.findViewById(R.id.timetext));
            getDialog().setCanceledOnTouchOutside(false);//заборона закриття діалогу коли натиснуто поза ним
            return v;
        }


        void setBestime(TextView textView) {
            textView.setText("Кращий час - " + BestTime);
            SharedPreferences.Editor ed = sPref.edit();

            ed.putString(SAVED_TEXT, BestTime);
            ed.commit();
        }

        void setThistime(TextView textView) {

            textView.setText("Ваш час - " + time);
        }

        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.newgameE:
                    finish();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mainmenuE:
                    intent = new Intent(getActivity(), MainMenu.class);
                    startActivity(intent);
                    finish();
                    break;

            }
        }

    }

    public MainActivity() {
        super();
    }

    public void onDestroy() {

        mPlayer.stop();
        super.onDestroy();
    }
}
