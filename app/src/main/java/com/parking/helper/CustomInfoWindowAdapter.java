package com.parking.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {
private final View mWindow;
private  Context mContext;
private String url = MapActivity.getValue();
    ImageView mImage;


    public CustomInfoWindowAdapter(Context mContext) {
        this.mContext = mContext;

        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window,null);
         mImage = mWindow.findViewById(R.id.image);
       // Glide.with(mContext).load(url).into(mImage);

    }



    private  void renderWindowText(Marker marker,View view){
       // Picasso.get().load(Uri.parse(url)).resize(250,250).into(mImage);
        String title = marker.getTitle();
        TextView mTitle = view.findViewById(R.id.title);
        mTitle.setText(title);
        String snippet = marker.getSnippet();
        TextView mSmippet = view.findViewById(R.id.snippet);

        mSmippet.setText(snippet);
//        String imageUrl = marker.getSnippet();
//        ImageView mimageUrl = view.findViewById(R.id.image);

    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker,mWindow);
       // Glide.with(getInfoWindow(marker)).load(url).into(mImage);
        return mWindow;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
