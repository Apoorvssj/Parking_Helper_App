package com.parking.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public class InternetDialog {
    Activity activity;
    AlertDialog alertDialog;
    InternetDialog(Activity myActivity){
        activity = myActivity;
    }

    void startInternetDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.internet_dialog,null));

//              builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dismiss();
//            }
//        });
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        alertDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        alertDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

    }
    void dismiss(){
        alertDialog.dismiss();
    }
}
