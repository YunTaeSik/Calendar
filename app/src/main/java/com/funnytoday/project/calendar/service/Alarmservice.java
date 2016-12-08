package com.funnytoday.project.calendar.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.funnytoday.project.calendar.alarm.AlarmActivity;
import com.funnytoday.project.calendar.db.DBManager;
import com.funnytoday.project.calendar.function.WriteActivity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Note on 2016-11-27.
 */
public class Alarmservice extends Service {
    private String TAG = "Alarmservice";
    private WriteActivity wr;
    private DBManager dbManager;
    private SQLiteDatabase redadb;
    private Cursor cursor;
    private ArrayList<String> hourlist = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "Alarmservice start");
        getWriteDB();
        return super.onStartCommand(intent, flags, startId);

    }

    private void getWriteDB() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String date = String.valueOf(calendar.get(calendar.DATE));
        String tablename = year + month + date;
        dbManager = new DBManager(this, "Write", null, 1);
        redadb = dbManager.getReadableDatabase();
        try {
            cursor = redadb.query("'" + tablename + "'", null, null, null, null, null, null);
        } catch (SQLiteException e) {
        }
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                if (!hourlist.contains(cursor.getString(i))) {
                    hourlist.add(cursor.getString(i));
                }
            }
            cursor.close();
        }
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent Intent = new Intent(getApplicationContext(), AlarmActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, Intent, 0);
        am.cancel(pIntent);
        for (int i = 0; i < hourlist.size(); i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(hourlist.get(i)) - cal.get(Calendar.HOUR_OF_DAY));
            if (cal.getTimeInMillis() - calendar.getTimeInMillis() > 0) {
                // am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);
                // am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pIntent); //테스트용
                int time = (Integer.parseInt(hourlist.get(i)) - calendar.get(Calendar.HOUR_OF_DAY)) * 3600 * 1000;
                am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pIntent);
            }
            Log.e(TAG, String.valueOf(cal.getTimeInMillis() - calendar.getTimeInMillis()));
        }
    }
}