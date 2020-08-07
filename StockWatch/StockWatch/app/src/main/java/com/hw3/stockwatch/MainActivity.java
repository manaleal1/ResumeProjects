package com.hw3.stockwatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


/**
 * BEFORE RUNNING:
 * o Read the README file located in thisRepository/StockWatch. It tells you how to get the IEX token.
 *   Once you get your token, put the token in the variable "token" found in line 68.
 */

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener{

    //1. Download internet data:  https://api.iextrading.com/1.0/ref-data/symbols
    //   We are interested in symbol and name
    //2. Download Stock Financial data
    //   Query Format: https://cloud.iexapis.com/stable/stock/stock_symbol/quote?token=your_token
    //   Example: https://cloud.iexapis.com/stable/stock/TSLA/quote?token=ABC123
    //   We are interested in symbol, companyName, latestPrice, change, changePercent
    //   If not found, return "Response Code: 404 - Not Found"

    private static final String TAG = "MainActivity";

    private SwipeRefreshLayout swiper;
    private RecyclerView recyclerView;
    private StockAdapter stockAdapter;
    private ArrayList<Stock> stockList = new ArrayList<>(); //Adapter pays attention to this one


    private String symbolAPI = "https://api.iextrading.com/1.0/ref-data/symbols";
    private String token = "Replace_with_Your_Token";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Stock Watch");
        setTitleColor(0xFFFFFF);

        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                 doRefresh();
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        stockAdapter = new StockAdapter(stockList, this);
        recyclerView.setAdapter(stockAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Stock> tempList = new ArrayList<>(); //Info from JSON file goes here
        try {
            InputStream inputStream = openFileInput("stockData.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();

                String jsonText = stringBuilder.toString();

                try {
                    JSONArray jsonArray = new JSONArray(jsonText);
                    Log.d(TAG, "onCreate: " + jsonArray.length());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String symbol = jsonObject.getString("symbolText");
                        String name = jsonObject.getString("stockNameText");
                        double price = jsonObject.getDouble("priceText");
                        double change = jsonObject.getDouble("changeText");
                        double changePercent = jsonObject.getDouble("changePercentText");

                        Stock n = new Stock(symbol, name, price, change, changePercent);
                        //stockList.add(n);
                        tempList.add(n);
                    }


                    Log.d(TAG, "onCreate: " + stockList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "onCreate: File not found: \" + e.toString()");
        } catch (IOException e) {
            Log.d(TAG, "onCreate: Can not read file: " + e.toString());
        }


        if(doNetCheck()){
            new SymbolAPI(this).execute(symbolAPI);
            for(Stock s : tempList){
                new StockDownloader(this).execute(s.getSymbol(),token);
                //add, sort, and notify adapter happens in addTheStockObject, which is called by StockDownloader
            }
        }
        else{
            errorDialog("No Network Connection", "");
            for(Stock s : tempList){
                stockList.add( new Stock(s.getSymbol(), s.getStockName(), 0.0, 0.0, 0.0) );
            }

            sortStockArrayList();
            stockAdapter.notifyDataSetChanged();
        }

    }//End of onCreate

    private void doRefresh(){
        stockList.clear();
        stockAdapter.notifyDataSetChanged();
        ArrayList<Stock> tempList = new ArrayList<>(); //Info from JSON file goes here
        try {
            InputStream inputStream = openFileInput("stockData.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();

                String jsonText = stringBuilder.toString();

                try {
                    JSONArray jsonArray = new JSONArray(jsonText);
                    Log.d(TAG, "onCreate: " + jsonArray.length());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String symbol = jsonObject.getString("symbolText");
                        String name = jsonObject.getString("stockNameText");
                        double price = jsonObject.getDouble("priceText");
                        double change = jsonObject.getDouble("changeText");
                        double changePercent = jsonObject.getDouble("changePercentText");

                        Stock n = new Stock(symbol, name, price, change, changePercent);


                        tempList.add(n);
                    }


                    Log.d(TAG, "onCreate: " + stockList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "onCreate: File not found: \" + e.toString()");
        } catch (IOException e) {
            Log.d(TAG, "onCreate: Can not read file: " + e.toString());
        }


        if(doNetCheck()){
            new SymbolAPI(this).execute(symbolAPI);
            for(Stock s : tempList){
                new StockDownloader(this).execute(s.getSymbol(),token);
                //add, sort, and notify adapter happens in addTheStockObject, which is called by StockDownloader
            }
        }
        else{
            errorDialog("No Network Connection", "");
            for(Stock s : tempList){
                stockList.add( new Stock(s.getSymbol(), s.getStockName(), 0.0, 0.0, 0.0) );
            }

            sortStockArrayList();
            stockAdapter.notifyDataSetChanged();
        }
        swiper.setRefreshing(false);


    }//end of doRefresh method

    public boolean doNetCheck(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            //Toast.makeText(this, "Connection:  "+netInfo.isConnected(), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            //Toast.makeText(this, "No Connection:", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void errorDialog(String errorTitle, String errorMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        // Code goes here

                    }

                });
        builder.setMessage(errorMessage);
        builder.setTitle(errorTitle);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void sortStockArrayList(){
        Collections.sort(stockList,new Comparator<Stock>() {
            @Override
            public int compare(Stock s1, Stock s2) {
                int limit = 0;
                if(s1.getSymbol().length() <= s2.getSymbol().length()){
                    limit = s1.getSymbol().length();
                }
                else{
                    limit = s2.getSymbol().length();
                }
                for(int i = 0; i < limit; i++) {
                    if (s1.getSymbol().charAt(i) < s2.getSymbol().charAt(i))
                        return -1;
                    else if (s1.getSymbol().charAt(i) > s2.getSymbol().charAt(i))
                        return 1;
                }
                return 0;
            }
        });

    }

    public void acceptResults(HashMap<String,String> results) {
        //symbolsArray = results;
        Log.d(TAG, "acceptResults: " + results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_stock_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.addStock:
                if(doNetCheck())
                    askForSymbolDialog();
                else
                    errorDialog("No Network Connection", "Stocks Cannot Be Added Without A Network Connection");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void askForSymbolDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);

        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        et.setFilters(new InputFilter[] {new InputFilter.AllCaps()} );

        builder.setView(et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
               ArrayList<String> namelist = SymbolAPI.symbolMatches(et.getText().toString());
               if(namelist.size() == 0){
                   //errorDialog("Symbol Not Found: " + et.getText(), "Data for Stock Symbol");
                   errorDialog("Response Code: 404 Not Found","");
                   return;
               }
               else if(namelist.size() == 1){
                   new StockDownloader(MainActivity.this).execute(namelist.get(0).substring(0,namelist.get(0).indexOf(" ")), token);
                   return;
               }
               displayMatches(namelist);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Cancel does nothing; just closes the dialog box
            }
        });

        builder.setMessage("Please enter a Stock Symbol:");
        builder.setTitle("Stock Selection");

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void displayMatches(ArrayList<String> nameslist){
        final CharSequence[] c = new CharSequence[nameslist.size()];
        int i = 0;
        for(String s : nameslist){
            c[i] = s;
            i++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a selection");

        builder.setItems(c, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new StockDownloader(MainActivity.this).execute(c[which].subSequence(0,c[which].toString().indexOf(" ")).toString(), token);
                //Toast.makeText(MainActivity.this, c[which].subSequence(0,c[which].toString().indexOf(" ")), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();

    }

    public void addTheStockObject(Stock stock){
        for(Stock s : stockList) {
            if (s.getSymbol().equals(stock.getSymbol())) {
                errorDialog("Duplicate Stock", "Stock symbol " + stock.getSymbol() + " is already displayed");
                return;
            }
        }
        stockList.add(stock);
        sortStockArrayList();

        JSONArray jsonArray = new JSONArray();

        for(Stock n : stockList){
            try{
                JSONObject stockJSON = new JSONObject();
                stockJSON.put("symbolText", n.getSymbol());
                stockJSON.put("stockNameText", n.getStockName());
                stockJSON.put("priceText", n.getPrice());
                stockJSON.put("changeText", n.getChange());
                stockJSON.put("changePercentText", n.getPercent());
                jsonArray.put(stockJSON);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        String jsonText = jsonArray.toString();

        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(
                            openFileOutput("stockData.txt", Context.MODE_PRIVATE)
                    );

            outputStreamWriter.write(jsonText);
            outputStreamWriter.close();
            //Toast.makeText(this, "File write success!", Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Log.d(TAG, "File write failed: " + e.toString());
            Toast.makeText(this, "File write failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        stockAdapter.notifyDataSetChanged(); //VERY IMPORTANT!!!

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");

        JSONArray jsonArray = new JSONArray();

        for(Stock n : stockList){
            try{
                JSONObject stockJSON = new JSONObject();
                stockJSON.put("symbolText", n.getSymbol());
                stockJSON.put("stockNameText", n.getStockName());
                stockJSON.put("priceText", n.getPrice());
                stockJSON.put("changeText", n.getChange());
                stockJSON.put("changePercentText", n.getPercent());
                jsonArray.put(stockJSON);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        String jsonText = jsonArray.toString();

        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(
                            openFileOutput("stockData.txt", Context.MODE_PRIVATE)
                    );

            outputStreamWriter.write(jsonText);
            outputStreamWriter.close();
            //Toast.makeText(this, "File write success!", Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Log.d(TAG, "File write failed: " + e.toString());
            Toast.makeText(this, "File write failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        //Toast.makeText(this,"You clicked something...", Toast.LENGTH_SHORT).show();
        int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stockList.get(pos);
        //URL = http://www.marketwatch.com/investing/stock/some_stock
        try{
        String stockURL = "http://www.marketwatch.com/investing/stock/" + s.getSymbol();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(stockURL));
        startActivity(i);
        } catch(Exception e){
            Toast.makeText(this,"Failed to open website.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onLongClick(final View v) {
        Log.d(TAG, "onLongClick: ");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        // Code goes here
                        int pos = recyclerView.getChildLayoutPosition(v);
                        ////////////////////////////////START OF json ITEM REMOVAL

                        JSONArray jsonArray = new JSONArray();

                        for(Stock s : stockList){
                            try{
                                JSONObject stockJSON = new JSONObject();
                                stockJSON.put("symbolText", s.getSymbol());
                                stockJSON.put("stockNameText", s.getStockName());
                                stockJSON.put("priceText", s.getPrice());
                                stockJSON.put("changeText", s.getChange());
                                stockJSON.put("changePercentText", s.getPercent());
                                jsonArray.put(stockJSON);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        jsonArray.remove(pos); //<-- Super important!!!
                        String jsonText = jsonArray.toString();

                        try {
                            OutputStreamWriter outputStreamWriter =
                                    new OutputStreamWriter(
                                            openFileOutput("stockData.txt", Context.MODE_PRIVATE)
                                    );

                            outputStreamWriter.write(jsonText);
                            outputStreamWriter.close();
                        }
                        catch (IOException e) {
                            Log.d(TAG, "File write failed: " + e.toString());
                        }

                        //////////////////////////////////END OF json ITEM REMOVAL


                        stockList.remove(pos);
                        stockAdapter.notifyDataSetChanged();


                    }

                });

        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //code goes here

                    }
                });

        int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stockList.get(pos);
        builder.setMessage("Delete Stock Symbol " + s.getSymbol() + " ?");
        builder.setTitle("Delete Stock");

        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }
}
