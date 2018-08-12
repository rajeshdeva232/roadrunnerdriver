package com.giridemo.roadrunnerdriver.services;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.giridemo.roadrunnerdriver.Utils.Constants;
import com.giridemo.roadrunnerdriver.Utils.SharedPrefrenceHelper;
import com.giridemo.roadrunnerdriver.Utils.Utils;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GPSTracker extends Service implements LocationListener {

    private Context context;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private static final String TAG=GPSTracker.class.getSimpleName();

    Location location;
    double latitude, longitude;

    LocationManager locationManager;
//    AlertDialogManager am = new AlertDialogManager();

//    public GPSTracker(Context context) {
//        this.context = context;
//        getLocation();
//    }


    @Override
    public void onCreate() {
        getLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    private Location getLocation() {
        // TODO Auto-generated method stub
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            assert locationManager != null;
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
//                        return TODO;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.SPASHSLEEP, 2, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
//                            return TODO;
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.SPASHSLEEP, 2, this);
                        if (locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                } else {
//                    showAlertDialog();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return location;
    }
//    public void showSettingsAlert(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GPSTracker.this);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("GPS is settings");
//
//        // Setting Dialog Message
//        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
//
//        // Setting Icon to Dialog
//        //alertDialog.setIcon(R.drawable.delete);
//
//        // On pressing Settings button
//        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog,int which) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//                dialog.cancel();
//            }
//        });
//
//        // on pressing cancel button
//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }

    public void showAlertDialog(){
//        am.showAlertDialog(GPSTracker.this, "GPS Setting", "Gps is not enabled. Do you want to enabled it ?", false);
    }
    public double getLatitude(){
        if (location != null){
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude(){
        if (location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }
    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if (location != null){
            this.location = location;
            Log.i(TAG, "onLocationChanged: working==>"+location);
            SharedPrefrenceHelper sharedPrefrenceHelper=new SharedPrefrenceHelper(getApplicationContext());
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Driver Location");
            GeoFire geoFire = new GeoFire(ref);
            geoFire.setLocation(sharedPrefrenceHelper.getString(Constants.DRIVERNAME), new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                        Utils.showToast(getApplicationContext(),"Error while update location");
                    } else {
                        System.out.println("Location saved on server successfully!");
                    }
                }

            });

        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
