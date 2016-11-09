package com.funnytoday.project.calendar.main;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.fragment.DayFragment;
import com.funnytoday.project.calendar.fragment.MouthFragment;
import com.funnytoday.project.calendar.fragment.WeekFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.bar_title_M));
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme)));
        initFragment();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                break;
            case R.id.weekly_btn:
                fragmentTransaction.replace(R.id.main_fragment, new WeekFragment());
                fragmentTransaction.commit();

                actionBar.setTitle(getString(R.string.bar_title_W));
                break;
            case R.id.day_btn:
                fragmentTransaction.replace(R.id.main_fragment, new DayFragment());
                fragmentTransaction.commit();

                actionBar.setTitle(getString(R.string.bar_title_D));
                break;
            case R.id.write_btn:
                /*fragmentTransaction.replace(R.id.main_fragment, new MouthFragment());
                fragmentTransaction.commit();*/

                actionBar.setTitle(getString(R.string.write_actionbar_text));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment, new MouthFragment());
        fragmentTransaction.commit();
    }
}
