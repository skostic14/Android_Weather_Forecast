package com.example.projekat1;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    TextView locTxt, dayTxt, tempTxt, presTxt, windDirTxt, windSpdTxt, sunriseTxt, sunsetTxt, humTxt, lastUpd;
    ImageView wxCond;
    Spinner unitSpinner;
    String dan;
    private int temperature, weathercode, temperatureF;
    public JSONObject wx, wind, main, sys, weatherinfo;
    public JSONArray weather;
    Button windBtn, tempBtn, sunBtn, statBtn;
    ImageButton refBtn;
    LinearLayout windLay, tempLay, sunLay;
    private HttpHelper httpHelper;
    int day, mode;
    public static String loc;
    private WxService wxService;
    private boolean isBound;
    private FahrConverter conv;

    public void addWxInfo(JSONObject wxinfo){
        try {
            JSONObject wxwind = wxinfo.getJSONObject("wind");
            JSONObject wxmain = wxinfo.getJSONObject("main");
            JSONObject wxsys = wxinfo.getJSONObject("sys");
            JSONArray wxarray = wxinfo.getJSONArray("weather");
            JSONObject wxcond = wxarray.getJSONObject(0);
            ContentValues values = new ContentValues();
            values.put(WxHelper.COLUMN_DATE_TIME, System.currentTimeMillis() / 1000L);
            values.put(WxHelper.COLUMN_CITY_NAME, loc);
            values.put(WxHelper.COLUMN_TEMPERATURE, wxmain.getInt("temp"));
            values.put(WxHelper.COLUMN_PRESSURE, wxmain.getString("pressure"));
            values.put(WxHelper.COLUMN_HUMIDITY, wxmain.getString("humidity"));
            values.put(WxHelper.COLUMN_SUNRISE, parseUnixTime(wxsys.getLong("sunrise")));
            values.put(WxHelper.COLUMN_SUNSET, parseUnixTime(wxsys.getLong("sunset")));
            values.put(WxHelper.COLUMN_WIND_SPEED, wxwind.getString("speed"));
            values.put(WxHelper.COLUMN_CONDITION, wxcond.getInt("id"));
            values.put(WxHelper.COLUMN_WIND_DIR, wxwind.getInt("deg"));
            ContentResolver res = getContentResolver();
            Log.d("DETAILS", values.toString());
            res.insert(WxProvider.CONTENT_URI, values);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("DETAILS", "Unable to parse data");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        locTxt = (TextView) findViewById(R.id.locInput);
        dayTxt = (TextView) findViewById(R.id.dayTxt);
        tempTxt = (TextView) findViewById(R.id.tempTxt);
        presTxt = (TextView) findViewById(R.id.presTxt);
        windDirTxt = (TextView) findViewById(R.id.wdirTxt);
        windSpdTxt = (TextView) findViewById(R.id.wspdTxt);
        sunriseTxt = (TextView) findViewById(R.id.sunrTxt);
        sunsetTxt = (TextView) findViewById(R.id.sunsTxt);
        humTxt = (TextView) findViewById(R.id.humTxt);
        lastUpd = (TextView) findViewById(R.id.dayLastUpd);

        wxCond = (ImageView) findViewById(R.id.wxIcon);
        windBtn = (Button) findViewById(R.id.windBtn);
        tempBtn = (Button) findViewById(R.id.tempBtn);
        sunBtn = (Button) findViewById(R.id.sunBtn);
        statBtn = (Button) findViewById(R.id.statBtn);
        windLay = (LinearLayout) findViewById(R.id.windLayout);
        tempLay = (LinearLayout) findViewById(R.id.tempLayout);
        sunLay = (LinearLayout) findViewById(R.id.sunLayout);
        unitSpinner = (Spinner) findViewById(R.id.tempSpinner);
        refBtn = (ImageButton) findViewById(R.id.refreshBtn);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tempunits, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        mode = 0;
        isBound = false;

        tempBtn.setOnClickListener(this);
        windBtn.setOnClickListener(this);
        sunBtn.setOnClickListener(this);
        statBtn.setOnClickListener(this);
        refBtn.setOnClickListener(this);
        unitSpinner.setOnItemSelectedListener(this);

        Intent in = getIntent();
        Bundle args = in.getExtras();
        loc = args.getString("city");
        httpHelper = new HttpHelper();
        Log.d("DETAILS", "Trying to get weather for "+loc);
        locTxt.setText(loc);

        Calendar today = Calendar.getInstance();
        day = today.get(Calendar.DAY_OF_WEEK);
        switch (day){
            case Calendar.MONDAY:
                dan = "Ponedeljak";
                break;
            case Calendar.TUESDAY:
                dan = "Utorak";
                break;
            case Calendar.WEDNESDAY:
                dan = "Sreda";
                break;
            case Calendar.THURSDAY:
                dan="Četvrtak";
                break;
            case Calendar.FRIDAY:
                dan="Petak";
                break;
            case Calendar.SATURDAY:
                dan = "Subota";
                break;
            case Calendar.SUNDAY:
                dan = "Nedelja";
                break;
            default:
                dan = "";
        }
        dayTxt.setText(dan);
        printLatestEntryDate();

        conv = new FahrConverter();
        temperatureF = -1;

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, WxService.class);
        Bundle bundle = new Bundle();
        bundle.putString("location", loc);
        intent.putExtras(bundle);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBound) {
            Intent intent = new Intent(this, WxService.class);
            unbindService(connection);
            isBound = false;
            stopService(intent);
            Log.d("WxService", "Stopped notifications");
        }
        isBound = false;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WxService.LocalBinder binder = (WxService.LocalBinder) service;
            wxService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tempBtn:
                if (mode == 1) {
                    mode = 0;
                    tempLay.setVisibility(View.INVISIBLE);
                } else {
                    tempLay.setVisibility(View.VISIBLE);
                    windLay.setVisibility(View.INVISIBLE);
                    sunLay.setVisibility(View.INVISIBLE);
                    mode = 1;
                }
                break;
            case R.id.sunBtn:
                if (mode == 2) {
                    mode = 0;
                    sunLay.setVisibility(View.INVISIBLE);
                } else {
                    tempLay.setVisibility(View.INVISIBLE);
                    windLay.setVisibility(View.INVISIBLE);
                    sunLay.setVisibility(View.VISIBLE);
                    mode = 2;
                }
                break;
            case R.id.windBtn:
                if (mode == 3) {
                    mode = 0;
                    windLay.setVisibility(View.INVISIBLE);
                } else {
                    tempLay.setVisibility(View.INVISIBLE);
                    windLay.setVisibility(View.VISIBLE);
                    sunLay.setVisibility(View.INVISIBLE);
                    mode = 3;
                }
                break;

            case R.id.statBtn:
                Intent statMove = new Intent(this, StatisticsActivity.class);
                Bundle args = new Bundle();
                args.putString("loc", loc);
                statMove.putExtras(args);
                this.startActivity(statMove);
                break;

            case R.id.refreshBtn:
                parseJSONWeather(loc);
                refBtn.setVisibility(View.INVISIBLE);
                lastUpd.setVisibility(View.INVISIBLE);
                break;

            default:
        }
    }

    public void parseJSONWeather(String loc){

        final String city = loc.toLowerCase().replace(' ', '+');

        new Thread(new Runnable() {
            @Override
            public void run() {
             try {
                 JSONObject wx = httpHelper.getWeatherData(city);
                 if (wx != null) {
                    addWxInfo(wx);
                    wind = wx.getJSONObject("wind");
                    main = wx.getJSONObject("main");
                    weather = wx.getJSONArray("weather");
                    weatherinfo = weather.getJSONObject(0);
                    sys = wx.getJSONObject("sys");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run(){
                            try {
                                temperature = main.getInt("temp");
                                tempTxt.setText(temperature + " °C");
                                windSpdTxt.setText(wind.getString("speed") + " m/s");
                                if (wind.has("deg")) {
                                    windDirTxt.setText(wind.getInt("deg") + "°");
                                } else
                                    windDirTxt.setText("360°");
                                presTxt.setText(main.getString("pressure") + " mbar");
                                humTxt.setText(main.getString("humidity") + " %");
                                sunriseTxt.setText(parseUnixTime(sys.getLong("sunrise")));
                                sunsetTxt.setText(parseUnixTime(sys.getLong("sunset")));
                                weathercode = weatherinfo.getInt("id");

                                refBtn.setVisibility(View.INVISIBLE);
                                lastUpd.setVisibility(View.INVISIBLE);

                                String date = parseUnixDate(System.currentTimeMillis() / 1000L);
                                dayTxt.setText(date);

                                if(weathercode == 800)
                                    wxCond.setImageDrawable(getDrawable(R.drawable.sun));
                                else if(weathercode/100 == 8)
                                    wxCond.setImageDrawable(getDrawable(R.drawable.cloudy));
                                else if(weathercode/100 == 6)
                                    wxCond.setImageDrawable(getDrawable(R.drawable.snowflake));
                                else if(weathercode/100 == 5)
                                    wxCond.setImageDrawable(getDrawable(R.drawable.rain));
                                else if(weathercode/100 == 2)
                                    wxCond.setImageDrawable(getDrawable(R.drawable.storm));
                                else
                                    wxCond.setImageDrawable(getDrawable(R.drawable.sun));
                            }
                            catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                 }

             } catch (
                     IOException e) {
                 e.printStackTrace();
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         Log.d("DETAILS", "Unable to download data for " + city);
                         Toast.makeText(DetailsActivity.this, "Ne mogu da skinem vremensku prognozu za " + city, Toast.LENGTH_LONG).show();

                     }
                 });
             } catch (
                     JSONException e) {
                 e.printStackTrace();
             }
         }
        }).start();
    }

    public void printLatestEntryDate(){
        int latestWx = 0;
        int temp = 0, cond = 800;
        String pres = "", humi = "", sunr = "", suns = "", wspd = "", wdir = "", date;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(WxProvider.CONTENT_URI, null, WxHelper.COLUMN_CITY_NAME + "=?", new String[]{loc}, null);

        if(cursor.getCount() < 1){
            parseJSONWeather(loc);
            return;
        }
        for(cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()){
            if(cursor.getInt(cursor.getColumnIndex(WxHelper.COLUMN_DATE_TIME)) > latestWx) {
                latestWx = cursor.getInt(cursor.getColumnIndex(WxHelper.COLUMN_DATE_TIME));
                temperature = cursor.getInt(cursor.getColumnIndex(WxHelper.COLUMN_TEMPERATURE));
                cond = cursor.getInt(cursor.getColumnIndex(WxHelper.COLUMN_CONDITION));
                pres = cursor.getString(cursor.getColumnIndex(WxHelper.COLUMN_PRESSURE));
                humi = cursor.getString(cursor.getColumnIndex(WxHelper.COLUMN_HUMIDITY));
                sunr = cursor.getString(cursor.getColumnIndex(WxHelper.COLUMN_SUNRISE));
                suns = cursor.getString(cursor.getColumnIndex(WxHelper.COLUMN_SUNSET));
                wspd = cursor.getString(cursor.getColumnIndex(WxHelper.COLUMN_WIND_SPEED));
                wdir = cursor.getString(cursor.getColumnIndex(WxHelper.COLUMN_WIND_DIR));
            }
        }

        tempTxt.setText(temp + " °C");
        windSpdTxt.setText(wspd + " m/s");
        windDirTxt.setText(wdir + "°");
        presTxt.setText(pres + " mbar");
        humTxt.setText(humi + " %");
        sunriseTxt.setText(sunr);
        sunsetTxt.setText(suns);

        if(cond == 800)
            wxCond.setImageDrawable(getDrawable(R.drawable.sun));
        else if(cond/100 == 8)
            wxCond.setImageDrawable(getDrawable(R.drawable.cloudy));
        else if(cond/100 == 6)
            wxCond.setImageDrawable(getDrawable(R.drawable.snowflake));
        else if(cond/100 == 5)
            wxCond.setImageDrawable(getDrawable(R.drawable.rain));
        else if(cond/100 == 2)
            wxCond.setImageDrawable(getDrawable(R.drawable.storm));
        else
            wxCond.setImageDrawable(getDrawable(R.drawable.sun));

        date = parseUnixDate((long)latestWx);
        dayTxt.setText(date);
        if(isToday((long)latestWx)){
            refBtn.setVisibility(View.INVISIBLE);
            lastUpd.setVisibility(View.INVISIBLE);
        }
        else{
            refBtn.setVisibility(View.VISIBLE);
            lastUpd.setVisibility(View.VISIBLE);
        }

    }

    public String parseUnixTime(long unixTime){
        Date date = new Date(unixTime*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        String formattedTime = sdf.format(date);
        return formattedTime;
    }

    public boolean isToday(long unixTime){
        if (unixTime/(60*60*24) == System.currentTimeMillis() / 1000L /(24*3600))
            return true;
        return false;
    }

    public String parseUnixDate(long unixTime){
        Date date = new Date(unixTime*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd. MM.");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        String formattedTime = sdf.format(date);
        return formattedTime;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:
                if(temperatureF!=-1)
                    temperature = conv.FahrenheitToCelsius(temperatureF);
                tempTxt.setText(temperature + " °C");
                break;
            case 1:
                temperatureF = conv.CelsiusToFahrenheit(temperature);
                tempTxt.setText(temperatureF + " °F");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
