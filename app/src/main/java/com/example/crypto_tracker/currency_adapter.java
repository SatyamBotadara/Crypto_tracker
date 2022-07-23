package com.example.crypto_tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class currency_adapter extends RecyclerView.Adapter<currency_adapter.viewHolder> {

    private ArrayList<crypto> list;
    private Context context;
    private static DecimalFormat df2=new DecimalFormat("#.##");

    public currency_adapter(ArrayList<crypto> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.crypto_rv_item,parent,false);
        return new currency_adapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        crypto crypto=list.get(position);
        holder.ratetv.setText(String.valueOf("$ "+df2.format(crypto.getPrice())));
        holder.currencyname.setText(crypto.getName());
        holder.symboltv.setText(crypto.getSymbol());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        private TextView currencyname,symboltv,ratetv;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            this.currencyname=itemView.findViewById(R.id.currencyname);
            this.symboltv=itemView.findViewById(R.id.symbol);
            this.ratetv=itemView.findViewById(R.id.rate);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<crypto> list){
        this.list=list;
        notifyDataSetChanged();
    }

}
