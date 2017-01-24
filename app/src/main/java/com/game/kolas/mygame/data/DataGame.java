package com.game.kolas.mygame.data;

import com.game.kolas.mygame.R;
import com.game.kolas.mygame.objects.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by mykola on 25.12.16.
 */

public class DataGame {

    public static ArrayList<Level> levels = new ArrayList<>();

    static {
        levels.add(new Level(0, true, "Сова", R.drawable.owl, "00:00"));
        levels.add(new Level(1, false, "Ч. смерть", R.drawable.petardy, "00:00"));
        levels.add(new Level(2, false, "Мисливець", R.drawable.hunter, "00:00"));
        levels.add(new Level(3, false, "Вершник", R.drawable.horse, "00:00"));
    }

    public static final LinkedHashMap<String, String> dialogs = new LinkedHashMap<>();

    static {
        dialogs.put("Cтiй гівнюк", "Відчепись");
        dialogs.put("Тобi капець", "Iди в *опу");
    }

    public static void updateDialogs(){
        dialogs.clear();
        dialogs.put("Cтiй уїбень", " Отїбись ");
        dialogs.put("Тобi пизда", "Iди нахуй");
        dialogs.put("Віддай ворота, сука", "   Соси   ");

    }
}
