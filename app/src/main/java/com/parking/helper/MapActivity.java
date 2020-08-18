package com.parking.helper;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{//, IOnLoadLocationListener {
    private ImageButton mBtnLocate;
    private Button mSave;
    private TextView coordinates;
    private SearchView mSearchView;
    private TextView mtextView2;
    private EditText mSearchAddress;
    private ImageButton mCurrentLocation;
    HandlerThread mHandlerThread;
    private GoogleMap mMap;
    private String customerId = "";
    private String lcn;
    private float rideDistance;
    Location mLastLocation;
    Location location;
    Marker marker;
    Marker markers;
    Circle circle;
   // private Button mLogout;
    private Button mScan;
   // private Button mInfo;
    InterstitialAd interstitialAd1;

    private boolean isLoggingOut = false;
    private boolean isDeletingAccount = false;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private FusedLocationProviderClient mLocationClient;
    public static final int Permission_request_code = 1;
    public static final int GPS_REQUEST_CODE = 2;
    public static final int DEFAULT_ZOOM = 15;
    private LocationCallback mLocationCallback;
    private ArrayList<String> Multi;
    private StringBuilder builder;
    private List<MyLatLng> coords;
    LatLng center;
    double radius;
    float distance1;
    private Typeface tfc1,tfc2,tfc3;
    private List<Marker> markerss = new ArrayList<Marker>();
    HashMap<String,Marker> hashMapMarker ;
    String locationName;
    String CarName;
    String CarNumber;
    private String ProfileimgUrl;
    SearchView searchView;
    private TextView mDay;
    private String Name;
    private static String value;
    //private static String value1;
   // private static String value2;
   // private static int value1 = OtherUser.geti2();
    public static String getValue() {
        return value;
    }
    //public static String getCar1() { return value1; }
    //public static String getCar2() { return value2; }
//    public static String getValue2() {
//        return value2;
//    }
    DatabaseReference MRef;
    ProgressDialog progressDialog;
    PD_save pd_save;
    int i=0;
    private int k = 1;
    private String  userId = FirebaseAuth.getInstance().getCurrentUser().getUid();



    // private IOnLoadLocationListener listener;
//    private LocationCallback mLocationCallback1;




   // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.style_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar;
        toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        interstitialAd1 = new InterstitialAd(this);
        interstitialAd1.setAdUnitId(getString(R.string.interstitial_ad1));
        interstitialAd1.loadAd(new AdRequest.Builder().build());
        tfc1 = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-Regular.ttf");
        tfc2 = Typeface.createFromAsset(getAssets(),"fonts/Staatliches-Regular.ttf");
        tfc3 = Typeface.createFromAsset(getAssets(),"fonts/Lemonada-Regular.ttf");
//        Intent intent = getIntent();
//        HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("hashMap");
        mDay = findViewById(R.id.day);
        mDay.setTypeface(tfc3);
//        Bundle bundle = getIntent().getExtras();
//        Name = bundle.getString("name");


        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        SharedPreferences prefs = getSharedPreferences("myKey", MODE_PRIVATE);
        String name = prefs.getString("name", "");
        if(timeOfDay < 12){

            mDay.setText("Good Morning , "+name);
           // Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
        }else if(timeOfDay < 16){
            mDay.setText("Good Afternoon , "+name);
           // Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();
        }else if(timeOfDay < 21){
            mDay.setText("Good Evening , "+name);
           // Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();
        }else if(timeOfDay < 24){
            mDay.setText("Good Night , "+name);
           // Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();
        }





         progressDialog = new ProgressDialog(MapActivity.this);
         pd_save = new PD_save(MapActivity.this);
        InternetDialog internetDialog = new InternetDialog(MapActivity.this);
        boolean isConnected = isNetworkConnected();
        if(!isConnected){
            internetDialog.startInternetDialog();
        }
       // mBtnLocate = findViewById(R.id.imageButton);
      //  mtextView2 = findViewById(R.id.textView2);
        mCurrentLocation = findViewById(R.id.imageButton2);
       // mSearchAddress = findViewById(R.id.editText2);
       // coordinates = findViewById(R.id.textView);
        mScan = findViewById(R.id.scan);
        mScan.setTypeface(tfc2);
        mSave = findViewById(R.id.save);
        mSave.setTypeface(tfc2);
//        mSearchView = findViewById(R.id.search);
//      //  mSearchView.setQueryHint("Enter Car number");
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if(query.matches("")){
//                            Toast.makeText(MapActivity.this,"Please enter car number to search for",Toast.LENGTH_SHORT).show();
//                        }else {
//
//                            Intent intent = new Intent(MapActivity.this,OtherUser.class);
//                            intent.putExtra("message1", query);
//                            startActivity(intent);
//                            finish();
//                        }
//                        return true;
//
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

       // mBtnLocate.setOnClickListener(this::geolocate);
//        mBtnLocate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                locationName = mSearchAddress.getText().toString();
//                if(locationName.matches("")){
//                    Toast.makeText(MapActivity.this,"Please enter car number to search for",Toast.LENGTH_SHORT).show();
//                }else {
//
//                    Intent intent = new Intent(MapActivity.this,OtherUser.class);
//                    intent.putExtra("message1", locationName);
//                    startActivity(intent);
//                    finish();
//                }
//
//            }
//        });


//        mLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("remember","false");
//                editor.apply();
//
//                isLoggingOut =true;
//               // remove_id();
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(MapActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();
//                return;
//            }
//        });
//        mInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MapActivity.this,User_info.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        mCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k=1;
                getLocationUpdates();

                //fetchLocation();

            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                k = 0;
                getLocationUpdates();
                saveCurrentLocation();
            }
        });

        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(marker!=null  ) {
                    inArea();
                    mMap.clear();

                    //value1 = "Scanning..";

                    progressDialog.startProgressDialog();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if (interstitialAd1.isLoaded()) {
                                interstitialAd1.show();
                            }
                            if(i==0){
                                Toast.makeText(MapActivity.this,"There are no users nearby",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(MapActivity.this,i+" users found.",Toast.LENGTH_LONG).show();
                            }
                        }

                    },5000);


                }
                else {
                    Toast.makeText(MapActivity.this,"Please first register your current location, by tapping on the button on top left of the google map",Toast.LENGTH_LONG).show();
                }



               // mMap.addCircle(new CircleOptions().center(new LatLng(lat, lng)).radius(500).strokeColor(Color.RED).fillColor(0x00000000).strokeWidth(5.0f));
            }




        });
        mLocationClient = LocationServices.getFusedLocationProviderClient(this); // or new FusedLocationProviderClient(this)
        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //super.onLocationResult(locationResult);
                if(locationResult==null){
                    return;
                }

                 location = locationResult.getLastLocation();


