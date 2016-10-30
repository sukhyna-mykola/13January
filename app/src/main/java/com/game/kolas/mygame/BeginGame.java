package com.game.kolas.mygame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BeginGame extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_game);
        final Intent intent = new Intent(this, MainMenu.class);

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
}
