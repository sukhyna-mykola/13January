package com.game.kolas.mygame.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.game.kolas.mygame.R;


/**
 * Created by mikola on 05.12.2016.
 */

public class DialogAbout extends DialogFragment  {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_about, null);

        return new AlertDialog.Builder(getActivity())
                .setView(v).create();
    }


}