//                Toast.makeText(MapActivity.this, location.getLatitude() + " \n" +
//                        location.getLongitude(), Toast.LENGTH_SHORT).show();
                if(k == 1){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        coordinates.setText(location.getLatitude() + ":" + location.getLongitude());
                        // getCurrentLocation();
                        gotoLocation(location.getLatitude(), location.getLongitude());

                        showMarker(location.getLatitude(), location.getLongitude(),"Me");
                       // mMap.addCircle(new CircleOptions().center(new LatLng(location.getLatitude(), location.getLongitude())).radius(500).strokeColor(Color.RED).fillColor(0x00000000).strokeWidth(5.0f));


                        Log.d("Location", "inside runOnUiThread method: Thread name: " + Thread.currentThread().getName());

                    }
                }); }

                Log.d("Location", "onLocationResult: " + location.getLatitude() + " \n" +
                        location.getLongitude());
                Log.d("Location", "onLocationResult: Thread name: " + Thread.currentThread().getName());
//                String location_string = "" + location.getLatitude() + "//" + location.getLongitude();
//               coordinates.setText(location_string);
               // coordinates.setText(location.getLatitude()+":"+location.getLongitude());
                if(!isLoggingOut && !isDeletingAccount){
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("useravailable");
                GeoFire geoFire = new GeoFire(refAvailable);
                geoFire.setLocation(userId,new GeoLocation(location.getLatitude(),location.getLongitude()));}
