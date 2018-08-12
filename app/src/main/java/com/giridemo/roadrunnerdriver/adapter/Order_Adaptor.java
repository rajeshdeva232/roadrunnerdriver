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
import com.giridemo.roadrunnerdriver.model.OrderHistory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Order_Adaptor extends RecyclerView.Adapter<Order_Adaptor.ViewHolder>{
   private SharedPrefrenceHelper sharedPrefrenceHelper;
    private Context context;
    private ArrayList<OrderHistory> orderHistories;

    public Order_Adaptor(Context context, ArrayList<OrderHistory> orderHistories){
        this.context=context;
        this.orderHistories=orderHistories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.odercard, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final OrderHistory orderHistory=orderHistories.get(position);
        sharedPrefrenceHelper = new SharedPrefrenceHelper(context);

        holder.tvName.setText(String.valueOf(orderHistory.getDelivername()+","+orderHistory.getDelivermobileNo()));
        holder.tvTime.setText(orderHistory.getOrderedTime());
        holder.tvAddress.setText(orderHistory.getAddresstoDelevery());
//        holder.tvDoorno.setText(orderHistory.get);
        if(!orderHistory.getLandmark().equals("NO_LANDMARK_PROVIDED")) {
            holder.tvLandmark.setText(orderHistory.getLandmark());
        }else {
            holder.tvLandmark.setVisibility(View.GONE);
        }
        if(!orderHistory.getDoorNo().equals("NO DOORNO PROVIDED")) {
            holder.tvDoorno.setText(orderHistory.getDoorNo());
        }else {
            holder.tvDoorno.setVisibility(View.GONE);
        }
        holder.tvTotal.setText(orderHistory.getTotalamount());
        holder.tvlatlng.setText(String.valueOf(orderHistory.getDeleveryLatlng()));
        holder.ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.ltExpand.isExpanded()){
                    holder.ltExpand.collapse();
                    holder.ivExpand.setImageDrawable(context.getDrawable(R.drawable.ic_dropdown));
                }else{
                    holder.ltExpand.expand();
                    holder.ivExpand.setImageDrawable(context.getDrawable(R.drawable.ic_dropup));
                }

            }
        });

        holder.tvUsername.setText(orderHistory.getUsername());
        holder.tvUserNo.setText(orderHistory.getUsermobileno());
        HistoryItem_Adaptor historyItemAdaptor=new HistoryItem_Adaptor(context,orderHistory.getGetItemlistArrayList());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        holder.rvExpand.setLayoutManager(mLayoutManager);
        holder.rvExpand.setItemAnimator(new DefaultItemAnimator());
        holder.rvExpand.setAdapter(historyItemAdaptor);
        holder.ltParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.ltExpand.isExpanded()){
                    holder.ltExpand.collapse();
                    holder.ivExpand.setImageDrawable(context.getDrawable(R.drawable.ic_dropdown));
                }else{
                    holder.ltExpand.expand();
                    holder.ivExpand.setImageDrawable(context.getDrawable(R.drawable.ic_dropup));
                }

            }
        });

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                acceptRequest(orderHistory);
            }
        });
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest(orderHistory);
            }
        });

    }

    private void  acceptRequest(OrderHistory orderHistory) {
        String name = sharedPrefrenceHelper.getString(Constants.DRIVERNAME);
        sharedPrefrenceHelper.saveString(Constants.ORDERID,orderHistory.getKey());
        MainActivity.orderId=orderHistory.getKey();
        System.out.println("picked by name in adapter "+name);
        Map<String,Object> acceptRequest= new HashMap<>();
        acceptRequest.put("deliveryStatus",1);
        acceptRequest.put("orderpickedby",name);
        acceptRequest.put("adminnotified",1);
        acceptRequest.put("notifiedUser",1);
        acceptRequest.put("processedTime", Utils.getCurrentTimeStamp());
        acceptRequest.put("dispatchHotel","");
        acceptRequest.put("dispatchStatus",0);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> updateDriver=new HashMap<>();
        updateDriver.put("Current orderId",orderHistory.getKey());
        databaseReference.child("DriverData").child(sharedPrefrenceHelper.getString(Constants.DRIVERNAME)).updateChildren(updateDriver);
//        databaseReference.updateChildren(updateDriver);
        databaseReference.child("PlaceOrder").child(orderHistory.getKey()).updateChildren(acceptRequest);
        Intent intent=new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
//        context.startActivity(new Intent(context, MainActivity.class));
    }

    private void rejectRequest(OrderHistory orderHistory) {
        Map<String,Object> rejectRequest= new HashMap<>();
        rejectRequest.put("deliveryStatus",-1);
        rejectRequest.put("processedTime", Utils.getCurrentTimeStamp());
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("PlaceOrder");
        databaseReference.child(orderHistory.getKey()).updateChildren(rejectRequest);
    }

    @Override
    public int getItemCount() {
        return orderHistories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvTime,tvDoorno,tvLandmark,tvAddress,tvlatlng,tvTotal,tvUsername,tvUserNo;
        RecyclerView rvExpand;
        ImageView ivExpand;
        ExpandableLayout ltExpand;
        CardView ltParent;
        Button btnAccept,btnReject;

        ViewHolder(View itemView) {
            super(itemView);

            ltExpand=itemView.findViewById(R.id.ltExpand);
            tvAddress=itemView.findViewById(R.id.tvAddress);
            tvName=itemView.findViewById(R.id.tvName);
            tvTime=itemView.findViewById(R.id.tvTime);
            tvDoorno=itemView.findViewById(R.id.tvDoorno);
            tvLandmark=itemView.findViewById(R.id.tvLandmark);
            tvlatlng=itemView.findViewById(R.id.tvlatlng);
            rvExpand=itemView.findViewById(R.id.rvExpand);
            ivExpand=itemView.findViewById(R.id.ivExpand);
            tvTotal=itemView.findViewById(R.id.tvTotal);
            ltParent=itemView.findViewById(R.id.ltParent);
            tvUsername=itemView.findViewById(R.id.tvUsername);
            tvUserNo=itemView.findViewById(R.id.tvUserNo);
            btnAccept=itemView.findViewById(R.id.btnAccept);
            btnReject=itemView.findViewById(R.id.btnReject);
        }
    }
}
