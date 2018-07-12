package com.giridemo.roadrunnerdriver.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.adapter.HistoryAdapter;
import com.giridemo.roadrunnerdriver.model.DriverHistory;

import java.util.ArrayList;

public class OrderHistoryctivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DriverHistory> driverhsitory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_historyctivity);
        recyclerView = findViewById(R.id.rvHistory);
        driverhsitory = new ArrayList<>();

        performAction();
    }

    private void performAction() {
        DriverHistory driverHistory2=new DriverHistory("name","address","mobile","total");
        driverhsitory.add(driverHistory2);
        DriverHistory driverHistory1 =new DriverHistory("name1","address1","mobile1","total1");
        driverhsitory.add(driverHistory1);
        HistoryAdapter historyAdapter =new HistoryAdapter(getApplicationContext(),driverhsitory);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historyAdapter);

    }
}
