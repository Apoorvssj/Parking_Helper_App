package com.parking.helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class User_info extends AppCompatActivity {
    private Button mconfirm;

private ImageView mProfileImage;
//    private EditText mNameField;
//    private EditText mPhoneField;
//    private EditText mCarName;
//    private EditText mCarNumber;
PD_user progressDialog;
int i = 0;
    private TextInputEditText mNameField;
    private TextInputEditText mPhoneField;
    private TextInputEditText mCarName;
    private TextInputEditText mCarNumber;
 private FirebaseAuth mAuth;
 private DatabaseReference mUserDatabase;
 private  String user_id;
 private String mName;
 private String mcarname;
 private String mcarnumber;
 private String mPhone;
 private  Uri resultUri;
 private String mProfileimgUrl;
 private  String mobile1;
    private String number = MainActivity.getValue();
   private byte[] data;
    Map<String, Object> user_info;
    private Typeface tfc2;
    InterstitialAd interstitialAd4;

//    private static String value_user;
//    private static int i1;
//    public static String getValue_user() { return value_user; }
//    public static int geti1() { return i1; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.style_1);
        setContentView(R.layout.user_info);

        progressDialog = new PD_user(User_info.this);
        mconfirm = findViewById(R.id.confirm);
        tfc2 = Typeface.createFromAsset(getAssets(),"fonts/Staatliches-Regular.ttf");
        mconfirm.setTypeface(tfc2);
        mProfileImage = findViewById(R.id.profileImage);
        mNameField = findViewById(R.id.nameField);
        mPhoneField = findViewById(R.id.phoneField);
        mCarName = findViewById(R.id.carName);
        mCarNumber = findViewById(R.id.carNumber);
        mPhoneField.setKeyListener(null);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mobile1 = mPhoneField.getText().toString().trim();
        if(mobile1.isEmpty()|| !number.isEmpty()){
            mPhoneField.setText(number);
        }
        getUserInfo();
        mPhoneField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(User_info.this,"To change your phone no.,Please,re-Login with the new number",Toast.LENGTH_LONG).show();
            }
        });
        mconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameField.getText().toString().trim();
                String mobile = mPhoneField.getText().toString().trim();
                String carName = mCarName.getText().toString().trim();
                String carNumber = mCarNumber.getText().toString().trim();
                isValid(name,mobile,carName,carNumber);
//                saveUserInfo();
               // Map<String,Object> hashMap = new HashMap<>();
               // hashMap= user_info;
