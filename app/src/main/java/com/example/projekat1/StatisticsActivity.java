package com.example.projekat1;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView monLab, tueLab, wedLab, thuLab, friLab, satLab, sunLab, locLab;
    private TextView monTemp, tueTemp, wedTemp, thuTemp, friTemp, satTemp, sunTemp;
    private TextView monPres, tuePres, wedPres, thuPres, friPres, satPres, sunPres;
    private TextView monHumi, tueHumi, wedHumi, thuHumi, friHumi, satHumi, sunHumi;
    private TextView minTemp, minTempDay, maxTemp, maxTempDay;
    private ImageButton hot, cold;
    public static String city;
    public static int mode;
    public static String dayNames[] = {"Ponedeljak", "Utorak", "Sreda", "Četvrtak", "Petak", "Subota", "Nedelja"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        monLab = findViewById(R.id.monLabel);
        tueLab = findViewById(R.id.tueLabel);
        wedLab = findViewById(R.id.wedLabel);
        thuLab = findViewById(R.id.thuLabel);
        friLab = findViewById(R.id.friLabel);
        satLab = findViewById(R.id.satLabel);
        sunLab = findViewById(R.id.sunLabel);
        locLab = findViewById(R.id.statLoc);

        monPres = findViewById(R.id.monPresTxt);
        tuePres = findViewById(R.id.tuePresTxt);
        wedPres = findViewById(R.id.wedPresTxt);
        thuPres = findViewById(R.id.thuPresTxt);
        friPres = findViewById(R.id.friPresTxt);
        satPres = findViewById(R.id.satPresTxt);
        sunPres = findViewById(R.id.sunPresTxt);

        monTemp = findViewById(R.id.monTempTxt);
        tueTemp = findViewById(R.id.tueTempTxt);
        wedTemp = findViewById(R.id.wedTempTxt);
        thuTemp = findViewById(R.id.thuTempTxt);
        friTemp = findViewById(R.id.friTempTxt);
        satTemp = findViewById(R.id.satTempTxt);
        sunTemp = findViewById(R.id.sunTempTxt);

        monHumi = findViewById(R.id.monHumiTxt);
        tueHumi = findViewById(R.id.tueHumiTxt);
        wedHumi = findViewById(R.id.wedHumiTxt);
        thuHumi = findViewById(R.id.thuHumiTxt);
        friHumi = findViewById(R.id.friHumiTxt);
        satHumi = findViewById(R.id.satHumiTxt);
        sunHumi = findViewById(R.id.sunHumiTxt);

        minTemp = findViewById(R.id.minTempTemp);
        maxTemp = findViewById(R.id.maxTempTemp);
        minTempDay = findViewById(R.id.minTempDay);
        maxTempDay = findViewById(R.id.maxTempDay);

        hot = findViewById(R.id.hotBtn);
        cold = findViewById(R.id.coldBtn);
        mode = 0;

        Intent in = getIntent();
        Bundle args = in.getExtras();
        city = args.getString("loc");
        locLab.setText(city);

        hot.setOnClickListener(this);
        cold.setOnClickListener(this);

        Log.d("STATISTICS", "Opened the Statistics activity for "+ city);

        markDay();
        getWeahterFromDB();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.hotBtn:
                if(mode != 2) {
                    mode = 2;
                    getWeahterFromDB();
                }
                else{
                    mode = 0;
                    getWeahterFromDB();
                }
                break;

            case R.id.coldBtn:
                if(mode != 1) {
                    mode = 1;
                    getWeahterFromDB();
                }
                else{
                    mode = 0;
                    getWeahterFromDB();
                }
                break;
            default:

        }
    }

    public void markDay(){

        Calendar today = Calendar.getInstance();

        monLab.setTypeface(null, Typeface.NORMAL);
        tueLab.setTypeface(null, Typeface.NORMAL);
        wedLab.setTypeface(null, Typeface.NORMAL);
        thuLab.setTypeface(null, Typeface.NORMAL);
        friLab.setTypeface(null, Typeface.NORMAL);
        satLab.setTypeface(null, Typeface.NORMAL);
        sunLab.setTypeface(null, Typeface.NORMAL);

        int day = today.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                monLab.setTypeface(null, Typeface.BOLD);
                break;
            case Calendar.TUESDAY:
                tueLab.setTypeface(null, Typeface.BOLD);
                break;
            case Calendar.WEDNESDAY:
                wedLab.setTypeface(null, Typeface.BOLD);
                break;
            case Calendar.THURSDAY:
                thuLab.setTypeface(null, Typeface.BOLD);
                break;
            case Calendar.FRIDAY:
                friLab.setTypeface(null, Typeface.BOLD);
                break;
            case Calendar.SATURDAY:
                satLab.setTypeface(null, Typeface.BOLD);
                break;
            case Calendar.SUNDAY:
                sunLab.setTypeface(null, Typeface.BOLD);
                break;

            default:
        }
    }

    public void clearTexts(){
        monTemp.setText("");
        monPres.setText("");
        monHumi.setText("");

        tueTemp.setText("");
        tuePres.setText("");
        tueHumi.setText("");

        wedTemp.setText("");
        wedPres.setText("");
        wedHumi.setText("");

        thuTemp.setText("");
        thuPres.setText("");
        thuHumi.setText("");

        friTemp.setText("");
        friPres.setText("");
        friHumi.setText("");

        satTemp.setText("");
        satPres.setText("");
        satHumi.setText("");

        sunTemp.setText("");
        sunPres.setText("");
        sunHumi.setText("");

        Log.d("STATISTICS", "Cleared textViews");
    }

    public void getWeahterFromDB(){
        String pres, humi;
        int temp, tempmin = 100, tempmax = 0, unixTime, unixMin=0, unixMax=0;
        ContentResolver resolver = getContentResolver();
        String[] projection = {WxHelper.COLUMN_DATE_TIME, WxHelper.COLUMN_TEMPERATURE, WxHelper.COLUMN_PRESSURE, WxHelper.COLUMN_HUMIDITY};
        Log.d("STATISTICS", "Trying to get weather from database");
        Cursor cursor = null;

        switch(mode) {
            case 0:
                cursor = resolver.query(WxProvider.CONTENT_URI, projection,
                    WxHelper.COLUMN_CITY_NAME + "=?", new String[]{city}, null);
                break;
            case 1:
                cursor = resolver.query(WxProvider.CONTENT_URI, projection,
                        WxHelper.COLUMN_CITY_NAME + "=? AND " + WxHelper.COLUMN_TEMPERATURE +"<?", new String[]{city, Integer.toString(10)}, null);
                break;
            case 2:
                cursor = resolver.query(WxProvider.CONTENT_URI, projection,
                        WxHelper.COLUMN_CITY_NAME + "=? AND " + WxHelper.COLUMN_TEMPERATURE +">?", new String[]{city, Integer.toString(10)}, null);

        }
        Log.d("STATISTICS", "Got "+cursor.getCount() +" entries from database");
        if(cursor.getCount() < 0)
            return;
        clearTexts();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            temp = cursor.getInt(cursor.getColumnIndex(WxHelper.COLUMN_TEMPERATURE));
            pres = cursor.getString(cursor.getColumnIndex(WxHelper.COLUMN_PRESSURE));
            humi = cursor.getString(cursor.getColumnIndex(WxHelper.COLUMN_HUMIDITY));
            unixTime = cursor.getInt(cursor.getColumnIndex(WxHelper.COLUMN_DATE_TIME));

            if(temp > tempmax) {
                tempmax = temp;
                unixMax = unixTime;
            }
            if(temp < tempmin){
                tempmin = temp;
                unixMin = unixTime;
            }

            switch(getUnixDay((long)unixTime)) {
                case 1:
                    monTemp.setText(temp + " °C");
                    monPres.setText(pres + " mbar");
                    monHumi.setText(humi + "%");
                    break;

                case 2:
                    tueTemp.setText(temp + " °C");
                    tuePres.setText(pres + " mbar");
                    tueHumi.setText(humi + "%");
                    break;

                case 3:
                    wedTemp.setText(temp + " °C");
                    wedPres.setText(pres + " mbar");
                    wedHumi.setText(humi + "%");
                    break;

                case 4:
                    thuTemp.setText(temp + " °C");
                    thuPres.setText(pres + " mbar");
                    thuHumi.setText(humi + "%");
                    break;

                case 5:
                    friTemp.setText(temp + " °C");
                    friPres.setText(pres + " mbar");
                    friHumi.setText(humi + "%");
                    break;

                case 6:
                    satTemp.setText(temp + " °C");
                    satPres.setText(pres + " mbar");
                    satHumi.setText(humi + "%");
                    break;

                case 7:
                    sunTemp.setText(temp + " °C");
                    sunPres.setText(pres + " mbar");
                    sunHumi.setText(humi + "%");
                    break;

                default:
                    Log.d("STATISTICS", "Error parsing day from database");

            }
        }
        if(mode == 0) {
         findAllDays(tempmin, true);
         findAllDays(tempmax, false);
        }

        minTemp.setText(tempmin + " °C");
        maxTemp.setText(tempmax + " °C");
        cursor.close();
    }

    public int getUnixDay(long unixTime){
        Date date = new Date(unixTime*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("u");
        String formattedTime = sdf.format(date);
        return Integer.parseInt(formattedTime);
    }

    public void findAllDays(int temp, boolean isMin){
        boolean days[] = {false, false, false, false, false, false, false, false};
        String setTxt = "";

        int unixTime;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(WxProvider.CONTENT_URI, new String[]{WxHelper.COLUMN_DATE_TIME},WxHelper.COLUMN_CITY_NAME + "=? AND " + WxHelper.COLUMN_TEMPERATURE + "=?", new String[]{city, Integer.toString(temp)}, null);
        if(cursor.getCount() < 0)
            return;
        Log.d("STATISTICS", "Got " + cursor.getCount() + " matching temperatures");
        for(cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()){
            unixTime = cursor.getInt(cursor.getColumnIndex(WxHelper.COLUMN_DATE_TIME));
            days[getUnixDay((long)unixTime) - 1] = true;
        }


        for(int i=0; i<7; i++){
            if(days[i])
                setTxt += dayNames[i] + " \n";
        }

        Log.d("STATISTICS", "Trying to set string: " + setTxt);
        if(isMin)
            minTempDay.setText(setTxt);
        else
            maxTempDay.setText(setTxt);
    }

}
