package com.game.kolas.mygame.data;

import com.game.kolas.mygame.R;
import com.game.kolas.mygame.objects.Level;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by mykola on 25.12.16.
 */

public class DataGame {

    public static ArrayList<Level> levels = new ArrayList<>();

    static {
        levels.add(new Level(0, true, "Сова", R.drawable.level_owl, "00:00"));
        levels.add(new Level(1, false, "Байкер", R.drawable.level_biker, "00:00"));
        levels.add(new Level(2, false, "Мисливець", R.drawable.level_hunter, "00:00"));
        levels.add(new Level(3, false, "Вершник", R.drawable.level_horse, "00:00"));
    }

    public static final LinkedHashMap<String, String> dialogs = new LinkedHashMap<>();

    static {
        dialogs.put("Cтояти гівнюк", "Відчепись");
        dialogs.put("Тобi капець", "Iди в *опу");
        dialogs.put("Стій скотиняка", "Дідька лисого");
    }

    public static void updateDialogs(){
        dialogs.clear();
        dialogs.put("Cтiй уїбень", " Отїбись ");
        dialogs.put("Тобi пизда", "Iди нахуй");
        dialogs.put("Віддай ворота, сука", "   Соси   ");
        dialogs.put("Щас захуярю", "Іди в пізду");

    }
}
