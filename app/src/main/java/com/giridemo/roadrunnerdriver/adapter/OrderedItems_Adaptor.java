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

import com.bumptech.glide.Glide;
import com.giridemo.roadrunnerdriver.R;
import com.giridemo.roadrunnerdriver.model.GetItemlist;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

public class OrderedItems_Adaptor extends RecyclerView.Adapter<OrderedItems_Adaptor.ViewHolder>{

    private Context context;
    private ArrayList<GetItemlist> getItemlists;
    private static final String TAG=OrderedItems_Adaptor.class.getSimpleName();

    public OrderedItems_Adaptor(Context context, ArrayList<GetItemlist> getItemlists){
        this. context=context;
        this. getItemlists=getItemlists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ordereditems, parent, false);

        return new OrderedItems_Adaptor.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final GetItemlist itemlist = getItemlists.get(position);
        Log.i(TAG, "onBindViewHolder: name==>"+itemlist.getQuantity());
        holder.tvHotalname.setText(itemlist.getItem());
        holder.tvItemcount.setText(String.valueOf(itemlist.getQuantity()));
        holder.tvTotalAmount.setText(String.valueOf(itemlist.getAmount()));
        Glide.with(context)
                .load(itemlist.getImageUrl())
                .into(holder.ivItem);
        if(itemlist.getNeed_Complementry()==1){
            for(final String complementry:itemlist.getComplement().keySet()){
                if (itemlist.getComplement().get(complementry) == 1) {
                    TextView tvComplement=new TextView(context);
                    tvComplement.setText(complementry);
                    tvComplement.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    tvComplement.setTag(complementry);
                    holder.ltComplement.addView(tvComplement);
                }
            }
            holder.ivExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.ltExpand.isExpanded()){
                        holder.ltExpand.collapse(true);
                        holder.ivExpand.setImageDrawable(context.getDrawable(R.drawable.ic_dropdown));
                    }else {
                        holder.ltExpand.expand(true);
                        holder.ivExpand.setImageDrawable(context.getDrawable(R.drawable.ic_dropup));
                    }
                }
            });
        }else {
            holder.ivExpand.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return getItemlists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvHotalname,tvItemcount,tvTotalAmount;
        ImageView ivExpand,ivItem;
        LinearLayout ltComplement;
        ExpandableLayout ltExpand;

        ViewHolder(View itemView) {
            super(itemView);
            tvHotalname=itemView.findViewById(R.id.tvHotalname);
            tvItemcount=itemView.findViewById(R.id.tvItemcount);
            tvTotalAmount=itemView.findViewById(R.id.tvTotalAmount);
            ivExpand=itemView.findViewById(R.id.ivExpand);
            ivItem=itemView.findViewById(R.id.ivItem);
            ltComplement=itemView.findViewById(R.id.ltComplement);
            ltExpand=itemView.findViewById(R.id.ltExpand);
        }
    }
}
