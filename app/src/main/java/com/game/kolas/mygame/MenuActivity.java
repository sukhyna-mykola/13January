package com.game.kolas.mygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.game.kolas.mygame.data.DataGame;
import com.game.kolas.mygame.dialogs.DialogAbout;
import com.game.kolas.mygame.dialogs.DialogSetting;
import com.game.kolas.mygame.views.CustomFontTextView;


public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private CustomFontTextView changeDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        changeDialogs = (CustomFontTextView) findViewById(R.id.change_dialogs);
        changeDialogs.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DataGame.updateDialogs();
                Toast.makeText(MenuActivity.this, R.string.app_name, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_play:
                startActivity(new Intent(this, LevelActivity.class));
                finish();
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
