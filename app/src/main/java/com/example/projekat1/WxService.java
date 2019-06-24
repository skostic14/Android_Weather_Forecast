package com.example.projekat1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class WxService extends Service {

    private String loc;
    private HttpHelper httpHelper;
    private WxRunnable wxRun;
    private IBinder binder;

    public WxService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        httpHelper = new HttpHelper();
        binder = new LocalBinder();
        wxRun = new WxRunnable();
        createNotificationChannel();
        wxRun.start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wxRun.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        loc = intent.getExtras().getString("location");
        return binder;
    }

    public class LocalBinder extends Binder {
        WxService getService() {
            return WxService.this;
        }
    }

    private class WxRunnable implements Runnable {
        private boolean mRun = true;
        private Handler mHandler;


        public WxRunnable() {
            mHandler = new Handler();
        }

        public void start() {
            mRun = true;
            mHandler.postDelayed(this, 3000L);
        }

        public void stop() {
            mRun = false;
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            if (!mRun)
                return;
            final String city = loc.toLowerCase().replace(' ', '+');
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject wx = httpHelper.getWeatherData(city);
                        if (wx != null) {
                            addWxInfo(wx);
                            showNotification(wx.getJSONObject("main").getInt("temp"), loc);
                        }

                    } catch (
                            IOException e) {
                        e.printStackTrace();
                    } catch (
                            JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


            Log.d("WxService", "Service parsing weather for " + loc);
            mHandler.postDelayed(this, 3000L);
        }
    }

    public void showNotification(int temp, String loc) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myChannel")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Vremenska prognoza")
                .setContentTitle("Temperatura za " + loc + " je osvežena")
                .setContentText("Trenutna temperatura: " + temp + " °C")
                .setSmallIcon(android.R.drawable.zoom_plate)
                .setContentInfo("info");

        NotificationManager notmgr = (NotificationManager) WxService.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notmgr.notify(1, builder.build());
        Log.d("WxService", "Notification shown");
    }

    public void addWxInfo(JSONObject wxinfo) {
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

    public String parseUnixTime(long unixTime) {
        Date date = new Date(unixTime * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        String formattedTime = sdf.format(date);
        return formattedTime;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notificationChannel";
            String description = "Moj notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("myChannel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
