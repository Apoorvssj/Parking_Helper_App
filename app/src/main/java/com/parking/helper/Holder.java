package com.parking.helper;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class Holder extends RecyclerView.ViewHolder {

    View view;
    public Holder(@NonNull View itemView) {
      super(itemView);

        view = itemView;

    }

    public void setView(final Context context, String carname, String carnumber, String name, String phone, String profileImageUrl) {
       TextView mtitle = view.findViewById(R.id.title);
       TextView msnippet= view.findViewById(R.id.snippet);
//        TextView mblood_group = view.findViewById(R.id.blood_group);
//        TextView mmobile = view.findViewById(R.id.mobile);
        ImageView imageView = view.findViewById(R.id.image);
//
       mtitle.setText(carname);
        msnippet.setText("Carnumber:"+carnumber);
//        mblood_group.setText(blood_group);
//        mmobile.setText(mobile);
        Glide.with(context).load(profileImageUrl).into(imageView);




//        imageView = HashMap.get("profileImageUrl").toString();
//        Glide.with(setView()).load(imageView).into(image);

    }
}
