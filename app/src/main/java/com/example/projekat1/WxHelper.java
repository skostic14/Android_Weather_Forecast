package com.example.projekat1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WxHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "weather.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "weather";
    public static final String COLUMN_DATE_TIME = "DateTime";
    public static final String COLUMN_CITY_NAME = "City";
    public static final String COLUMN_TEMPERATURE = "Temperature";
    public static final String COLUMN_PRESSURE = "Pressure";
    public static final String COLUMN_HUMIDITY = "Humidity";
    public static final String COLUMN_SUNRISE = "Sunrise";
    public static final String COLUMN_SUNSET = "Sunset";
    public static final String COLUMN_WIND_SPEED = "WindSpeed";
    public static final String COLUMN_WIND_DIR = "WindDir";
    public static final String COLUMN_CONDITION = "Condition";

    public WxHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_DATE_TIME + " INTEGER, " +
                COLUMN_CITY_NAME + " TEXT, " +
                COLUMN_TEMPERATURE + " INTEGER, " +
                COLUMN_PRESSURE + " TEXT, " +
                COLUMN_HUMIDITY + " TEXT, " +
                COLUMN_SUNRISE + " TEXT, " +
                COLUMN_SUNSET + " TEXT, " +
                COLUMN_CONDITION + " INTEGER, " +
                COLUMN_WIND_SPEED + " TEXT, " +
                COLUMN_WIND_DIR + " TEXT);" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
