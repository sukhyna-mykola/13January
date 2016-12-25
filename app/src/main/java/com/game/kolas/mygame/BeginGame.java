package com.game.kolas.mygame;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.game.kolas.mygame.level.Level;

import static com.game.kolas.mygame.DBHelper.BEST_TIME_KEY;
import static com.game.kolas.mygame.DBHelper.ID_KEY;
import static com.game.kolas.mygame.DBHelper.LEVEL_TABLE;
import static com.game.kolas.mygame.DBHelper.OPEN_KEY;
import static com.game.kolas.mygame.DataGame.levels;
import static com.game.kolas.mygame.MainActivity.MY_SETTINGS;

public class BeginGame extends Activity {
    private SharedPreferences sPref;
    public static final String IS_CREATED_BD = "is_created_DB1";

    public static final int TRUE = 0;
    public static final int FALSE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_game);
        final Intent intent = new Intent(this, MainMenu.class);
        sPref = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        if (!checkIsCreatedBD()) {
            putToBD();

            saveIsCreatedBD();
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(intent);


            }
        });
        thread.start();

    }

    public void onPause() {
        super.onPause();
        finish();

    }

    private void putToBD() {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Level l : levels) {
            ContentValues cv = new ContentValues();
            cv.put(ID_KEY, l.getId());
            cv.put(BEST_TIME_KEY, l.getBestTime());
            cv.put(OPEN_KEY, l.isOpen() ? TRUE : FALSE);
            // вставляем запись и получаем ее ID
            db.insert(LEVEL_TABLE, null, cv);

        }

    }


    private void saveIsCreatedBD() {
        SharedPreferences.Editor ed = sPref.edit();

        ed.putBoolean(IS_CREATED_BD, true);
        ed.commit();
    }

    private boolean checkIsCreatedBD() {
        Toast.makeText(this, String.valueOf(sPref.getBoolean(IS_CREATED_BD, false)), Toast.LENGTH_SHORT).show();
        return sPref.getBoolean(IS_CREATED_BD, false);
    }
}
