package com.giridemo.roadrunnerdriver.activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

    EditText email , password;
    Button login;
    SharedPrefrenceHelper sharedPrefrenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPrefrenceHelper =new SharedPrefrenceHelper(getApplicationContext());
        email =findViewById(R.id.email);
        password =findViewById(R.id.password);
        login =findViewById(R.id.login);

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserName())
                {
                    Utils.showToast(getApplicationContext(),"Enter Valid UserName");
                }else if(checkPassword())
                {
                    Utils.showToast(getApplicationContext(),"Enter Valid Password");

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
}