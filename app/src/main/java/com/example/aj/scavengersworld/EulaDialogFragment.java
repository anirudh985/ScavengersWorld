package com.example.aj.scavengersworld;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;

/**
 * Created by kalyan on 10/14/16.
 */

public class EulaDialogFragment extends DialogFragment {
    public void setEulaAccepted()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.prefs), 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(getString(R.string.eula_accepted_key), true).commit();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.about_app)
                .setMessage(Html.fromHtml(getString(R.string.eula)))
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        setEulaAccepted();
                    }
                })
                .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        getActivity().finish();
                    }
                });
        return builder.create();
    }
}
