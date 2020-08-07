package com.hw3.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StockDownloader extends AsyncTask<String, View, String> {

    private static final String TAG = "StockDownloader";
    private MainActivity mainActivity;
    private JSONArray allResults = new JSONArray();
    private JSONObject jsonResult;

    StockDownloader(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... strings) {
        //Query Format: https://cloud.iexapis.com/stable/stock/stock_symbol/quote?token=your_token
        String stock_symbol = strings[0];
        String your_token = strings[1];
        String query = "https://cloud.iexapis.com/stable/stock/" + stock_symbol + "/quote?token=" + your_token;
        Uri.Builder buildURL = Uri.parse(query).buildUpon();
        String urlToUse = buildURL.build().toString();

        while(urlToUse != null){
            Log.d(TAG, "doInBackground: " + urlToUse);

            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(urlToUse);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "doInBackground: ");
                    return null;
                }

                conn.setRequestMethod("GET");

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while((line = reader.readLine()) != null){
                    sb.append(line).append('\n');
                }

                urlToUse = parseJSONResults(sb.toString());

            } catch(Exception e){
                return null;
            }

        }

        return jsonResult.toString();
        
    }

    private String parseJSONResults(String s){
        try {
            jsonResult = new JSONObject(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        try {
                JSONObject jsonObject = jsonResult;
                String symbol = jsonObject.getString("symbol").trim();
                String name = jsonObject.getString("companyName").trim();
                String price;
                String change;
                String changePercent;

                if(jsonObject.getString("latestPrice").equals("null"))
                    price = "0.0";
                else
                    price = jsonObject.getString("latestPrice").trim();

                if(jsonObject.getString("change").equals("null"))
                    change = "0.0";
                else
                    change = jsonObject.getString("change").trim();

                if(jsonObject.getString("changePercent").equals("null"))
                    changePercent = "0.0";
                else
                    changePercent = jsonObject.getString("changePercent").trim();
                mainActivity.addTheStockObject( new Stock(symbol,
                                                            name,
                                                            Double.parseDouble(price),
                                                            Double.parseDouble(change),
                                                            Double.parseDouble(changePercent)) );

            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
