package com.funnytoday.project.calendar.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.dialog.FinishDialog;
import com.funnytoday.project.calendar.fragment.DayFragment;
import com.funnytoday.project.calendar.fragment.MouthFragment;
import com.funnytoday.project.calendar.fragment.WeekFragment;
import com.funnytoday.project.calendar.util.Contact;

public class MainActivity extends AppCompatActivity {
    private FragmentTransaction fragmentTransaction;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();
        initFragment();
        initBroadCast();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.monthly_btn:
                fragmentTransaction.replace(R.id.main_frame, new MouthFragment());
                fragmentTransaction.commit();
                actionBar.setTitle("월별보기");
                break;
            case R.id.weekly_btn:
                fragmentTransaction.replace(R.id.main_frame, new WeekFragment());
                fragmentTransaction.commit();
                actionBar.setTitle("주별보기");
                break;
            case R.id.day_btn:
                fragmentTransaction.replace(R.id.main_frame, new DayFragment());
                fragmentTransaction.commit();
                actionBar.setTitle("일별보기");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, FinishDialog.class);
        startActivity(intent);
    }


    private void initActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme)));
        actionBar.setTitle("월별보기");
    }

    private void initFragment() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_frame, new MouthFragment());
        fragmentTransaction.commit();
    }

    private void initBroadCast() {
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