//                FirebaseDatabase.getInstance().getReference("useravailable").child("userId").child(userId).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });



            }
        };

        interstitialAd1.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                interstitialAd1.loadAd(new AdRequest.Builder().build());
            }
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.d("ADd", "Error: " + adError.getMessage());
            }
        });




    }

    private void saveCurrentLocation(){
                        SharedPreferences preferences = getSharedPreferences("location",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("current_location",userId);
                editor.apply();
                editor.commit();


       // value1 = "Saving..";
        pd_save.startProgressDialog();

        Handler handler = new Handler();
        Runnable my_runnable = new Runnable() {
            @Override
            public void run() {
                pd_save.dismiss();
                if (interstitialAd1.isLoaded()) {
                    interstitialAd1.show();
                }


//                if(i==0){
//                    Toast.makeText(MapActivity.this,"There are no users nearby",Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Toast.makeText(MapActivity.this,i+" users found.",Toast.LENGTH_LONG).show();
//                }
                Toast.makeText(MapActivity.this,"Saved",Toast.LENGTH_SHORT).show();
            }
        };
        handler.postDelayed(my_runnable, 5000);
       // handler.removeCallbacks(my_runnable);


       // Toast.makeText(MapActivity.this,Current+"saved saved saved",Toast.LENGTH_LONG).show();
    }



    private void fetchLocation(String user) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
         //user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("useravailable");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(user);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if values exists
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    //if this child exists
//                                if(map.get("name")!=null){
//
//                                    mNameField1.setText(map.get("name").toString());
//                                }
//                                if(map.get("phone")!=null){
//
//                                    mPhoneField1.setText(map.get("phone").toString());
//                                }
                    if(map.get("carname")!=null){
                        CarName = map.get("carname").toString();
                    }
                    if(map.get("carnumber")!=null){
                        CarNumber = map.get("carnumber").toString();
                    }
                                if(map.get("profileImageUrl")!=null){
                                     ProfileimgUrl = map.get("profileImageUrl").toString();

                                  //  Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(image);
                                }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        GeoFire geoFire = new GeoFire(refAvailable);
        geoFire.getLocation(user, new com.firebase.geofire.LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                  //  Toast.makeText(MapActivity.this,""+location,Toast.LENGTH_SHORT).show();

                  // mtextView2.setText(location.latitude+":"+location.longitude);
                   // mtextView2.setText(location.latitude+":"+location.longitude);
                   // inArea();
                   // mMap.addCircle(new CircleOptions().center(new LatLng(location.latitude, location.longitude)).radius(500).strokeColor(Color.RED).fillColor(0x00000000).strokeWidth(5.0f));
                    showMarkers(location.latitude,location.longitude,CarName,CarNumber,ProfileimgUrl);




                } else {
                    Toast.makeText(MapActivity.this,"Location not found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

//    private void getCurrentLocation() {
//        mLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                if (task.isSuccessful()) {
//                    Location location = task.getResult();
//                    gotoLocation(location.getLatitude(), location.getLongitude());
//                } else {
//                    Log.d("Location", "getCurrentLocation: Error: " + task.getException().getMessage());
//                }
//            }
//        });
//
//    }



    private  void geolocate(View view){
hideSoftKeyboard(view);
 locationName = mSearchAddress.getText().toString();
     Geocoder geocoder = new Geocoder(this, Locale.getDefault());
     try {
        List<Address> addressList = geocoder.getFromLocationName(locationName,1);
         if (addressList.size() > 0) {
             Address address = addressList.get(0);

             gotoLocation(address.getLatitude(), address.getLongitude());

             showMarker(address.getLatitude(), address.getLongitude(),"Me");


             Toast.makeText(this, address.getLocality(), Toast.LENGTH_SHORT).show();

             Log.d("Location", "geoLocate: Locality: " + address.getLocality());
         }
     } catch (IOException e) {
        // e.printStackTrace();
     }
 }
 private void hideSoftKeyboard(View view){

     //hiding soft keyboard
     InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
     imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
    private void showMarker(double lat, double lng,String title) {
        if(marker!=null  ){
            marker.remove();
        }
        if(circle!=null){
            circle.remove();
        }
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lng));
        //markerOptions.title(String.valueOf(lat)+"/"+String.valueOf(lng));
        markerOptions.title(title);
       // markerOptions.snippet("snippet");
       // markerOptions.icon(bitmapDescriptorFromVector(this,R.drawable.ic_person_pin_circle_black_24dp));
        marker= mMap.addMarker(markerOptions);
        circle=mMap.addCircle(new CircleOptions().center(new LatLng(lat,lng)).radius(500).strokeColor(Color.RED).fillColor(0x00000000).strokeWidth(5.0f));
        radius = circle.getRadius();
        center = circle.getCenter();

    }
    private void showMarkers(double lat, double lng,String a,String b,String c) {

        hashMapMarker = new HashMap<>();
        LatLng latlng = new LatLng(lat,lng);
        Location loc1 = new Location("");
        loc1.setLatitude(latlng.latitude);
        loc1.setLongitude(latlng.longitude);
       Location  loc2 = new Location("");
        loc2.setLatitude(center.latitude);
        loc2.setLongitude(center.longitude);
         distance1 = loc2.distanceTo(loc1);
         value = c;
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lng));
       // markerOptions.title(String.valueOf(lat)+"/"+String.valueOf(lng));
        markerOptions.title(a);
        markerOptions.snippet("CarNumber:"+b);
        markerOptions.icon(bitmapDescriptorFromVector(this,R.drawable.ic_person_pin));
        markers= mMap.addMarker(markerOptions);


        if(distance1>radius){
           // markerss.add(markers);
            hashMapMarker.put("1",markers);
            Marker mm = hashMapMarker.get("1");
            mm.remove();
            hashMapMarker.remove("1");
        }
        else  i++;
//        for (Marker m : markerss) {
//
//            m.remove();
//            markerss.remove(m);
//
//        }
//        mtextView2.setText(String.valueOf(distance1));
    }
    private void gotoLocation(double lat,double lng){

        LatLng latLng=new LatLng(lat,lng);

        CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);
        mMap.animateCamera(cameraUpdate);
       // mMap.moveCamera(cameraUpdate);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }
    private void getLocationUpdates() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);  // 1000*10 = 10 seconds
        locationRequest.setFastestInterval(2000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mHandlerThread = new HandlerThread("LocationCallbackThread");
        mHandlerThread.start();
        //mHandlerThread.setPriority(Thread.MAX_PRIORITY);

        mLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, mHandlerThread.getLooper());





    }

    private void getLocation() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);  // 1000*10 = 10 seconds
        locationRequest.setFastestInterval(2000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if(!isLoggingOut && !isDeletingAccount){
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("useravailable");
            GeoFire geoFire = new GeoFire(refAvailable);
            geoFire.setLocation(userId,new GeoLocation(location.getLatitude(),location.getLongitude()));}
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //check for permissions
        //this if means what version the user's phone is
        //mMap.setMyLocationEnabled(true);

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //if the permission is already granted
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if(isGPSEnabled()) {
                    Toast.makeText(this, "Ready to go!", Toast.LENGTH_SHORT).show();
                    // to show map after getting permissions
//                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
//                            .findFragmentById(R.id.map);
//
//                    supportMapFragment.getMapAsync(this);
                }
            }else{
                checkLocationPermission();
            }
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(final Marker mark) {

                mark.showInfoWindow();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mark.showInfoWindow();

                    }
                }, 200);

                return true;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng c=  marker.getPosition();

               // Toast.makeText(MapActivity.this,""+s,Toast.LENGTH_SHORT).show();

