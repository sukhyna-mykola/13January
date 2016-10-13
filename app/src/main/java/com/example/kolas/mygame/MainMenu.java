package com.example.kolas.mygame;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends Activity implements View.OnClickListener {
    Intent intent;
    private static final String MY_SETTINGS = "my_settings";
    final String CHECK = "sound";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        final Button button1 = (Button) findViewById(R.id.button);
        final Button button2 = (Button) findViewById(R.id.button2);
        final Button button3 = (Button) findViewById(R.id.button3);
        final Button button4 = (Button) findViewById(R.id.button4);
        SharedPreferences sp = getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        // проверяем, первый ли раз открывается программа
        boolean hasVisited = sp.getBoolean("hasVisited", false);

        SharedPreferences.Editor e = sp.edit();
        e.putBoolean(CHECK, true);
        // e.commit();
        if (!hasVisited) {
            // выводим нужную активность

            e.putString("saved_text", "0:00");
            e.putBoolean("hasVisited", true);
            e.commit(); // не забудьте подтвердить изменения
        }
// устанавливаем один обработчик для всех кнопок
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

// анализируем, какая кнопка была нажата. Всего один метод для всех кнопок
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button2:
                Intent intent = new Intent(this, Setting.class);
                startActivity(intent);
                // Toast.makeText(MainMenu.this, "На стадії розробки", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.button4:
                finish();

                break;
        }
    }
}
