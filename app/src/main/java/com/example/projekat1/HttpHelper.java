package com.example.projekat1;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHelper {

    public JSONObject getWeatherData(String city) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?id=524901&APPID=563672fdfc4a9084c6a863aec970bcc6" + "&units=metric&q=" + city);
        Log.d("GETWEATHER", "Trying to connect to "+url.toString());
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(10000);
        try{
            urlConnection.connect();
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line=br.readLine())!=null){
            sb.append(line+"\n");
        }
        br.close();
        String jsonString = sb.toString();
        Log.d("GETWEATHER", "Server response: " + jsonString);
        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();

        if(responseCode == 200)
            return new JSONObject(jsonString);
        return null;


    }

}
