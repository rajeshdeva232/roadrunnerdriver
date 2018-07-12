package com.giridemo.roadrunnerdriver.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.adapter.Order_Adaptor;
import com.giridemo.roadrunnerdriver.broadcast_reciver.NotificationReceiver;
import com.giridemo.roadrunnerdriver.model.GetItemlist;
import com.giridemo.roadrunnerdriver.model.OrderHistory;
import com.giridemo.roadrunnerdriver.services.NotificationService;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OderActivity extends AppCompatActivity  {

    String TAG ="OderActivity";
    ArrayList<OrderHistory> orderHistoryArrayList=new ArrayList<>();
    RecyclerView rvNeworder;
    TextView textView;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (!isMyServiceRunning(NotificationReceiver.class)) {
//            Log.i(TAG, "onCreate: started");
//            startService(new Intent(this, NotificationService.class));
//        }else{
//            Log.i(TAG, "onCreate: service is running");
//        }
//    }

//    @Override
//    protected void onStop() {
//        stopService(new Intent(this, NotificationService.class));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder);
        rvNeworder = findViewById(R.id.rvOrder);
        textView = findViewById(R.id.error);
        if (!isMyServiceRunning(NotificationReceiver.class)) {
            Log.i(TAG, "onCreate: started");
            startService(new Intent(this, NotificationService.class));
        }else{
            Log.i(TAG, "onCreate: service is running");
        }
        loadData();
    }

    private void loadData() {

        Query query;
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        query = databaseReference.child("PlaceOrder").orderByChild("deliveryStatus").equalTo(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderHistoryArrayList.clear();
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot history:dataSnapshot.getChildren())
                    {
                        OrderHistory orderHistory=new OrderHistory();
                    Log.i(TAG, "onDataChange: getAddress==> "+history.child("addresstoDelevery").getValue());
                    orderHistory.setAddresstoDelevery(String.valueOf(history.child("addresstoDelevery").getValue()));
                    orderHistory.setDelivermobileNo(String.valueOf(history.child("delivermobileNo").getValue()));
                    orderHistory.setTotalamount(String.valueOf(history.child("totalamount").getValue()));
                    orderHistory.setDelivername(String.valueOf(history.child("delivername").getValue()));
                    orderHistory.setLandmark(String.valueOf(history.child("landmark").getValue()));
                    orderHistory.setDeleveryStatus(Integer.parseInt(String.valueOf(history.child("deliveryStatus").getValue())));
                    orderHistory.setKey(String.valueOf(history.child("key").getValue()));
                    orderHistory.setTime(String.valueOf(history.child("orderedTime").getValue()));
                    orderHistory.setUsermobileno(String.valueOf(history.child("usermobileno").getValue()));
                    orderHistory.setUsername(String.valueOf(history.child("username").getValue()));
                    Log.i(TAG, "onDataChange: doorno==>"+history.child("deliverDoorNo").getValue());
                    orderHistory.setDoorNo(String.valueOf(history.child("deliverDoorNo").getValue()));
                    orderHistory.setDeleveryLatlng(
                            new LatLng(Double.parseDouble(String.valueOf(history.child("deliverlatlng").child("latitude").getValue()))
                                    ,Double.parseDouble(String.valueOf(history.child("deliverlatlng").child("longitude").getValue()))));
                    ArrayList<GetItemlist> getItemlistArrayList=new ArrayList<>();
                    for(DataSnapshot hotelName:history.child("Ordered items").getChildren()){
                        for(DataSnapshot itemName:hotelName.getChildren()) {
                            GetItemlist getItemlist = new GetItemlist();
                            getItemlist.setItem(itemName.getKey());
//                        Log.i(TAG, "onDataChange: Hotelname==>"+itemName.child("Hotelname").getValue());
                            getItemlist.setHotalName(hotelName.getKey());//itemName.child("Hotelname").getValue().toString()
//                        Log.i(TAG, "onDataChange: Amount==>"+itemName.child("Amount").getValue());
                            getItemlist.setAmount(Integer.parseInt(Objects.requireNonNull(itemName.child("Amount").getValue()).toString()));
                            getItemlist.setPrice_Per_Item(Integer.parseInt(Objects.requireNonNull(itemName.child("Price_Per_Item").getValue()).toString()));
//                        Log.i(TAG, "onDataChange: Quantity==>"+itemName.child("Quantity").getValue());
                            getItemlist.setQuantity(Integer.parseInt(Objects.requireNonNull(itemName.child("Quantity").getValue()).toString()));
                            getItemlist.setMinimumquantity(Integer.parseInt(Objects.requireNonNull(itemName.child("Minimum").getValue()).toString()));
                            getItemlist.setIsComplementryAvalible(Integer.parseInt(Objects.requireNonNull(itemName.child("IsComplementryAvalible").getValue()).toString()));
                            getItemlist.setNeed_Complementry(Integer.parseInt(Objects.requireNonNull(itemName.child("Need_Complementry").getValue()).toString()));
                            if (itemName.child("Complementry").exists()) {
                                Log.i(TAG, "onDataChange: work");
                                Map<String, Integer> complement = new HashMap<>();
                                for (DataSnapshot complimentry : itemName.child("Complementry").getChildren()) {
                                    Log.i(TAG, "onDataChange: complementry==>" + complimentry.getKey());
                                    complement.put(complimentry.getKey(), Integer.parseInt(Objects.requireNonNull(complimentry.getValue()).toString()));
                                    getItemlist.setComplement(complement);
                                }
                            }
                            getItemlistArrayList.add(getItemlist);
                            orderHistory.setGetItemlistArrayList(getItemlistArrayList);
                        }
                    }

                    orderHistoryArrayList.add(orderHistory);
                }
                Order_Adaptor orderAdaptor=new Order_Adaptor(getApplicationContext(),orderHistoryArrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rvNeworder.setLayoutManager(mLayoutManager);
                rvNeworder.setItemAnimator(new DefaultItemAnimator());
                rvNeworder.setAdapter(orderAdaptor);

            }else{
                rvNeworder.setAdapter(null);
                textView.setVisibility(View.VISIBLE);
            }


        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }
}
