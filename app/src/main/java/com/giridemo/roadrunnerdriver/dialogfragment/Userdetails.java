package com.giridemo.roadrunnerdriver.dialogfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.Utils.Constants;

import java.util.Objects;


public class Userdetails extends DialogFragment {




    public Userdetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_userdetails, container, false);
        Objects.requireNonNull(getActivity()).getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        getActivity().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        TextView tvName=view.findViewById(R.id.tvName);
        TextView tvMobileno=view.findViewById(R.id.tvMobileno);
        TextView tvLandmark=view.findViewById(R.id.tvLandmark);
        TextView tvDoorno=view.findViewById(R.id.tvDoorno);
        TextView tvOrderedname=view.findViewById(R.id.tvOrderedname);
        TextView tvOrderednumber=view.findViewById(R.id.tvOrderednumber);
        TextView tvClose=view.findViewById(R.id.tvClose);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if(getArguments()!=null){
            tvOrderedname.setText(getArguments().getString(Constants.ORDEREDBYNMAE));
            tvOrderednumber.setText(getArguments().getString(Constants.ORDEREDBYNO));
            tvMobileno.setText(getArguments().getString(Constants.COSTOMERMOBILENO));
            tvName.setText(String.valueOf(getArguments().getString(Constants.COSTOMERNAME)));
            if(!Objects.requireNonNull(getArguments().getString(Constants.COSTOMERLANDMARK)).trim().equals("NO_LANDMARK_PROVIDED")){
                tvLandmark.setText(String.valueOf(getArguments().getString(Constants.COSTOMERLANDMARK)));
            }else {
                tvLandmark.setVisibility(View.GONE);
            }
            if(!Objects.requireNonNull(getArguments().getString(Constants.COSTOMERDOORNO)).trim().equals("NO DOORNO PROVIDED")){
                tvDoorno.setText(String.valueOf(getArguments().getString(Constants.COSTOMERDOORNO)));
            }else {
                tvDoorno.setVisibility(View.GONE);
            }
        }

        return view;
    }
}
