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

public class DialogEnd extends DialogFragment implements View.OnClickListener {

    private static final String TIME = "TIME";
    private static final String BEST_TIME = "BEST_TIME";

    private String mTime;
    private String mBestTime;

    private DialogMethods mCallbacks;

    public static DialogEnd newInstance(String time, String bestTime) {

        Bundle args = new Bundle();
        args.putString(TIME, time);
        args.putString(BEST_TIME, bestTime);
        DialogEnd fragment = new DialogEnd();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_end_game, null);

        v.findViewById(R.id.newgameE).setOnClickListener(this);
        v.findViewById(R.id.mainmenuE).setOnClickListener(this);

        mTime = getArguments().getString(TIME);
        mBestTime = getArguments().getString(BEST_TIME);
        setBestime((CustomFontTextView) v.findViewById(R.id.besttime));
        setThistime((CustomFontTextView) v.findViewById(R.id.timetext));
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(v).create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;

    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.newgameE:
                mCallbacks.newGame();
                dismiss();
                break;
            case R.id.mainmenuE:
                mCallbacks.exitGame();
                break;

        }
    }

    void setBestime(TextView textView) {
        textView.setText("Кращий час - " + mBestTime);
    }

    void setThistime(TextView textView) {

        textView.setText("Ваш час - " + mTime);
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
