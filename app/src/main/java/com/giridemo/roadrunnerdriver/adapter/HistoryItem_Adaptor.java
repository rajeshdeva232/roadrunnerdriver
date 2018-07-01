package com.giridemo.roadrunnerdriver.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.model.GetItemlist;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

public class HistoryItem_Adaptor extends RecyclerView.Adapter<HistoryItem_Adaptor.ViewHolder>  {

    private Context applicationContext;
    private ArrayList<GetItemlist> getItemlists;
    private String TAG=HistoryItem_Adaptor.class.getSimpleName();

    public HistoryItem_Adaptor(Context Context, ArrayList<GetItemlist> getItemlists){
        this.applicationContext=Context;
        this.getItemlists=getItemlists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final GetItemlist itemlist =getItemlists.get(position);
        if(itemlist.getNeed_Complementry()==1){
            holder.tvNocomplementry.setText("View the selected complementry");
            for(final String complementry:itemlist.getComplement().keySet()) {
                if (itemlist.getComplement().get(complementry) == 1) {
                    Log.i(TAG, "onBindViewHolder: compementry==>"+complementry);
                    final TextView tv = new TextView(applicationContext);
                    tv.setText(complementry);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMarginStart(20);
                    tv.setLayoutParams(params);
                    tv.setTextColor(applicationContext.getResources().getColor(R.color.greay));
                    tv.setTag(complementry);
                    holder.ltComplementry.addView(tv);
                }
            }
            holder.tvNocomplementry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.elComplementry.isExpanded()){
                        holder.elComplementry.collapse();
                        holder.ivexpand.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_dropdown));
                    }else{
                        holder.elComplementry.expand();
                        holder.ivexpand.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_dropup));
                    }
                }
            });

            holder.ivexpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(holder.elComplementry.isExpanded()){
                        holder.elComplementry.collapse();
                        holder.ivexpand.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_dropdown));
                    }else{
                        holder.elComplementry.expand();
                        holder.ivexpand.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_dropup));
                    }

                }
            });
        }else{
            holder.elComplementry.setVisibility(View.GONE);
            holder.ivexpand.setVisibility(View.GONE);
            if(itemlist.getIsComplementryAvalible()==0) {
                holder.tvNocomplementry.setText("No Complement for this item.");
            }else{
                holder.tvNocomplementry.setText("You have not selected any complementry");
            }
        }

        holder.tvHotalname.setText(String.valueOf(itemlist.getItem()+","+itemlist.getHotalName()));
        holder.tvTotalamount.setText(String.valueOf(itemlist.getAmount()));
        holder.tvTotalquatity.setText(String.valueOf(itemlist.getQuantity()));
        holder.costPeritem.setText(String.valueOf(itemlist.getPrice_Per_Item()));

    }

    @Override
    public int getItemCount() {
        return getItemlists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTotalquatity,tvTotalamount,tvHotalname,tvNocomplementry,costPeritem;
        LinearLayout ltComplementry;
        ImageView ivItem,ivexpand;
        ExpandableLayout elComplementry;

        ViewHolder(View itemView) {
            super(itemView);
            tvHotalname=itemView.findViewById(R.id.tvHotalname);
            costPeritem=itemView.findViewById(R.id.costPeritem);
            ivItem=itemView.findViewById(R.id.ivItem);
            tvTotalquatity=itemView.findViewById(R.id.tvTotalquatity);
            tvTotalamount=itemView.findViewById(R.id.tvTotalamount);
            ltComplementry=itemView.findViewById(R.id.ltComplementry);
            ivexpand=itemView.findViewById(R.id.ivexpand);
            elComplementry=itemView.findViewById(R.id.elComplementry);
            tvNocomplementry=itemView.findViewById(R.id.tvNocomplementry);

        }
    }
}