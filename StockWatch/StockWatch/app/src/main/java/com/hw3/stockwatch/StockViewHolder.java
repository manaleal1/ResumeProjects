package com.hw3.stockwatch;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class StockViewHolder extends RecyclerView.ViewHolder {

    TextView symbol;
    TextView stockName;
    TextView price;
    TextView change;

    StockViewHolder(View view){
        super(view);

        symbol = view.findViewById(R.id.symbol);
        stockName = view.findViewById(R.id.stockName);
        price = view.findViewById(R.id.price);
        change = view.findViewById(R.id.change);
    }

}
