package com.giridemo.roadrunnerdriver.activity;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity  {

    TextInputEditText email , password;
    TextInputLayout tlPassword,tlUsername;
    Button login;
    SharedPrefrenceHelper sharedPrefrenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPrefrenceHelper =new SharedPrefrenceHelper(getApplicationContext());
        email =findViewById(R.id.email);
        password =findViewById(R.id.password);
        tlUsername=findViewById(R.id.tlUsername);
        tlPassword=findViewById(R.id.tlPassword);
        login =findViewById(R.id.login);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tlUsername.setError(null);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                tlPassword.setError(null);
            }
        });

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserName())
                {
                    tlUsername.setError("Enter Valid UserName");
                    email.requestFocus();
                }else if(checkPassword())
                {
                    tlPassword.setError("Enter Valid Password");
                   password.requestFocus();

                }else {

                    checkCredentials(email.getText().toString(),password.getText().toString());
                }
            }
        });
    }

    private void checkCredentials(final String email , final String password) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriverData").child(email);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    System.out.println("encript pass word "+Utils.base64Encrption(password).trim());
                    if(Utils.base64Encrption(password).trim().equals(String.valueOf(dataSnapshot.child("password").getValue()))) {
                        sharedPrefrenceHelper.saveString(Constants.DRIVERNAME, email);
                        sharedPrefrenceHelper.saveBoolean(Constants.LOGINSTATUS,true);
                        startActivity(new Intent(LoginActivity.this, OderActivity.class));
                    }else{
                        Utils.showToast(getApplicationContext(),"Invalid Password");
                    }

                }else {
                    Utils.showToast(getApplicationContext(),"User Id Not Exits");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private boolean checkPassword() {
        if (TextUtils.isEmpty(password.getText().toString()))
        {
            return true;
        }
        return false;
    }

    private boolean checkUserName() {

        if (TextUtils.isEmpty(email.getText().toString()))
        {
            return true;
        }
        return false;
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
}
