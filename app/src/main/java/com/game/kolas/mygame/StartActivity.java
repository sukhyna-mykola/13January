package com.game.kolas.mygame;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.game.kolas.mygame.data.DBHelper;
import com.game.kolas.mygame.objects.Level;

import static com.game.kolas.mygame.data.DBHelper.BEST_TIME_KEY;
import static com.game.kolas.mygame.data.DBHelper.ID_KEY;
import static com.game.kolas.mygame.data.DBHelper.LEVEL_TABLE;
import static com.game.kolas.mygame.data.DBHelper.OPEN_KEY;
import static com.game.kolas.mygame.data.DataGame.levels;
import static com.game.kolas.mygame.dialogs.DialogSetting.MY_SETTINGS;
import static com.game.kolas.mygame.dialogs.DialogSetting.SOUND;
import static com.game.kolas.mygame.dialogs.DialogSetting.sound;


public class StartActivity extends AppCompatActivity {
    private SharedPreferences sPref;
    public static final String IS_CREATED_BD = "is_created_DB1";

    public static final int TRUE = 0;
    public static final int FALSE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        sPref = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        if (!checkIsCreatedBD()) {
            putToBD();
            saveIsCreatedBD();
        }

        sound = sPref.getBoolean(SOUND,false);

        readFromBD();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(RESULT_OK);
                finish();
            }
        },2000);

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
        return sPref.getBoolean(IS_CREATED_BD, false);
    }

    private  void readFromBD() {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(LEVEL_TABLE, null, null, null, null, null, null);

        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex(ID_KEY);
            int openColIndex = c.getColumnIndex(OPEN_KEY);
            int bestColIndex = c.getColumnIndex(BEST_TIME_KEY);

            do {
                int id = c.getInt(idColIndex);

                levels.get(id).setId(id);
                levels.get(id).setBestTime(c.getString(bestColIndex));
                levels.get(id).setOpen(c.getInt(openColIndex) == 0 ? true : false);

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        dbHelper.close();
    }

}
