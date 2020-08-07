package com.hw3.stockwatch;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder>{

    private static final String TAG = "StockAdapter";

    private final ArrayList<Stock> sList;
    private MainActivity mainActivity;

    StockAdapter(ArrayList<Stock> stockList, MainActivity mainActivity) {
        this.sList = stockList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType){
        Log.d(TAG, "onCreateViewHolder: ");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent, false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: SETTING DATA");
        Stock selection = sList.get(position);
        //▲
        //▼
        holder.symbol.setText( selection.getSymbol() );
        holder.stockName.setText( selection.getStockName() );
        holder.price.setText( String.format("%.2f", selection.getPrice() ) );

        if(selection.getChange() < 0) {
            holder.change.setText( "▼ "+String.format("%.2f", selection.getChange()) +"(" + String.format( "%.2f", selection.getPercent() ) + "%)" );
            holder.symbol.setTextColor(0xFFFF0000); //Red
            holder.stockName.setTextColor(0xFFFF0000);
            holder.price.setTextColor(0xFFFF0000);
            holder.change.setTextColor(0xFFFF0000);
        }
        else{
            holder.change.setText( "▲ "+String.format("%.2f", selection.getChange()) +"(" + String.format( "%.2f", selection.getPercent() ) + "%)" );
            holder.symbol.setTextColor(0xFF00FF0C);
            holder.stockName.setTextColor(0xFF00FF0C);
            holder.price.setTextColor(0xFF00FF0C);
            holder.change.setTextColor(0xFF00FF0C);
        }


    }

    @Override
    public int getItemCount() {
        return sList.size();
    }


}