//                for(Marker m : markerList){
//                    marker = m;
//                    String n = marker.getTitle();
//                    LatLng c=  marker.getPosition();
//                    if( n.equals("abcdefg"))
//                        Toast.makeText(MapActivity.this,""+c,Toast.LENGTH_SHORT).show();
//
//                }



                if(c.latitude==location.getLatitude() || c.longitude==location.getLongitude()){
                    Toast.makeText(MapActivity.this,"This is you",Toast.LENGTH_SHORT).show();

                }else{
                    String n = marker.getSnippet();
                    String s = n.substring(10).replaceAll("\\s", "").toLowerCase();
                    //value2 = s;
                Intent intent = new Intent(MapActivity.this,OtherUser.class);
                intent.putExtra("message2", s);
                startActivity(intent);
                finish();}
//            }else{
//                  // String uri = String.format(Locale.ENGLISH, "geo:%f,%f",37.8087,122.4098);
//                  // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                  // Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                          // Uri.parse("geo:0,0?q="+c.latitude+","+c.longitude+"(" + "Your Car" + ")"));
//                   Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                           Uri.parse("google.navigation:q="+c.latitude+","+c.longitude+"&mode=w"));
//                  // startActivity(intent);
//                   MapActivity.this.startActivity(intent);
//                   Toast.makeText(MapActivity.this,"This is you!!",Toast.LENGTH_SHORT).show();
//               }
            }
        });


    }

    private void checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                new AlertDialog.Builder(this)
