package com.parking.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class PD_otheruser {

    Activity activity;
    AlertDialog alertDialog;
//    private String title = MapActivity.getValue1();
//    private String check = User_info.getValue_user();
//    private String check1 = OtherUser.getValue_otheruser();
//    private int i1 = User_info.geti1();
//    private int i2 = OtherUser.geti2();
//    private TextView mtext;




    PD_otheruser(Activity myActivity){
        activity = myActivity;
    }
    void startProgressDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.pd_user,null));
        builder.setCancelable(false);
        alertDialog = builder.create();

//        if(i1==1){
//
//            alertDialog.setTitle(check);
//        }else if(i2==1) {
//            alertDialog.setTitle(check1);
//        }else{
//            alertDialog.setTitle(title);
//        }

        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
    }
    void dismiss(){
        alertDialog.dismiss();
    }
}
