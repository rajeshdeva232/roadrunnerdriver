package com.giridemo.roadrunnerdriver.dialogfragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.Utils.Constants;
import com.giridemo.roadrunnerdriver.Utils.Utils;
import com.giridemo.roadrunnerdriver.adapter.OrderedItems_Adaptor;
import com.giridemo.roadrunnerdriver.model.GetItemlist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderedItems extends DialogFragment {

    TextView tvHotalname,tvClose;
    RecyclerView rvOrdereditems;
    ArrayList<GetItemlist> getItemlists=new ArrayList<>();
//    String hotalName,orderId;


    public OrderedItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_ordered_items, container, false);
        tvHotalname=view.findViewById(R.id.tvHotalname);
        tvClose=view.findViewById(R.id.tvClose);
        rvOrdereditems=view.findViewById(R.id.rvOrdereditems);
        if (getArguments() != null) {
            String hotalName=getArguments().getString(Constants.SELECTEDHOTELNAME);
            tvHotalname.setText(hotalName);
            String orderId=getArguments().getString(Constants.ORDERID);
            getDetails(hotalName,orderId);
        }
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    private void getDetails(String hotalName, String orderId) {

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("PlaceOrder").child(orderId).child("Ordered items");
        databaseReference.child(hotalName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot itemDetails:dataSnapshot.getChildren()){
                    GetItemlist getItemlist=new GetItemlist();
                    getItemlist.setItem(itemDetails.getKey());
                    getItemlist.setAmount(Integer.parseInt(Objects.requireNonNull(itemDetails.child("Amount").getValue()).toString()));
                    getItemlist.setPrice_Per_Item(Integer.parseInt(Objects.requireNonNull(itemDetails.child("Price_Per_Item").getValue()).toString()));
                    getItemlist.setQuantity(Integer.parseInt(Objects.requireNonNull(itemDetails.child("Quantity").getValue()).toString()));
                    getItemlist.setImageUrl(String.valueOf(itemDetails.child("ImageUrl").getValue()));
                    getItemlist.setNeed_Complementry(Integer.parseInt(String.valueOf(itemDetails.child("Need_Complementry").getValue())));
                    if(Integer.parseInt(String.valueOf(itemDetails.child("Need_Complementry").getValue()))==1){
                        Map<String,Integer> complement=new HashMap<>();
                        for(DataSnapshot complimentry:itemDetails.child("Complementry").getChildren()){
                            complement.put(complimentry.getKey(),Integer.parseInt(Objects.requireNonNull(complimentry.getValue()).toString()));
                            getItemlist.setComplement(complement);
                        }
                    }
                    getItemlists.add(getItemlist);
                }
                OrderedItems_Adaptor orderedItems=new OrderedItems_Adaptor(getContext(),getItemlists);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                rvOrdereditems.setLayoutManager(mLayoutManager);
                rvOrdereditems.setItemAnimator(new DefaultItemAnimator());
                rvOrdereditems.setAdapter(orderedItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Utils.showToast(getContext(),databaseError.getMessage());
            }
        });
    }

}
