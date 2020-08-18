package com.parking.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity   {

    private EditText mEmail,mPassword,mMobileNumber;
    private EditText mcountryCode;
    private TextView mTopText;
    private Button mLogin,mRegistration;
    private CheckBox mRemember;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static String valuephone;
    public static String getValue() {
        return valuephone;
    }
    private Typeface tfc1,tfc2;

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.style_1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
//        String checkbox = preferences.getString("remember","");
//        if(checkbox.equals("true")){
//            Intent intent = new Intent(MainActivity.this,MapActivity.class);
//            startActivity(intent);
//        }else{
//
//            Toast.makeText(MainActivity.this,"Please Sign In",Toast.LENGTH_SHORT).show();
//        }

        tfc1 = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-Regular.ttf");
        tfc2 = Typeface.createFromAsset(getAssets(),"fonts/Staatliches-Regular.ttf");
        mTopText = findViewById(R.id.topText);
        mTopText.setTypeface(tfc1);
        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //stores current user
                //user logs in and goes to next page
//                if(user!=null){
//                    Intent intent = new Intent(MainActivity.this,MapsActivity.class);//changes activity
//                    startActivity(intent);
//                    finish();
//                    return;
//                }
            }
        };
        mcountryCode = findViewById(R.id.countrycode);

      //  mEmail = findViewById(R.id.email);
        mMobileNumber = findViewById(R.id.mobileNumber);
      //  mPassword = findViewById(R.id.password);
       // mLogin = findViewById(R.id.login);
        mRegistration = findViewById(R.id.registration);
        mRegistration.setTypeface(tfc2);
      //  mRemember = findViewById(R.id.remember);


//        mRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(buttonView.isChecked()){
//                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("remember","true");
//                    editor.apply();
//                    Toast.makeText(MainActivity.this,"Checked",Toast.LENGTH_SHORT).show();
//                } else if(!buttonView.isChecked()){
//                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("remember","false");
//                    editor.apply();
//                    Toast.makeText(MainActivity.this,"UnChecked",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        //to register new user
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final String email = mEmail.getText().toString();
//                final String password = mPassword.getText().toString();
                final String number = mMobileNumber.getText().toString();
                valuephone=number;
                String countryCode = mcountryCode.getText().toString();
                if(number.isEmpty()){
                    mMobileNumber.setError("Phone no. cannot be empty");
                    mMobileNumber.requestFocus();

                   // Toast.makeText(MainActivity.this,"Please provide phone number and country code.",Toast.LENGTH_LONG).show();
               }else if(countryCode.isEmpty()){
                    mcountryCode.setError("Please specify country code");
                    mcountryCode.requestFocus();
                }
                else{

                    Intent intent = new Intent(getApplicationContext(), VerifyPhoneNo.class);
                    intent.putExtra("phoneNo", number);
                    intent.putExtra("CountryCode", countryCode);
                    startActivity(intent);

                }
//                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(!task.isSuccessful()){
//                            Toast.makeText(MainActivity.this,"sing up error",Toast.LENGTH_SHORT).show();
//
//                        } else{
//                            String user_id = mAuth.getCurrentUser().getUid();
////
//                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id); //realtime database reference
//                            current_user_db.setValue(true);
//                            Toast.makeText(MainActivity.this,"Registered pls tap on Login",Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });

        // login in user
//        mLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String email = mEmail.getText().toString();
//                final String password = mPassword.getText().toString();
//                final String number = mMobileNumber.getText().toString();
//                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(!task.isSuccessful()){
//                            Toast.makeText(MainActivity.this,"sing up error",Toast.LENGTH_LONG).show();
//
//                        }
//
//                        //else if no error onauthstatechanged is called and user goes to next activity
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        Intent intent = new Intent(MainActivity.this,User_info.class);//changes activity
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//            }
//        });
    }
    private void checkUserStatus(){

        user = mAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            startActivity(new Intent(MainActivity.this,MapActivity.class));
            finish();

        }

//        else
//        {
//            startActivity(new Intent(MainActivity.this,MapActivity.class));
//            finish();
//        }
    }



}