//                        .setTitle("give permission")
//                        .setMessage("give permission message")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                            }
//                        })
//                        .create()
//                        .show();
//            }
                //requesting permissions
                ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
    }
// callback method for method .requestPermissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:{ //if permission granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//                    mMap.setMyLocationEnabled(true);
                    if(isGPSEnabled()){
                    Toast.makeText(getApplicationContext(),"permission granted",Toast.LENGTH_SHORT).show();
                } }else{
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
            } break;
        }
    }
    private boolean isGPSEnabled() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
  //using provider taki endles loop mein na phasse jab user on hi na kare gps aur app baar baar puchti rehe
        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (providerEnabled) {
            return true;
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permission")
                    .setMessage("GPS is required for this app to work. Please enable GPS.")
                    .setPositiveButton("OK", ((dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, GPS_REQUEST_CODE);
                    }))
                    .setCancelable(false)
                    .show();

        }

        return false;
    }
    //startActivityForResult callback function
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_REQUEST_CODE) {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (providerEnabled) {
                Toast.makeText(this, "GPS is enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "GPS not enabled. Unable to show user location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void remove_id_location(){
        isLoggingOut =true;
        String  userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("useravailable");


        GeoFire geoFire = new GeoFire(refAvailable);
        geoFire.removeLocation(userId);
    }
    private void remove_id(){
        isDeletingAccount = true;
        String  userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference refAvailable1 = FirebaseDatabase.getInstance().getReference("Users");
        refAvailable1.child(userId).removeValue();
        user.delete();
        DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("useravailable");
        GeoFire geoFire = new GeoFire(refAvailable);
        geoFire.removeLocation(userId);

    }


    private void inArea(){
         coords = new ArrayList<>();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
       // listener = this;
        FirebaseDatabase.getInstance().getReference("useravailable").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // List<MyLatLng> latlnglist = new ArrayList<>();
                ArrayList<String> multiusers= new ArrayList<>();
                for(DataSnapshot locationSnapshot: dataSnapshot.getChildren()){

                       // MyLatLng latlng = locationSnapshot.getValue(MyLatLng.class);
                       // latlnglist.add(latlng);
                    String mu = locationSnapshot.getKey();
                     multiusers.add(mu);
                }
               // coords = latlnglist;
                Multi = new ArrayList<>();
                Multi = multiusers;
                //Toast.makeText(MapActivity.this,""+Multi,Toast.LENGTH_LONG).show();
                 //builder = new StringBuilder();
                for(String differentUsers : Multi){
                     if(differentUsers.equals(userId)){
                         continue;
                         }
                    fetchLocation(differentUsers);

                    // builder.append(differentUsers+"\n");
                    //mtextView2.setText(differentUsers);
                   // Toast.makeText(MapActivity.this,""+differentUsers,Toast.LENGTH_LONG).show();
                   //fetchLocation();
                   // mMap.addCircle(new CircleOptions().center(new LatLng(lat, lng)).radius(500).strokeColor(Color.RED).fillColor(0x00000000).strokeWidth(5.0f));
                }//mtextView2.setText( builder);
               //listener.OnLoadLocationSuccess(multiusers);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //listener.OnLoadLocationFailure(databaseError.getMessage());
            }
        });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        if (netInfo == null) {
            // There are no active networks.
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
                MenuItem item = menu.findItem(R.id.search);

             //  SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
              SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint("Enter Car number");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if(query.matches("")){
                            Toast.makeText(MapActivity.this,"Please enter car number to search for",Toast.LENGTH_SHORT).show();
                        }else {

                            //value1 = query;
                                Intent intent = new Intent(MapActivity.this,OtherUser.class);
                                intent.putExtra("message1", query);
                                startActivity(intent);
                                finish();



                        }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                       // firebaseSearch(newText);
                       // locationName = mSearchAddress.getText().toString();


                        return false;
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.delete:
                remove_id();
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(MapActivity.this,MainActivity.class);
                startActivity(intent2);

                return true;
            case R.id.logout:
//                SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("remember","false");
//                editor.apply();
//                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DatabaseReference refremove = FirebaseDatabase.getInstance().getReference(userId);
//                refremove.removeValue();
                remove_id_location();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MapActivity.this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.info:
                Intent intent1 = new Intent(MapActivity.this,User_info.class);
                startActivity(intent1);
                return true;
            case R.id.contact_us:
                Intent intent4 = new Intent(MapActivity.this,ContactUs.class);
                startActivity(intent4);
                return true;
            case R.id.find:
                Intent intent5 = new Intent(MapActivity.this,FindCar.class);
                startActivity(intent5);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight() );
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
protected void onDestroy() {
    super.onDestroy();
    if (mHandlerThread != null) {
        mHandlerThread.quit();
        if(!isLoggingOut){
           // remove_id();
        }

    }

}

    @Override
    protected void onStop() {
        super.onStop();
        if(!isLoggingOut){
          // remove_id();
        }


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseRecyclerOptions<DataModel>options = new FirebaseRecyclerOptions.Builder<DataModel>()
//                .setQuery(MRef,DataModel.class)
//                .build();
//        FirebaseRecyclerAdapter<DataModel,Holder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataModel, Holder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull DataModel model) {
//                holder.setView(getApplicationContext(),model.getCarname(),model.getCarnumber(),model.getName(),model.getPhone(),model.getProfileImageUrl());
//
//            }
//
//            @NonNull
//            @Override
//            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_info_window,parent,false);
//                Holder viewHolder = new Holder(view);
//                return viewHolder;
//            }
//        };
//    }
    //    @Override
//    public void OnLoadLocationSuccess(ArrayList<String> MultiUser) {
//        Multi = MultiUser;
//        //Toast.makeText(MapActivity.this,""+Multi,Toast.LENGTH_LONG).show();
//
//    }
//
//    @Override
//    public void OnLoadLocationFailure(String message) {
//          Toast.makeText(MapActivity.this,""+message,Toast.LENGTH_SHORT).show();
//    }
}
