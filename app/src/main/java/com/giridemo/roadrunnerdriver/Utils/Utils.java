package com.giridemo.roadrunnerdriver.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static Boolean checkLocation(Context context){
        Boolean checklocation;
        checklocation = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return checklocation;
    }

    public static void showToast(Context context ,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    public static String base64Encrption(String password)

    {
        byte[] bytes = new byte[0];
        try {
             bytes = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    public static String decode64 (String password)
    {
        String text="";
        byte[] data = Base64.decode(password, Base64.DEFAULT);
        try {
             text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
