package com.example.kolas.mygame;

import android.app.Activity;
import android.app.DialogFragment;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Setting extends Activity implements View.OnClickListener {
    private static final String MY_SETTINGS = "my_settings";
    SharedPreferences sPref;
    final String SAVED_TEXT = "saved_text";
    final String CHECK = "sound";
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button sett=(Button)findViewById(R.id.sett);


        sett.setOnClickListener(this);
        sPref =getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        checkBox =(CheckBox)findViewById(R.id.checkBox);

        checkBox.setChecked(sPref.getBoolean(CHECK, true));


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sett:
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(SAVED_TEXT,"0:00");
                ed.commit();
                break;

        }
    }
    public void onDestroy() {
        SharedPreferences.Editor ed1 = sPref.edit();
        ed1.putBoolean(CHECK,checkBox.isChecked());
        ed1.commit();

        super.onDestroy();

    }

    }

