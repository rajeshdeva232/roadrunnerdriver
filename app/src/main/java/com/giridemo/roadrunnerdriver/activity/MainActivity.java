package com.giridemo.roadrunnerdriver.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.Utils.SharedPrefrenceHelper;
import com.giridemo.roadrunnerdriver.Utils.Utils;
import com.giridemo.roadrunnerdriver.Utils.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.maps.*;

import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnCameraIdleListener,GoogleMap.OnCameraMoveListener,GoogleMap.OnMapLoadedCallback,GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    TextView text;
    Marker marker;
    private String TAG = "MainActivity";
    SharedPrefrenceHelper sharedPrefrenceHelper;
    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=findViewById(R.id.text);
        sharedPrefrenceHelper =new SharedPrefrenceHelper(getApplicationContext());
        orderId=sharedPrefrenceHelper.getString("OrderId");
        checkPermissionforma();

    }



    private void checkPermissionforma() {

        if (Utils.checkLocation(getApplicationContext())) {
            intMap();
        } else   {
            ActivityCompat.requestPermissions(Objects.requireNonNull(MainActivity.this),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.REQUEST_LOCATION);
        }
    }
    private void intMap() {
        SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fMap);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onCameraIdle() {
        
    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onMapLoaded() {
        getOrderDetailsFromFireBase();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (Utils.checkLocation(getApplicationContext())) {
            getLocatonInfo();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setOnCameraMoveListener(this);
            mMap.setOnCameraIdleListener(this);
            mMap.setOnMapLoadedCallback(this);
            mMap.setOnMarkerClickListener(this);

        }

    }

    private void getOrderDetailsFromFireBase() {
        final ArrayList<String> hotelNamelList = new ArrayList();
        System.out.println("get details from firebase "+orderId);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("PlaceOrder").child(orderId).child("Ordered items");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (final DataSnapshot hotalName : dataSnapshot.getChildren()) {
                        Log.i(TAG, "onDataChange: hotalName==>" + hotalName.getKey());
                        hotelNamelList.add(hotalName.getKey());
                    System.out.println("hotel name in map activity " + dataSnapshot);
                    final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Hotaldetails").child(hotalName.getKey()).child("LatLng");
                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                            if (dataSnapshot1.exists()) {

                                String lat = dataSnapshot1.child("Latitude").getValue().toString();
                                String longitude = dataSnapshot1.child("Longitude").getValue().toString();
                                System.out.println("lat and lang in main activity " + lat + " " + longitude);
                                addMarker(lat, longitude, hotalName.getKey());
                            } else {
                                Utils.showToast(getApplicationContext(), "No value in datasnapshot");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Utils.showToast(getApplicationContext(), "Getting error in responce");
                        }
                    });
                }
                }
                else
                {
                    Utils.showToast(getApplicationContext(),"No value in datasnapshot1");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addMarker(String lat , String lang ,String name) {
        System.out.println("lat and long in marker "+lat+"  "+lang);
        LatLng latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lang));
        Marker marker = mMap.addMarker(new MarkerOptions()
                 .position(latLng)
                 .title(name)
                 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        marker.setTag(name);
        marker.hideInfoWindow();


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void getLocatonInfo() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        try {

            if (Utils.checkLocation(getApplicationContext())) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentlocation = (Location) task.getResult();
                            if(currentlocation!=null) {
                                mMap.setMyLocationEnabled(true);
                                moveCamera(new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude()), Constants.DEFAULT_ZOOM);
                            }
                        } else {
                            Log.i(TAG, "onComplete: error while geting currentlocation==>" + task.getException());
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getLocatonInfo: security excaption==>", e.getCause());
        }
    }
    private void moveCamera(LatLng latLng, Float zoom) {
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                latLng, zoom);
        mMap.animateCamera(location);

    }

    @Override
    public boolean onMarkerClick(Marker marker1) {
        final ArrayList<String> items = new ArrayList<>();
            System.out.println("marker details "+marker1.getTag());
        System.out.println("marker details id"+orderId);
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("PlaceOrder").child(orderId).child("Ordered items").child(marker1.getTag().toString());
        items.clear();
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    System.out.println("ordered items "+dataSnapshot);
                     for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                     {
                         items.add(String.valueOf(dataSnapshot1.getKey()));
                     }

                    mthd1(items);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return false;
    }

    private void mthd1(ArrayList items) {
        String name = "";
        for (Object list: items)
        {
            name+= "\n"+String.valueOf(list);
        }
        System.out.println("array list value "+name);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.show_hotel_details, null);
        dialogBuilder.setView(dialogView);
        TextView editText =  dialogView.findViewById(R.id.details);
        editText.setText(name);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }
}
