package com.hw3.stockwatch;

public class Stock {

    private String symbol;
    private String stockName;
    private double price;
    private double change;
    private double percent;

    public Stock(String symbol, String stockName, double price, double change, double percent){
        this.symbol = symbol;
        this.stockName = stockName;
        this.price = price;
        this.change = change;
        this.percent = percent;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getStockName() {
        return stockName;
    }

    public double getPrice() {
        return price;
    }

    public double getChange() {
        return change;
    }

    public double getPercent() {
        return percent;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", stockName='" + stockName + '\'' +
                ", price='" + price + '\'' +
                ", change='" + change + '\'' +
                ", percent='" + percent + '\'' +
                '}';
    }
}
