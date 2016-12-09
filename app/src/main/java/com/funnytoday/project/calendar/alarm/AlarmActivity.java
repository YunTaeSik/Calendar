package com.funnytoday.project.calendar.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.funnytoday.project.calendar.R;

import java.io.IOException;

public class AlarmActivity extends Activity implements View.OnClickListener {
    private Button finish_btn;
    private Button realarm_btn;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alarm);
        finish_btn = (Button) findViewById(R.id.finish_btn);
        realarm_btn = (Button) findViewById(R.id.realarm_btn);
        finish_btn.setOnClickListener(this);
        realarm_btn.setOnClickListener(this);
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        player = new MediaPlayer();
        try {
            player.setDataSource(this, alert);
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (player.isPlaying()) {
            player.stop();
            player.release();
        }
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
            try {
                player.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            player.stop();
            player.release();
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish_btn:
                finish();
                break;
            case R.id.realarm_btn:
                finish();
                AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent Intent = new Intent(getApplicationContext(), AlarmActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, Intent, 0);
                int time = 5 * 60 * 1000;
                am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pIntent);
                break;
        }
    }
}
