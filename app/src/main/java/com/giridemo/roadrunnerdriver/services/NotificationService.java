package com.giridemo.roadrunnerdriver.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.activity.OderActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NotificationService extends Service {
    private static final String TAG=NotificationService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(){
//        SharedPrefrenceHelper sharedPrefrenceHelper = new SharedPrefrenceHelper(getApplicationContext());
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        Query getUserordered=databaseReference.child("PlaceOrder").orderByChild("deliveryStatus").equalTo(0);
        getUserordered.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot placedOrder:dataSnapshot.getChildren()){
                        Log.i(TAG, "onDataChange: working==>"+placedOrder.child("deliveryStatus").getValue());
                        if(Integer.parseInt(String.valueOf(placedOrder.child("deliveryStatus").getValue()))==0 && Integer.parseInt(String.valueOf(placedOrder.child("drivernotified").getValue()))==1){
                            databaseReference.child("PlaceOrder").child(String.valueOf(placedOrder.child("key").getValue())).child("drivernotified").setValue(0);
                            String username=String.valueOf(placedOrder.child("username").getValue());
                            Log.i(TAG, "onDataChange: username==>"+username);
                            setNotification("New Order","New Order was given by "+username,String.valueOf(String.valueOf(placedOrder.child("key").getValue())));
                        }
//                        if(Integer.parseInt(String.valueOf(placedOrder.child("deliveryStatus").getValue()))==2 && Integer.parseInt(String.valueOf(placedOrder.child("notifiedUser").getValue()))==1){
//                            databaseReference.child("PlaceOrder").child(String.valueOf(placedOrder.child("key").getValue())).child("notifiedUser").setValue(0);
//                            setNotification("Order Picked","Your Order is picked by driver",String.valueOf(String.valueOf(placedOrder.child("key").getValue())));
//                        }
//                        if(Integer.parseInt(String.valueOf(placedOrder.child("deliveryStatus").getValue()))==-1 && Integer.parseInt(String.valueOf(placedOrder.child("notifiedUser").getValue()))==1){
//                            databaseReference.child("PlaceOrder").child(String.valueOf(placedOrder.child("key").getValue())).child("notifiedUser").setValue(0);
//                            setNotification("Order Rejected","Your Order hasbeen Rejected by Admin",String.valueOf(String.valueOf(placedOrder.child("key").getValue())));
//                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        Query picked=databaseReference.child("PlaceOrder").orderByChild("deliveryStatus").equalTo(2);
//        picked.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    for(DataSnapshot placedOrder:dataSnapshot.getChildren()){
//                        Log.i(TAG, "onDataChange: working==>"+placedOrder.child("deliveryStatus").getValue());
//                        if(Integer.parseInt(String.valueOf(placedOrder.child("deliveryStatus").getValue()))==2 && Integer.parseInt(String.valueOf(placedOrder.child("adminnotified").getValue()))==1){
//                            databaseReference.child("PlaceOrder").child(String.valueOf(placedOrder.child("key").getValue())).child("adminnotified").setValue(0);
//                            String username=String.valueOf(placedOrder.child("username").getValue());
//                            Log.i(TAG, "onDataChange: username==>"+username);
//                            setNotification("Order picked","Order was picked by "+username,String.valueOf(String.valueOf(placedOrder.child("key").getValue())));
//                        }
////                        if(Integer.parseInt(String.valueOf(placedOrder.child("deliveryStatus").getValue()))==2 && Integer.parseInt(String.valueOf(placedOrder.child("notifiedUser").getValue()))==1){
////                            databaseReference.child("PlaceOrder").child(String.valueOf(placedOrder.child("key").getValue())).child("notifiedUser").setValue(0);
////                            setNotification("Order Picked","Your Order is picked by driver",String.valueOf(String.valueOf(placedOrder.child("key").getValue())));
////                        }
////                        if(Integer.parseInt(String.valueOf(placedOrder.child("deliveryStatus").getValue()))==-1 && Integer.parseInt(String.valueOf(placedOrder.child("notifiedUser").getValue()))==1){
////                            databaseReference.child("PlaceOrder").child(String.valueOf(placedOrder.child("key").getValue())).child("notifiedUser").setValue(0);
////                            setNotification("Order Rejected","Your Order hasbeen Rejected by Admin",String.valueOf(String.valueOf(placedOrder.child("key").getValue())));
////                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void setNotification(String title,String content,String key){
        Intent notificationIntent = new Intent(getApplicationContext(), OderActivity.class);

//**add this line**
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

//**edit this line to put requestID as requestCode**
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(),key);
        notification.setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_cart)
                .setContentTitle(title)
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND )
                .setContentIntent(contentIntent);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(1, notification.build());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }
}
