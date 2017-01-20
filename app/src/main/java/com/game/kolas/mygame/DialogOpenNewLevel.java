package com.game.kolas.mygame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by mikola on 05.12.2016.
 */

public class DialogOpenNewLevel extends DialogFragment  {

    private CustomFontTextView newLevelText;
    public static final String OPEN_LEVEL = "OPEN_LEVEL";

    public static DialogOpenNewLevel newInstance(int level) {

        Bundle args = new Bundle();
        args.putInt(OPEN_LEVEL,level);
        DialogOpenNewLevel fragment = new DialogOpenNewLevel();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.new_level_layout, null);

        newLevelText = (CustomFontTextView) v.findViewById(R.id.new_level_text);
        newLevelText.setText("Вiдкрито рiвень "+getArguments().getInt(OPEN_LEVEL));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        },1000);


        return new AlertDialog.Builder(getActivity())
                .setView(v).create();
    }

}
