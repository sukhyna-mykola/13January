package com.game.kolas.mygame.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.game.kolas.mygame.R;


/**
 * Created by mikola on 05.12.2016.
 */

public class DialogSetting extends DialogFragment  {

        public static final String MY_SETTINGS = "my_settings";
        public static final String SOUND = "SOUND";
        private CheckBox checkBox;
        private SharedPreferences sPref;

        public static boolean sound;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_setting, null);


        sPref = getContext().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        checkBox.setChecked(sound);


        return new AlertDialog.Builder(getActivity())
                .setView(v).create();
    }

    public void onDestroy() {
        super.onDestroy();
        sound = checkBox.isChecked();
        SharedPreferences.Editor ed1 = sPref.edit();
        ed1.putBoolean(SOUND, sound);
        ed1.commit();

    }
}
