package com.giridemo.roadrunnerdriver.broadcast_reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.giridemo.roadrunnerdriver.services.NotificationService;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG=NotificationReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: Serrvice has stoped");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            context.startService(new Intent(context, NotificationService.class));
            Log.i(TAG, "onReceive: brodcast receiver is started after reboot");
        }else {
            context.startService(new Intent(context, NotificationService.class));
            Log.i(TAG, "onReceive: service has started");
        }
    }
}