//                Intent intent = new Intent(User_info.this,MapActivity.class);
//                //intent.putExtra("hashMap", (Serializable) hashMap);
//                startActivity(intent);
//                finish();
            }
        });

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent,3);    //after opening gallery we want something from it
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select image"),3);
            }
        });

        interstitialAd4 = new InterstitialAd(this);
        interstitialAd4.setAdUnitId(getString(R.string.interstitial_ad1));
        interstitialAd4.loadAd(new AdRequest.Builder().build());
        interstitialAd4.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                interstitialAd4.loadAd(new AdRequest.Builder().build());
                Intent intent = new Intent(User_info.this,MapActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.d("ADd", "Error: " + adError.getMessage());
//                Intent intent = new Intent(User_info.this,MapActivity.class);
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

    private boolean isValid(String name,String mobile,String carname,String carnumber){

            if(name.isEmpty()){
                mNameField.setError("Name cannot be empty");
                mNameField.requestFocus();
            }

            else if(mobile.length()!=10) {
                mPhoneField.setError("Invalid mobile number");
                mPhoneField.requestFocus();
            }
            else if(carname.isEmpty()){
                mCarName.setError("Field cannot be empty.");
                mCarName.requestFocus();
            }
            else if(carnumber.isEmpty()){
                mCarNumber.setError("Field cannot be empty.");
                mCarNumber.requestFocus();
            }
            else{
                saveUserInfo();
                SharedPreferences.Editor editor = getSharedPreferences("myKey", MODE_PRIVATE).edit();
                editor.putString("name", mName);
                editor.apply();
                Toast.makeText(this,"Registered Successfully!",Toast.LENGTH_SHORT).show();
                if (interstitialAd4.isLoaded()) {
                    interstitialAd4.show();
                }else{
                    Intent intent = new Intent(User_info.this,MapActivity.class);
                    //intent.putExtra("hashMap", (Serializable) hashMap);
                    startActivity(intent);
                    finish();
                }

            }

            return true;
        }


    private  void getUserInfo(){
//        i1 = 1;
//        value_user = "Fetching..";
        progressDialog.startProgressDialog();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if(i==0){
                    Toast.makeText(User_info.this,"No user found , Please Register",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(User_info.this,i+" User found",Toast.LENGTH_LONG).show();
                }
            }

        },5000);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if values exists
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    i = 1;
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    //if this child exists
                    if(map.get("name")!=null){
                        mName = map.get("name").toString();
                        mNameField.setText(mName);
                    }
                    if(map.get("phone")!=null){
                        mPhone = map.get("phone").toString();
                        if(mobile1.isEmpty()){
                        mPhoneField.setText(mPhone);}
                    }
                    if(map.get("carname")!=null){
                        mcarname = map.get("carname").toString();
                        mCarName.setText(mcarname);
                    }
                    if(map.get("carnumber")!=null){
                        mcarnumber = map.get("carnumber").toString();
                        mCarNumber.setText(mcarnumber);
                    }
                    if(map.get("profileImageUrl")!=null){
                        mProfileimgUrl = map.get("profileImageUrl").toString();
                      //  Glide.with(getApplication()).load(mProfileimgUrl).into(mProfileImage);
                        Glide
                                .with(getApplication())
                                .load(mProfileimgUrl)
                                .circleCrop()
                                .into(mProfileImage);
                    }
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void saveUserInfo() {


        mName = mNameField.getText().toString();
        mPhone = mPhoneField.getText().toString();
        mcarname = mCarName.getText().toString();
        mcarnumber = mCarNumber.getText().toString();
        user_info = new HashMap<>();
        user_info.put("name", mName);
        user_info.put("phone", mPhone);
        user_info.put("carname", mcarname);
        user_info.put("carnumber", mcarnumber);
        user_info.put("search", mcarnumber.toLowerCase().replaceAll("\\s", ""));

        mUserDatabase.updateChildren(user_info);
        if (resultUri != null) {
            //StorageReference works same as database reference but dor storage section of firebase
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(user_id);
            //now this bitmap thing is gonna pass image location(resultUri) to bitmap
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (InputStream inputStream = User_info.this.getContentResolver().openInputStream(resultUri)) {
                ExifInterface exif = new ExifInterface(inputStream);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                Bitmap bmRotated = rotateBitmap(bitmap, orientation);
                // now image compression
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmRotated.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                //creating variable of type data and moving image to an array to store images to the firebase storage
                 data = baos.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
//           // now image compression
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
//            //creating variable of type data and moving image to an array to store images to the firebase storage
//            byte[] data = baos.toByteArray();


            UploadTask uploadTask = filePath.putBytes(data);  //upload task uploads the image to storage firebase

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                    return;
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();

                    Map newImage = new HashMap();
                    newImage.put("profileImageUrl", downloadUrl.toString());
                    mUserDatabase.updateChildren(newImage);

                    finish();
                    return;
                }
            });




        } else {
            finish();
        }
    }
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();  // Uri is the location where image is stored
          resultUri=imageUri;
           mProfileImage.setImageURI(resultUri);
//            try {
//                Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
//                mProfileImage.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(User_info.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            // your code
//            Intent intent = new Intent(User_info.this,MainActivity.class);
//            startActivity(intent);
//            finish();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
}
