package com.giridemo.roadrunnerdriver.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.Utils.SharedPrefrenceHelper;
import com.giridemo.roadrunnerdriver.Utils.Utils;
import com.giridemo.roadrunnerdriver.Utils.Constants;
import com.giridemo.roadrunnerdriver.dialogfragment.OrderedItems;
import com.giridemo.roadrunnerdriver.dialogfragment.Userdetails;
import com.giridemo.roadrunnerdriver.services.NotificationService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.maps.*;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnCameraIdleListener,GoogleMap.OnCameraMoveListener,GoogleMap.OnMapLoadedCallback,GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    TextView text,tvEndTrip;
    Marker marker;
    private static final String TAG = "MainActivity";
    SharedPrefrenceHelper sharedPrefrenceHelper;
    public static String orderId;
    private ImageView ivExpand;
    private ExpandableLayout ltExpand;
    Bundle costomerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stopService(new Intent(this, NotificationService.class));
        text=findViewById(R.id.text);
        ivExpand=findViewById(R.id.ivExpand);
        ltExpand=findViewById(R.id.ltExpand);
        tvEndTrip=findViewById(R.id.tvEndTrip);
        sharedPrefrenceHelper =new SharedPrefrenceHelper(getApplicationContext());
        orderId=sharedPrefrenceHelper.getString(Constants.ORDERID);
        checkPermissionforma();
        performAction();
    }

    private void performAction() {
        ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ltExpand.isExpanded()){
                    ivExpand.setImageDrawable(getDrawable(R.drawable.ic_dropdown));
                    ltExpand.collapse(true);
                }else {
                    ivExpand.setImageDrawable(getDrawable(R.drawable.ic_dropup));
                    ltExpand.expand(true);
                }
            }
        });

        tvEndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder alertDialog  = new android.app.AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Delivery")
                .setMessage("Are Sure You Have delivered the item?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleveredItem();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
    }

    private void deleveredItem() {

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("PlaceOrder");
        HashMap<String,Object> deleveryDetails=new HashMap<>();
        deleveryDetails.put("deliveryStatus",3);
        deleveryDetails.put("notifiedUser",1);
        deleveryDetails.put("adminnotified",1);
        deleveryDetails.put("deleveredAt",Utils.getCurrentTimeStamp());
        databaseReference.child(orderId).updateChildren(deleveryDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                orderId=null;
                sharedPrefrenceHelper.saveString(Constants.ORDERID,null);
                Utils.showToast(getApplicationContext(),"Sucessfully delevered");
                startActivity(new Intent(MainActivity.this,OderActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utils.showToast(getApplicationContext(),"Error while updateing ==>"+e);
            }
        });

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
        getLocatonInfo();
        getOrderDetailsFromFireBase();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (Utils.checkLocation(getApplicationContext())) {
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
//        final ArrayList<String> hotelNamelList = new ArrayList();
        System.out.println("get details from firebase "+orderId);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("PlaceOrder").child(orderId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    getCostomerdetails(dataSnapshot);

                    for (final DataSnapshot hotalName : dataSnapshot.child("Ordered items").getChildren()) {
                        Log.i(TAG, "onDataChange: hotalName==>" + hotalName.getKey());
//                        hotelNamelList.add(hotalName.getKey());
                    Log.i(TAG,"hotel name in map activity " + dataSnapshot);
                    final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Hotaldetails").child(hotalName.getKey()).child("LatLng");
                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                            if (dataSnapshot1.exists()) {

                                String lat = String.valueOf(dataSnapshot1.child("Latitude").getValue());
                                String longitude = String.valueOf(dataSnapshot1.child("Longitude").getValue());
                                Log.i(TAG, "onDataChange: lat and lang in main activity "+ lat + " " + longitude);
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

    private void getCostomerdetails(DataSnapshot dataSnapshot) {
        TextView tvAddress=findViewById(R.id.tvAddress);
        TextView tvTotal=findViewById(R.id.tvTotal);
        TextView tvDeleverycharge=findViewById(R.id.tvDeleverycharge);
//        TextView tvLandmark=findViewById(R.id.tvLandmark);
//        TextView tvDoorno=findViewById(R.id.tvDoorno);
//        tvMobileno.setText(String.valueOf(dataSnapshot.child("delivermobileNo").getValue()));
//        tvName.setText(String.valueOf(dataSnapshot.child("delivername").getValue()));
        tvAddress.setText(String.valueOf(dataSnapshot.child("addresstoDelevery").getValue()));
        tvTotal.setText(String.valueOf(dataSnapshot.child("totalamount").getValue()));
        tvDeleverycharge.setText(String.valueOf(dataSnapshot.child("deliverycharge").getValue()));
//        if(String.valueOf(dataSnapshot.child("landmark").getValue()).equals("NO_LANDMARK_PROVIDED")){
//            tvLandmark.setText(String.valueOf(dataSnapshot.child("landmark").getValue()));
//        }else {
//            tvLandmark.setVisibility(View.GONE);
//        }
//        if(String.valueOf(dataSnapshot.child("deliverDoorNo").getValue()).equals("NO DOORNO PROVIDED")){
//            tvDoorno.setText(String.valueOf(dataSnapshot.child("deliverDoorNo").getValue()));
//        }else {
//            tvDoorno.setVisibility(View.GONE);
//        }
        costomerDetails = new Bundle();
        costomerDetails.putString(Constants.COSTOMERNAME,String.valueOf(dataSnapshot.child("delivername").getValue()));
        costomerDetails.putString(Constants.COSTOMERMOBILENO,String.valueOf(dataSnapshot.child("delivermobileNo").getValue()));
        costomerDetails.putString(Constants.COSTOMERDOORNO,String.valueOf(dataSnapshot.child("deliverDoorNo").getValue()));
        costomerDetails.putString(Constants.COSTOMERLANDMARK,String.valueOf(dataSnapshot.child("landmark").getValue()));
        costomerDetails.putString(Constants.ORDEREDBYNMAE,String.valueOf(dataSnapshot.child("username").getValue()));
        costomerDetails.putString(Constants.ORDEREDBYNO,String.valueOf(dataSnapshot.child("usermobileno").getValue()));

//        LatLng costumerLocation=new LatLng(Double.parseDouble(String.valueOf(dataSnapshot.child("deliverlatlng").child("latitude").getValue()))
//                ,Double.parseDouble(String.valueOf(Double.parseDouble(String.valueOf(dataSnapshot.child("deliverlatlng").child("longitude").getValue())))));
        addMarkertoDelevery(String.valueOf(dataSnapshot.child("deliverlatlng").child("latitude").getValue()),String.valueOf(dataSnapshot.child("deliverlatlng").child("longitude").getValue()));

    }

    private void addMarker(String lat , String lang ,String name) {
        Log.i(TAG,"lat and long in marker "+lat+"  "+lang);
        LatLng latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lang));
        Marker marker = mMap.addMarker(new MarkerOptions()
                 .position(latLng)
                 .title(name)
                 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        marker.setTag(name);
        marker.showInfoWindow();


    }

    private void addMarkertoDelevery(String lat, String lang){
        LatLng latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lang));
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Delivery Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        marker.setTag("Delivery Location");
        marker.showInfoWindow();
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
                            Log.e(TAG, "onComplete: error while geting currentlocation==>" + task.getException());
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
//        final ArrayList<String> items = new ArrayList<>();
//            System.out.println("marker details "+marker1.getTag());
//        System.out.println("marker details id"+orderId);
        if(String.valueOf(marker1.getTag()).equals("Delivery Location")){
            Userdetails userdetails=new Userdetails();
            userdetails.setCancelable(false);
            userdetails.setArguments(costomerDetails);
            userdetails.show(getSupportFragmentManager(), "costomerDetails");

        }else {
            OrderedItems orderedItems = new OrderedItems();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.SELECTEDHOTELNAME, String.valueOf(marker1.getTag()));
            bundle.putString(Constants.ORDERID, orderId);
            orderedItems.setArguments(bundle);
            orderedItems.show(getSupportFragmentManager(), "MyCustomDialog");
        }
//        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("PlaceOrder").child(orderId).child("Ordered items").child(String.valueOf(marker1.getTag()));
//        items.clear();
//        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists())
//                {
//                    System.out.println("ordered items "+dataSnapshot);
//                     for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
//                     {
//                         items.add(String.valueOf(dataSnapshot1.getKey()));
//                     }
//
//                    mthd1(items);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



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
