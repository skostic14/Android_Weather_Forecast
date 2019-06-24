package com.example.projekat1;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    EditText locInput;
    Button btnAdd;
    String loc;
    CityAdapter cityAdapter;
    ListView cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.showBtn);
        locInput = (EditText) findViewById(R.id.locTxt);
        cityList = (ListView) findViewById(R.id.cityList);
        cityAdapter = new CityAdapter(this);
        cityList.setAdapter(cityAdapter);

        cityAdapter.addCity(new CityItem("Novi Sad"));
        cityAdapter.addCity(new CityItem("Zrenjanin"));
        cityAdapter.addCity(new CityItem("Sombor"));
        cityAdapter.addCity(new CityItem("Subotica"));

        btnAdd.setOnClickListener(this);
        cityList.setOnItemLongClickListener(this);

        setupDummyValues();
        refreshDB();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.showBtn:
                if(locInput.getText().toString().matches("")){
                    Toast.makeText(MainActivity.this, "Niste dobro uneli lokaciju", Toast.LENGTH_LONG).show();
                }
                else{
                    cityAdapter.addCity(new CityItem(locInput.getText().toString()));
                    locInput.setText("");
                }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(MainActivity.this, "Uklonjen grad iz liste", Toast.LENGTH_LONG).show();
        cityAdapter.removeCity(position);
        return false;
    }



    public void setupDummyValues(){
        ContentValues values = new ContentValues();

        ContentResolver res = getContentResolver();
        long time;

        //NOVI SAD - Petak 10:28 Temp: 12
        time = 1557484131L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Novi Sad");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "83");
        values.put(WxHelper.COLUMN_PRESSURE, "1014");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 18);
        Log.d("WxHelper", values.toString());
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //NOVI SAD - Subota 13:28 Temp: 24
        time = 1557581331L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Novi Sad");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "61");
        values.put(WxHelper.COLUMN_PRESSURE, "1012");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 24);
        Log.d("WxHelper", values.toString());
        res = getContentResolver();
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //NOVI SAD - Nedelja 13:43 Temp: 1
        time = 1557668631L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Novi Sad");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "10");
        values.put(WxHelper.COLUMN_PRESSURE, "1012");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 1);
        Log.d("WxHelper", values.toString());
        res = getContentResolver();
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //NOVI SAD - Ponedeljak 09:32 Temp: 1
        time = 1557739971L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Novi Sad");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "18");
        values.put(WxHelper.COLUMN_PRESSURE, "998");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 1);
        Log.d("WxHelper", values.toString());
        res = getContentResolver();
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //NOVI SAD - Utorak 20:21 Temp: 6
        time = 1557865311L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Novi Sad");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "42");
        values.put(WxHelper.COLUMN_PRESSURE, "1005");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 6);
        Log.d("WxHelper", values.toString());
        res = getContentResolver();
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //NOVI SAD - Utorak 20:31 Temp: 10
        time = 1557865911L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Novi Sad");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "71");
        values.put(WxHelper.COLUMN_PRESSURE, "1023");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 10);
        Log.d("WxHelper", values.toString());
        res = getContentResolver();
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //NOVI SAD - Sreda 20:30 Temp: 37 - Donglice na kvizu
        time = 1557952251L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Novi Sad");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "100");
        values.put(WxHelper.COLUMN_PRESSURE, "1023");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 37);
        Log.d("WxHelper", values.toString());
        res = getContentResolver();
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //NOVI SAD - Četvrtak 07:15 Temp: 21 - Donglice mamurne
        /*time = 1557990951L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Novi Sad");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "78");
        values.put(WxHelper.COLUMN_PRESSURE, "1023");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 10);
        values.put(WxHelper.COLUMN_TEMPERATURE, 21);
        Log.d("WxHelper", values.toString());
        res = getContentResolver();
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();*/

        //Zrenjanin - Petak 10:28 Temp: 12
        time = 1557484171L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Zrenjanin");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "61");
        values.put(WxHelper.COLUMN_PRESSURE, "1014");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 12);
        Log.d("WxHelper", values.toString());
        res = getContentResolver();
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //Zrenjanin - Subota 13:28 Temp: 24
        time = 1557581631L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Zrenjanin");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "31");
        values.put(WxHelper.COLUMN_PRESSURE, "1012");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 19);
        Log.d("WxHelper", values.toString());
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //Zrenjanin - Nedelja 13:43 Temp: 1
        time = 1557668831L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Zrenjanin");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "51");
        values.put(WxHelper.COLUMN_PRESSURE, "1014");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 11);
        Log.d("WxHelper", values.toString());
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //Zrenjanin - Ponedeljak 09:32 Temp: 11
        time = 1557740271L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Zrenjanin");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "58");
        values.put(WxHelper.COLUMN_PRESSURE, "1009");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 17);
        Log.d("WxHelper", values.toString());
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //Zrenjanin - Utorak 20:21 Temp: 6
        time = 1557865111L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Zrenjanin");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "72");
        values.put(WxHelper.COLUMN_PRESSURE, "1031");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 6);
        Log.d("WxHelper", values.toString());
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //Zrenjanin - Utorak 20:31 Temp: 10
        time = 1557865911L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Zrenjanin");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "71");
        values.put(WxHelper.COLUMN_PRESSURE, "1023");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 10);
        Log.d("WxHelper", values.toString());
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //Zrenjanin - Sreda 20:30 Temp: 8
        time = 1557952251L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Zrenjanin");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "81");
        values.put(WxHelper.COLUMN_PRESSURE, "1023");
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 5);
        values.put(WxHelper.COLUMN_TEMPERATURE, 8);
        Log.d("WxHelper", values.toString());
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

        //Zrenjanin - Četvrtak 07:15 Temp: 21
        time = 1557990951L;
        values.put(WxHelper.COLUMN_CITY_NAME, "Zrenjanin");
        values.put(WxHelper.COLUMN_DATE_TIME, (int) time);
        values.put(WxHelper.COLUMN_HUMIDITY, "66");
        values.put(WxHelper.COLUMN_PRESSURE, "1016");
        values.put(WxHelper.COLUMN_CONDITION, 800);
        values.put(WxHelper.COLUMN_SUNRISE, "05:15");
        values.put(WxHelper.COLUMN_SUNSET, "20:24");
        values.put(WxHelper.COLUMN_WIND_DIR, "182");
        values.put(WxHelper.COLUMN_WIND_SPEED, 10);
        values.put(WxHelper.COLUMN_TEMPERATURE, 23);
        Log.d("WxHelper", values.toString());
        res.insert(WxProvider.CONTENT_URI, values);
        values.clear();

    }

    public void refreshDB(){
        ContentResolver res = getContentResolver();
        int del = res.delete(WxProvider.CONTENT_URI, WxHelper.COLUMN_DATE_TIME + "<?", new String[]{Long.toString(getOneWeekAgo())});
        Log.d("WxProvider", "Deleted " + del + " outdated entries");
    }

    public int getOneWeekAgo(){
        long unixTime = System.currentTimeMillis() / 1000L;
        unixTime -= 60*60*24*7;
        return (int)unixTime;
    }

}
