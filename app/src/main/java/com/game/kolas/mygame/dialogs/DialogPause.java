package com.game.kolas.mygame.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.game.kolas.mygame.views.CustomFontTextView;
import com.game.kolas.mygame.GameActivity;
import com.game.kolas.mygame.R;

/**
 * Created by mykola on 23.01.17.
 */

public  class DialogPause extends DialogFragment implements View.OnClickListener {
    private static final String TIME = "TIME";
    private String mTime;
    private DialogMethods mCallbacks;

    public static DialogPause newInstance(String time) {

        Bundle args = new Bundle();
        args.putString(TIME,time);
        DialogPause fragment = new DialogPause();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_pause, null);
        v.findViewById(R.id.play).setOnClickListener(this);
        v.findViewById(R.id.newgame).setOnClickListener(this);
        v.findViewById(R.id.mainmenu).setOnClickListener(this);
        mTime = getArguments().getString(TIME);
        setThistime((CustomFontTextView) v.findViewById(R.id.textView));

        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(v).create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;

    }


    void setThistime(TextView textView) {
        textView.setText("Ваш час - " + mTime);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                mCallbacks.start();
                dismiss();
                break;
            case R.id.newgame:
                mCallbacks.newGame();
                dismiss();
                break;
            case R.id.mainmenu:
                mCallbacks.exitGame();
                break;

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (GameActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}
