package com.game.kolas.mygame;

import com.game.kolas.mygame.objects.Level;

import java.util.ArrayList;

/**
 * Created by mykola on 25.12.16.
 */

public class DataGame {

 public  static ArrayList<Level>  levels = new ArrayList<>();

    static {
        levels.add(new Level(0,true,"Сова",R.drawable.owl,"00:00"));
        levels.add(new Level(1,false,"Ч. смерть",R.drawable.petardy,"00:00"));
        levels.add(new Level(2,false,"Укр. кухня",R.drawable.dumpling,"00:00"));
    }


}
