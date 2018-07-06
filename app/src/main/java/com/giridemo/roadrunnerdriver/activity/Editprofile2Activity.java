package com.giridemo.roadrunnerdriver.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.Utils.Constants;
import com.giridemo.roadrunnerdriver.Utils.SharedPrefrenceHelper;
import com.giridemo.roadrunnerdriver.Utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Editprofile2Activity extends AppCompatActivity {

    EditText password,password1;
    Button save;
    ImageView back;
    SharedPrefrenceHelper sharedPrefrenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile2);
        sharedPrefrenceHelper = new SharedPrefrenceHelper(getApplicationContext());
        password = findViewById(R.id.password);
        password1 = findViewById(R.id.password1);
        back = findViewById(R.id.ivBackArrow);
        save = findViewById(R.id.btnSignout);
        performAction();
    }

    private void performAction() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Editprofile2Activity.this, ProfileActivity.class));
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            String driverName = sharedPrefrenceHelper.getString(Constants.DRIVERNAME);
            @Override
            public void onClick(View v) {
                String passwordtext = password.getText().toString();
                String passwordtext1 = password1.getText().toString();
                if (passwordtext.equals(passwordtext1))
                {
                    String base64password = Utils.base64Encrption(passwordtext);
                    System.out.println("driver name in edit profile "+driverName);
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriverData").child(driverName).child("password");
                    databaseReference.setValue(base64password);
                }
                else
                {
                    Utils.showToast(getApplicationContext(),"Password Mismatch");
                }
            }
        });
    }

}
