package com.game.kolas.mygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MenuActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_play:
                startActivity(new Intent(this, LevelActivity.class));
                break;
            case R.id.button_settings:
                new DialogSetting().show(getSupportFragmentManager(), "Setting_DIALOG");
                break;
            case R.id.button_about:
                new DialogAbout().show(getSupportFragmentManager(), "About_DIALOG");
                break;
            case R.id.button_exit:
                finish();
                break;
        }
    }
}
