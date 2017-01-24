package com.game.kolas.mygame.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String LEVEL_TABLE = "LEVEL_TABLE";
    public static final String DB_NAME = "DB";

    public static final String ID_KEY = "id";
    public static final String OPEN_KEY = "open";
    public static final String BEST_TIME_KEY = "best";

    public static final int DB_VERSION = 1;
 
    public DBHelper(Context context) {
      // конструктор суперкласса
      super(context, DB_NAME, null, DB_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {

      // создаем таблицу с полями
      db.execSQL("create table "+LEVEL_TABLE+"  ("
          + ID_KEY+" integer,"
          + OPEN_KEY+" integer,"
          + BEST_TIME_KEY+" text" + ");");
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
    }
  }