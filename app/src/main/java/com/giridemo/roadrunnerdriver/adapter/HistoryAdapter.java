package com.giridemo.roadrunnerdriver.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.Utils.Constants;
import com.giridemo.roadrunnerdriver.Utils.SharedPrefrenceHelper;
import com.giridemo.roadrunnerdriver.Utils.Utils;
import com.giridemo.roadrunnerdriver.activity.MainActivity;
import com.giridemo.roadrunnerdriver.model.DriverHistory;
import com.giridemo.roadrunnerdriver.model.OrderHistory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private SharedPrefrenceHelper sharedPrefrenceHelper;
    private Context context;
    private ArrayList<DriverHistory> orderHistories;

    public HistoryAdapter(Context context, ArrayList<DriverHistory> orderHistories){
        this.context=context;
        this.orderHistories=orderHistories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_oderitem_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final DriverHistory orderHistory=orderHistories.get(position);
        sharedPrefrenceHelper = new SharedPrefrenceHelper(context);
        System.out.println("array list size "+orderHistories.size()+"       "+orderHistory.getName());
        System.out.println("array list in adapter "+orderHistory.getMobile());
       holder.name.setText(orderHistory.getName());
       holder.address.setText(orderHistory.getAddress());
       holder.mobile.setText(orderHistory.getMobile());
       holder.name.setText(orderHistory.getTotalamount());
    }
    @Override
    public int getItemCount() {
        return orderHistories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,address,mobile,totalAmount;

        ViewHolder(View itemView) {
            super(itemView);

            name =itemView.findViewById(R.id.tvName);
            address = itemView.findViewById(R.id.tvAddress);
            mobile = itemView.findViewById(R.id.tvMobile);
            totalAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}

