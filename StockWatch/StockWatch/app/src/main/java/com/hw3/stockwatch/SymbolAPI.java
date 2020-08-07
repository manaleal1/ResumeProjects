package com.hw3.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SymbolAPI extends AsyncTask<String, Void, String> {

    private static final String TAG = "SymbolAPI";
    private MainActivity mainActivity;
    private JSONArray allResults = new JSONArray();
    static HashMap<String, String> nameMap = new HashMap<>();

    SymbolAPI(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... strings) {

        String api = strings[0];
        Uri.Builder buildURL = Uri.parse(api).buildUpon();
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

        return allResults.toString();
    }

    private String parseJSONResults(String s){
        try {
            JSONArray dataResults = new JSONArray(s);
            Log.d(TAG, "parseJSONResults: " + dataResults.length());
            for (int i = 0; i < dataResults.length(); i++) {
                JSONObject symbolRecord = dataResults.getJSONObject(i);
                allResults.put(symbolRecord);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String symbol = jsonObject.getString("symbol").trim();
                String name = jsonObject.getString("name").trim();

                nameMap.put(symbol, name);
            }
            mainActivity.acceptResults(nameMap);
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mainActivity.acceptResults(null);
    }

    public static ArrayList<String> symbolMatches(String s){
        ArrayList<String> matches = new ArrayList<>();
        for(String name: nameMap.keySet()){
            if(name.contains(s) || nameMap.get(name).contains(s)){
                matches.add(name + " - " + nameMap.get(name));
            }
        }

        return matches;
    }

}
