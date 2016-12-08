package com.funnytoday.project.calendar.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by User on 2016-03-29.
 */
public class AlarmServiceReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {                  //스마프폰이 부팅되었을때
            Log.e("intentaction", "ACTION_BOOT_COMPLETED");
            context.startService(new Intent(context, Alarmservice.class));                //서비스시작
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {           //패키지가 추가되었을때
            Log.e("intentaction", "ACTION_PACKAGE_ADDED");
            context.startService(new Intent(context, Alarmservice.class));
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {        //패키지가 대체되었을때
            Log.e("intentaction", "ACTION_PACKAGE_REPLACED");
            context.startService(new Intent(context, Alarmservice.class));
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_INSTALL)) {         //패키지가 깔렸을때
            Log.e("intentaction", "ACTION_PACKAGE_INSTALL");
            context.startService(new Intent(context, Alarmservice.class));
        }
    }
}
