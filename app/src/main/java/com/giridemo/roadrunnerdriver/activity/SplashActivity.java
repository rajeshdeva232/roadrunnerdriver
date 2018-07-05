package com.giridemo.roadrunnerdriver.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.Utils.Constants;
import com.giridemo.roadrunnerdriver.Utils.SharedPrefrenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends Activity {

    SharedPrefrenceHelper sharedPrefrenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPrefrenceHelper=new SharedPrefrenceHelper(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    permissioncheck();

                } else {
                    // Pre-Marshmallow
                    lanchApp();
                }

            }
        }, Constants.SPASHSLEEP);
    }


    private void lanchApp() {
        Intent intent = null;
        if(sharedPrefrenceHelper.getBoolean(Constants.LOGINSTATUS)) {
            if (MainActivity.orderId == null) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, OderActivity.class);
            }
        }else{
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
        startActivity(intent);
    }



    private void permissioncheck() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();

       if(!addPermission(permissionsList,Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("ACCESS_COARSE_LOCATION");
        if(!addPermission(permissionsList,Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("ACCESS_FINE_LOCATION");


        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                StringBuilder message = new StringBuilder("You need to grant access to " + permissionsNeeded.get(0));
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message.append(", ").append(permissionsNeeded.get(i));
                showMessageOKCancel(message.toString(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Build.VERSION.SDK_INT >= 23) {
                                    // Marshmallow+
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                           Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);


                                } else {
                                    // Pre-Marshmallow
                                }

                            }
                        });
                return;
            }

            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);


            } else {
                // Pre-Marshmallow

            }

        } else {
            // Toast.makeText(this,"Permission",Toast.LENGTH_LONG).show();
            lanchApp();
        }

        //insertDummyContact();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {

        Boolean cond;
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    //  return false;

                    cond = false;
            }
            //  return true;

            cond = true;


        } else {
            // Pre-Marshmallow
            cond = true;
        }

        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == 23) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Permission Needed To Run The App", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
            // Initial
            perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);

            if (perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lanchApp();

            } else {
                // Permission Denied
                Toast.makeText(SplashActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                        finish();
                    }
                }, Constants.SPASHSLEEP);
            }

        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
