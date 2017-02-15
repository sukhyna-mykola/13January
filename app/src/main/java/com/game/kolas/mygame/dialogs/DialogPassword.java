package com.game.kolas.mygame.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.game.kolas.mygame.GameActivity;
import com.game.kolas.mygame.MenuActivity;
import com.game.kolas.mygame.R;
import com.game.kolas.mygame.data.DataGame;
import com.game.kolas.mygame.views.CustomFontTextView;

/**
 * Created by mykola on 23.01.17.
 */

public  class DialogPassword extends DialogFragment implements View.OnClickListener {
    private EditText password;
    private CustomFontTextView errorPasword;
    public static final String PASSWORD = "12345678";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_password, null);
        v.findViewById(R.id.confirm_password).setOnClickListener(this);
        password = (EditText) v.findViewById(R.id.input_password);
        errorPasword = (CustomFontTextView) v.findViewById(R.id.text_error_password);
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(v).create();
        return dialog;

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_password:
                String passwordText = password.getText().toString();
                if(passwordText.equals(PASSWORD)){
                    DataGame.updateDialogs();
                    Toast.makeText(getContext(), R.string.mode_turn_on, Toast.LENGTH_SHORT).show();
                    dismiss();
                }else {
                  errorPasword.setVisibility(View.VISIBLE);
                }

                break;

        }

    }

}
