package com.parking.helper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class ContactUs extends AppCompatActivity {
    private Button mail;
    private TextView mname,mContent,m4;
    //private CardView mcard;
    private Typeface tfc1,tfc2,tfc3;

    AdView adView;
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.style_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        mail = findViewById(R.id.button);
        mail.setTypeface(tfc2);
        mname = findViewById(R.id.intro);
       // mcard = findViewById(R.id.cardView);
        mContent = findViewById(R.id.contact);
        //m4 = findViewById(R.id.textView4);
        mContent.setTypeface(tfc1);
        //m4.setTypeface(tfc1);
        SharedPreferences prefs = getSharedPreferences("myKey", MODE_PRIVATE);
        String name = prefs.getString("name", "");
        mname.setText("Hey,"+name);

        tfc1 = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-Regular.ttf");
        tfc2 = Typeface.createFromAsset(getAssets(),"fonts/Staatliches-Regular.ttf");
        tfc3 = Typeface.createFromAsset(getAssets(),"fonts/Lemonada-Regular.ttf");
        mname.setTypeface(tfc3);

//        mcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(
//                        "https://play.google.com/store/apps/details?id=com.bloodbank.anew"));
//                intent.setPackage("com.android.vending");
//                startActivity(intent);
//            }
//        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = "quantaappdevs@gmail.com";
                String subject="Regarding Parking Helper app.";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                //Intent emailIntent = new Intent(Intent.ACTION_VIEW);
              //  emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL,  new String[]{ to});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// this will make such that when user returns to your app, your app is displayed, instead of the email app.
               // emailIntent.setType("text/plain");
               // emailIntent.setType("message/rfc822");
                try {//we use try and cath because if mail app is not installed it will through an exception
                    startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(ContactUs.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    //    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//        try {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//        } catch (android.content.ActivityNotFoundException anfe) {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//        }

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //adView.setAdUnitId(getString(R.string.banner_ad1));
        adView.loadAd(adRequest);

    }
}