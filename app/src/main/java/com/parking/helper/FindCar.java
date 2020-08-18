package com.parking.helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FindCar extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient mLocationClient;
    private LocationCallback mLocationCallback;
    Location location;
    private GoogleMap mMap;
    public static final int DEFAULT_ZOOM = 15;
    Marker marker;
    private Button mGo;
    private MaterialCardView mCard;
    InterstitialAd interstitialAd2;

    private TextView mFirst;
    private Typeface tfc1,tfc2;


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sh = getSharedPreferences("location" , MODE_PRIVATE);
        String cc = sh.getString("current_location","");
        //Toast.makeText(MapActivity.this,cc,Toast.LENGTH_LONG).show();



        if(!cc.isEmpty()){
             fetchCurrentLocation(cc);
            mFirst.setText("Here you GO!!");
            mFirst.setTextSize(20);
//            mCard.getLayoutParams();




        }else{
            mFirst.setText("First,Register your car location by clicking 'current location button ,located on the top right of the google map in the Main page.'");
        }
        //Toast.makeText(MapActivity.this,cc+"saved saved saved",Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.style_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_car);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);

        tfc1 = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-Regular.ttf");
        tfc2 = Typeface.createFromAsset(getAssets(),"fonts/Staatliches-Regular.ttf");

        mGo = findViewById(R.id.go);
        mGo.setTypeface(tfc2);
        mFirst = findViewById(R.id.first);
        mFirst.setTypeface(tfc1);
        mCard = findViewById(R.id.card);


        //mLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        mLocationCallback = new LocationCallback(){
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                //super.onLocationResult(locationResult);
//                if(locationResult==null){
//                    return;
//                }
//
//                location = locationResult.getLastLocation();
//
//
////                Toast.makeText(MapActivity.this, location.getLatitude() + " \n" +
////                        location.getLongitude(), Toast.LENGTH_SHORT).show();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        coordinates.setText(location.getLatitude() + ":" + location.getLongitude());
//                        // getCurrentLocation();
//                        gotoLocation(location.getLatitude(), location.getLongitude());
//
//                        showMarker(location.getLatitude(), location.getLongitude(),"Me");
//                        // mMap.addCircle(new CircleOptions().center(new LatLng(location.getLatitude(), location.getLongitude())).radius(500).strokeColor(Color.RED).fillColor(0x00000000).strokeWidth(5.0f));
//
//
//                        Log.d("Location", "inside runOnUiThread method: Thread name: " + Thread.currentThread().getName());
//
//                    }
//                });
//
//                Log.d("Location", "onLocationResult: " + location.getLatitude() + " \n" +
//                        location.getLongitude());
//                Log.d("Location", "onLocationResult: Thread name: " + Thread.currentThread().getName());
////                String location_string = "" + location.getLatitude() + "//" + location.getLongitude();
////               coordinates.setText(location_string);
//                // coordinates.setText(location.getLatitude()+":"+location.getLongitude());
////                if(!isLoggingOut && !isDeletingAccount){
////                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
////                    DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("useravailable");
////                    GeoFire geoFire = new GeoFire(refAvailable);
////                    geoFire.setLocation(userId,new GeoLocation(location.getLatitude(),location.getLongitude()));}
////                FirebaseDatabase.getInstance().getReference("useravailable").child("userId").child(userId).addValueEventListener(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////
////
////
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                    }
////                });
//
//
//
//            }
//        };

        interstitialAd2 = new InterstitialAd(this);
        interstitialAd2.setAdUnitId(getString(R.string.interstitial_ad2));
        interstitialAd2.loadAd(new AdRequest.Builder().build());
        interstitialAd2.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                interstitialAd2.loadAd(new AdRequest.Builder().build());
                Intent intent = new Intent(FindCar.this,MapActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.d("ADd", "Error: " + adError.getMessage());
//                Intent intent = new Intent(FindCar.this,MapActivity.class);
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

        mGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd2.isLoaded()) {
                    interstitialAd2.show();
                }else{
                    Intent intent = new Intent(FindCar.this,MapActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }


    private void gotoLocation(double lat,double lng){

        LatLng latLng=new LatLng(lat,lng);

        CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);
        mMap.animateCamera(cameraUpdate);
        // mMap.moveCamera(cameraUpdate);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    private void showMarker(double lat, double lng,String title) {
        if(marker!=null  ){
            marker.remove();
        }
//        if(circle!=null){
//            circle.remove();
//        }
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(FindCar.this));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lng));
        //markerOptions.title(String.valueOf(lat)+"/"+String.valueOf(lng));
        markerOptions.title(title);
        // markerOptions.snippet("snippet");
        // markerOptions.icon(bitmapDescriptorFromVector(this,R.drawable.ic_person_pin_circle_black_24dp));
        marker= mMap.addMarker(markerOptions);
        //circle=mMap.addCircle(new CircleOptions().center(new LatLng(lat,lng)).radius(500).strokeColor(Color.RED).fillColor(0x00000000).strokeWidth(5.0f));
        //radius = circle.getRadius();
        //center = circle.getCenter();

    }

    private void fetchCurrentLocation(String l){
        DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("useravailable");
        GeoFire geoFire = new GeoFire(refAvailable);
        geoFire.getLocation(l, new com.firebase.geofire.LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    //  Toast.makeText(MapActivity.this,""+location,Toast.LENGTH_SHORT).show();

                    // mtextView2.setText(location.latitude+":"+location.longitude);
                    // mtextView2.setText(location.latitude+":"+location.longitude);
                    // inArea();
                    // mMap.addCircle(new CircleOptions().center(new LatLng(location.latitude, location.longitude)).radius(500).strokeColor(Color.RED).fillColor(0x00000000).strokeWidth(5.0f));
                    showMarker(location.latitude, location.longitude,"Click me,to get directions for your car");
                    //showMarkers(location.latitude,location.longitude,CarName,CarNumber,ProfileimgUrl);

                    gotoLocation(location.latitude, location.longitude);




                } else {
                    Toast.makeText(FindCar.this,"Location not found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

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


                if(location != null){
                    if(c.latitude==location.getLatitude() || c.longitude==location.getLongitude()){
                        Toast.makeText(FindCar.this,"This is you",Toast.LENGTH_SHORT).show();

                    }
                }else{
                    // String uri = String.format(Locale.ENGLISH, "geo:%f,%f",37.8087,122.4098);
                    // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    // Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    // Uri.parse("geo:0,0?q="+c.latitude+","+c.longitude+"(" + "Your Car" + ")"));
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q="+c.latitude+","+c.longitude+"&mode=w"));
                    // startActivity(intent);
                    FindCar.this.startActivity(intent);
                    Toast.makeText(FindCar.this,"This is you!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}