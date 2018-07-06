package com.giridemo.roadrunnerdriver.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.Utils.Constants;
import com.giridemo.roadrunnerdriver.Utils.SharedPrefrenceHelper;
import com.giridemo.roadrunnerdriver.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

EditText etName , etmobile , etPassword;
ImageView ivEdit;
Button btnSignout;
SharedPrefrenceHelper sharedPrefrenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.giridemo.roadrunnerdriver.R.layout.activity_profile);
        sharedPrefrenceHelper = new SharedPrefrenceHelper(getApplicationContext());
        etName = findViewById(R.id.etName);
        etmobile = findViewById(R.id.mobile);
        etPassword = findViewById(R.id.etPassword);
        ivEdit = findViewById(R.id.ivEdit);
        btnSignout =findViewById(R.id.btnSignout);
        etName.setClickable(false);
        etmobile.setClickable(false);
        etPassword.setClickable(false);
        performAction();
        getDetails();

    }

    private void getDetails() {
        final String driverName = sharedPrefrenceHelper.getString(Constants.DRIVERNAME);
        System.out.println("Name in profile page "+driverName);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriverData").child(driverName);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    final String mobile = String.valueOf(dataSnapshot.child("mobileno").getValue());
                    String password = String.valueOf(dataSnapshot.child("password").getValue());
                    final String password1 = Utils.decode64(password);
                    System.out.println("driver details in firebase "+mobile +"   "+password+"  "+password1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            etName.setText(driverName);
                            etmobile.setText(mobile);
                            etPassword.setText(password1);
                        }
                    });
                }
                else
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = false;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    private void performAction() {

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   sharedPrefrenceHelper.saveSets(Constants.LOGINSTATUS,false);
                   sharedPrefrenceHelper.saveString(Constants.DRIVERNAME,"null");
                   startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                   finish();

            }
        });

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(ProfileActivity.this, Editprofile2Activity.class));
            }
        });

    }

}
