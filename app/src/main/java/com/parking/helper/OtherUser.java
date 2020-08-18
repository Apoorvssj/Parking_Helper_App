package com.parking.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OtherUser extends AppCompatActivity {
    String CarNumber1;
    String CarNumbersmall;
    String CarNumber2;
    String PhoneNumber;
    private Typeface tfc2;
    PD_otheruser progressDialog;
    private Button mconfirm1;
    private ImageView mProfileImage1;
    private TextInputEditText mNameField1;
   // private TextInputEditText mPhoneField1;
    private TextInputEditText mCarName1;
    private TextInputEditText mCarNumber1;
    private TextInputLayout mNameLayout;
   // private TextInputLayout mPhoneLayout;
    private TextInputLayout mCarNameLayout;
    private TextInputLayout mCarNumberLayout;
   // private EditText mNameField1;
//    private EditText mPhoneField1;
//    private EditText mCarName1;
//    private EditText mCarNumber1;
    private ImageButton mCall;
    private static final int REQUEST_CALL = 1;
//    private static String value_otheruser;
//    public static String getValue_otheruser() { return value_otheruser; }
   private  String i2 = "0";
    InterstitialAd interstitialAd3;
  // public static int geti2() { return i2; }
    View view1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.style_1);
        setContentView(R.layout.other_user);
       // AppCompatActivity myActivity = (AppCompatActivity) getApplicationContext();
        mconfirm1 = findViewById(R.id.confirm1);
        mCall = findViewById(R.id.call);
        mProfileImage1 = findViewById(R.id.profileImage1);
        mNameLayout = findViewById(R.id.nameLayout1);
//        mPhoneLayout = findViewById(R.id.phoneLayout1);
        mCarNameLayout = findViewById(R.id.carnameLayout1);
        mCarNumberLayout = findViewById(R.id.carnumberLayout1);
       mNameField1 = findViewById(R.id.nameField1);
      // mPhoneField1 = findViewById(R.id.phoneField1);
       mCarName1 = findViewById(R.id.carName1);
        mCarNumber1 = findViewById(R.id.carNumber1);
        tfc2 = Typeface.createFromAsset(getAssets(),"fonts/Staatliches-Regular.ttf");
        mconfirm1.setTypeface(tfc2);


        progressDialog = new PD_otheruser(OtherUser.this);

        Bundle bundle = getIntent().getExtras();
        CarNumber1 = bundle.getString("message1");
       // CarNumber1 = MapActivity.getCar1();
       if(CarNumber1!=null)
       {CarNumbersmall = CarNumber1.toLowerCase().replaceAll("\\s", "");}
        CarNumber2 = bundle.getString("message2");
        //CarNumber2 = MapActivity.getCar2();
        mNameField1.setKeyListener(null);
        //mPhoneField1.setKeyListener(null);
        mCarName1.setKeyListener(null);
        mCarNumber1.setKeyListener(null);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)‌​
      //  hideSoftKeyboard(myActivity);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                progressDialog();
            }
        });

        find();
        mconfirm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (interstitialAd3.isLoaded()) {
                    interstitialAd3.show();
                } else {

                    Intent intent = new Intent(OtherUser.this, MapActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

        interstitialAd3 = new InterstitialAd(this);
        interstitialAd3.setAdUnitId(getString(R.string.interstitial_ad3));
        interstitialAd3.loadAd(new AdRequest.Builder().build());
        interstitialAd3.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                interstitialAd3.loadAd(new AdRequest.Builder().build());
                Intent intent = new Intent(OtherUser.this, MapActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.d("ADd", "Error: " + adError.getMessage());
//                Intent intent = new Intent(OtherUser.this, MapActivity.class);
//                startActivity(intent);
//                finish();
            }
            @Override
            public void onAdLeftApplication() {
//                Intent intent = new Intent(FindCar.this,MapActivity.class);
//                startActivity(intent);
//                finish();
            }
        });

    }

    private void find(){
        List<String> userids = new ArrayList<>();
        String CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userId = FirebaseAuth.getInstance().getUid();
        //CarNumber = mSearchAddress.getText().toString();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot Snapshot: dataSnapshot.getChildren()){
                        //String userId = Snapshot.getValue().toString();
                        String userId = Snapshot.getKey();
                        userids.add(userId);
                    }
                    for(String differentUsers : userids) {
                        if (userids.equals(CurrentUserId)) continue;
                        child(differentUsers);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void child(String Users){
        DatabaseReference  mRef = FirebaseDatabase.getInstance().getReference("Users").child(Users);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();


                    //if this child exists
//                    if(map.get("phone")!=null) {
//                        PhoneNumber = map.get("phone").toString();
//
//                    }



                        if(map.get("search")!=null){
                        String mcarnumber = map.get("search").toString();
                        if(mcarnumber.equals(CarNumbersmall) || mcarnumber.equals(CarNumber2) ){

                            i2 = "1";
                            otherUserInfo(Users);

                        }




                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    public void hideSoftKeyboard() {
//        if(getCurrentFocus()!=null) {
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(myActivity.INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
//    }

    private void progressDialog(){



        progressDialog.startProgressDialog();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if(i2.equals("0")){
                    //  Toast.makeText(OtherUser.this,"No user found , Please Register",Toast.LENGTH_LONG).show();
                    new AlertDialog.Builder(OtherUser.this)
                            .setTitle("User not found")
                            .setMessage("Either car number does not exist or you have typed wrong car number.")
                            .setPositiveButton("OK", ((dialogInterface, i) -> {
                                Intent intent = new Intent(OtherUser.this,MapActivity.class);
                                startActivity(intent);
                            }))
                            .setCancelable(false)
                            .show();
                }
                else{
                    Toast.makeText(OtherUser.this,i2+" User found",Toast.LENGTH_LONG).show();
                }
            }

        },5000);
    }

    private  void otherUserInfo(String U){
//        i2 = 1;
//        value_otheruser = "Fetching..";




        DatabaseReference  mRef = FirebaseDatabase.getInstance().getReference("Users").child(U);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if values exists
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    //if this child exists
                    if(map.get("name")!=null){
                       // String a = mNameLayout.getEditText().toString();
                        mNameField1.setText(map.get("name").toString());
                    }
//                    if(map.get("phone")!=null){
//
//                        PhoneNumber = map.get("phone").toString();
//                        mPhoneField1.setText(map.get("phone").toString());
//                    }
                    if(map.get("carname")!=null){

                        mCarName1.setText(map.get("carname").toString());
                    }
                    if(map.get("carnumber")!=null){

                        mCarNumber1.setText(map.get("carnumber").toString());
                    }
                    if(map.get("profileImageUrl")!=null){

                       // Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(mProfileImage1);
                        Glide
                                .with(getApplication())
                                .load(map.get("profileImageUrl"))
                                .circleCrop()
                                .into(mProfileImage1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void makeCall(){
       if(ContextCompat.checkSelfPermission(OtherUser.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(OtherUser.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL); //String array defines permissions we want to request,in our case it is only 1
       }else{
           String p = "tel:"+PhoneNumber;
//           Intent intent = new Intent(Intent.ACTION_CALL);
//           intent.setData(Uri.parse(p));
//           startActivity(intent);
           startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(p)));
       }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){ //grantResults[0] checks for fist permission in string array
                makeCall();
            }else{
                Toast.makeText(OtherUser.this,"Can't make phone call.Permission not Granted",Toast.LENGTH_LONG).show();
            }
        }
    }
}
