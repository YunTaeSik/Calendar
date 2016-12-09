package com.funnytoday.project.calendar.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.dialog.FinishDialog;
import com.funnytoday.project.calendar.fragment.DayFragment;
import com.funnytoday.project.calendar.fragment.MouthFragment;
import com.funnytoday.project.calendar.fragment.WeekFragment;
import com.funnytoday.project.calendar.function.WriteActivity;
import com.funnytoday.project.calendar.service.Alarmservice;
import com.funnytoday.project.calendar.util.Contact;

import static com.funnytoday.project.calendar.fragment.MouthFragment.getWriteVisible;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.bar_title_M));
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme)));
        initFragment();
        setIntentFilter();
        startService(new Intent(this, Alarmservice.class));

        int permissionCheck_Read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck_Read == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            Log.e("권한", "있음");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onBackPressed() {
        if (getWriteVisible.equals("GONE")) {
            Intent intent = new Intent(this, FinishDialog.class);
            startActivity(intent);
        } else {
            sendBroadcast(new Intent(Contact.WRITE_LIST_GONE));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.monthly_btn:
                fragmentTransaction.replace(R.id.main_fragment, new MouthFragment());
                fragmentTransaction.commit();
                actionBar.setTitle(getString(R.string.bar_title_M));
                MouthFragment.getWriteVisible = "GONE";
                break;
            case R.id.weekly_btn:
                fragmentTransaction.replace(R.id.main_fragment, new WeekFragment());
                fragmentTransaction.commit();
                actionBar.setTitle(getString(R.string.bar_title_W));
                MouthFragment.getWriteVisible = "GONE";
                break;
            case R.id.day_btn:
                fragmentTransaction.replace(R.id.main_fragment, new DayFragment());
                fragmentTransaction.commit();
                actionBar.setTitle(getString(R.string.bar_title_D));
                MouthFragment.getWriteVisible = "GONE";
                break;
            case R.id.write_btn:
                Intent intent = new Intent(this, WriteActivity.class);
                startActivity(intent);
                MouthFragment.getWriteVisible = "GONE";
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        recreate();
                    } else {
                        finish();
                        Toast.makeText(getApplicationContext(), "권한을 허용을 하셔야 이용하실수 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                return;
        }
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment, new MouthFragment());
        fragmentTransaction.commit();
    }

    private void setIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contact.Real_Finish);
        registerReceiver(broadcastReceiver, intentFilter);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Contact.Real_Finish)) {
                finish();
            }
        }
    };
}
