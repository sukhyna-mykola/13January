package com.game.kolas.mygame;

import com.game.kolas.mygame.level.Level;

import java.util.ArrayList;

/**
 * Created by mykola on 25.12.16.
 */

public class DataGame {

 public  static ArrayList<Level>  levels = new ArrayList<>();

    static {
        levels.add(new Level(0,true,"Просто",R.drawable.arm,"00:00"));
        levels.add(new Level(1,true,"Середня скланість",R.drawable.arm_yes,"00:00"));
        levels.add(new Level(2,false,"Важко",R.drawable.but,"00:00"));
        levels.add(new Level(3,false,"Важко",R.drawable.but,"00:00"));
    }


}
