package com.funnytoday.project.calendar.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.adapter.MonthlyPagerAdapter;
import com.funnytoday.project.calendar.adapter.WriteRecyAdapter;
import com.funnytoday.project.calendar.db.DBManager;
import com.funnytoday.project.calendar.function.WriteActivity;
import com.funnytoday.project.calendar.util.Contact;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class MouthFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager monthly_viewpager;
    private MonthlyPagerAdapter monthlyPagerAdapter;
    private int currentposition;

    private TextView null_calendar_text;
    private ImageView close_image;
    private LinearLayout write_list_layout;
    private TextView write_calendar_text;
    private ImageView write_add_btn;
    private RecyclerView write_recycleview;
    private WriteRecyAdapter writeRecyAdapter;
    private RecyclerView.LayoutManager R_layoutManager;

    private int Year;
    private int Month;
    private int Day;
    private int DAY_OF_WEEK;

    public static String getWriteVisible = "GONE";

    private DBManager dbManager;
    private SQLiteDatabase redadb;
    private Cursor cursor;

    public MouthFragment() {
    }


    public static MouthFragment newInstance(String param1, String param2) {
        MouthFragment fragment = new MouthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mouth, container, false);

        setIntentFilter();

        currentposition = Contact.VIEWPAGER_CURRENT;
        monthly_viewpager = (ViewPager) view.findViewById(R.id.monthly_viewpager);
        monthlyPagerAdapter = new MonthlyPagerAdapter(getContext());
        monthly_viewpager.setAdapter(monthlyPagerAdapter);
        monthly_viewpager.setCurrentItem(Contact.VIEWPAGER_CURRENT);
        monthly_viewpager.setOffscreenPageLimit(2);
        monthly_viewpager.setOnPageChangeListener(this);

        close_image = (ImageView) view.findViewById(R.id.close_image);
        write_list_layout = (LinearLayout) view.findViewById(R.id.write_list_layout);
        write_calendar_text = (TextView) view.findViewById(R.id.write_calendar_text);
        write_add_btn = (ImageView) view.findViewById(R.id.write_add_btn);
        null_calendar_text = (TextView) view.findViewById(R.id.null_calendar_text);

        write_recycleview = (RecyclerView) view.findViewById(R.id.write_recycleview);

        Picasso.with(getContext()).load(R.drawable.close_image).fit().into(close_image);
        Picasso.with(getContext()).load(R.drawable.write_add_image).fit().into(write_add_btn);
        close_image.setOnClickListener(this);
        write_add_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        WriteListSetVisible(0);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_image:
                WriteListSetVisible(0);
                break;
            case R.id.write_add_btn:
                WriteListSetVisible(0);
                Intent intent = new Intent(getContext(), WriteActivity.class);
                intent.putExtra(Contact.YEAR, Year);
                intent.putExtra(Contact.MONTH, Month);
                intent.putExtra(Contact.DAY, Day);
                intent.putExtra(Contact.DAY_OF_WEEK, DAY_OF_WEEK);
                startActivity(intent);
                break;
        }
    }

    private void setIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contact.viewpager_left);
        intentFilter.addAction(Contact.viewpager_right);
        intentFilter.addAction(Contact.WRITE_CLICK);
        intentFilter.addAction(Contact.SAVE_DB);
        intentFilter.addAction(Contact.WRITE_LIST_GONE);
        getContext().registerReceiver(broadcastReceiver, intentFilter);
    }

    private void WriteListSetVisible(int VISIBLE) {
        if (VISIBLE == 1) {
            if (write_list_layout.getVisibility() != View.VISIBLE) {
                Animation animation_v = AnimationUtils.loadAnimation(getContext(), R.anim.write_animation_v);
                write_list_layout.startAnimation(animation_v);
                write_list_layout.setVisibility(View.VISIBLE);
                getWriteVisible = "VISIBLE";
            }
        } else if (VISIBLE == 0) {
            if (write_list_layout.getVisibility() != View.GONE) {
                Animation animation_g = AnimationUtils.loadAnimation(getContext(), R.anim.write_animation_g);
                write_list_layout.startAnimation(animation_g);
                write_list_layout.setVisibility(View.GONE);
                getWriteVisible = "GONE";
            }
        }
    }

    private String getDayOfWeek(int dayofweek) {
        switch (dayofweek) {
            case 1:
                return "일요일";
            case 2:
                return "월요일";
            case 3:
                return "화요일";
            case 4:
                return "수요일";
            case 5:
                return "목요일";
            case 6:
                return "금요일";
            case 7:
                return "토요일";
        }
        return "";
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Contact.viewpager_left)) {
                monthly_viewpager.setCurrentItem(monthly_viewpager.getCurrentItem() - 1);
            } else if (intent.getAction().equals(Contact.viewpager_right)) {
                monthly_viewpager.setCurrentItem(monthly_viewpager.getCurrentItem() + 1);
            } else if (intent.getAction().equals(Contact.WRITE_CLICK)) {
                Year = intent.getIntExtra(Contact.YEAR, 0);
                Month = intent.getIntExtra(Contact.MONTH, 0);
                Day = intent.getIntExtra(Contact.DAY, 0);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Year, Month - 1, Day);
                DAY_OF_WEEK = calendar.get(Calendar.DAY_OF_WEEK);
                write_calendar_text.setText(Year + "." + Month + "." + Day + " " + getDayOfWeek(DAY_OF_WEEK));
                WriteListSetVisible(1);
                AsyncTask asyncTask = new asyncTask();
                asyncTask.execute();
                // setWriteListView(Year, Month, Day);
            } else if (intent.getAction().equals(Contact.SAVE_DB)) {
                monthlyPagerAdapter.notifyDataSetChanged();
            } else if (intent.getAction().equals(Contact.WRITE_LIST_GONE)) {
                WriteListSetVisible(0);
            }
        }
    };

    private void setWriteListView(int Year, int Month, int Day) {
     /*   dbManager = new DBManager(getContext(), "Write", null, 1);
        redadb = dbManager.getReadableDatabase();
        String table_name = String.valueOf(Year) + String.valueOf(Month) + String.valueOf(Day);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Year);
        calendar.set(Calendar.MONTH, Month - 1);
        calendar.set(Calendar.DATE, Day);

        ArrayList starttime = new ArrayList();
        ArrayList endtime = new ArrayList();
        ArrayList title = new ArrayList();
        ArrayList jsonarray = new ArrayList();

        try {
            cursor = redadb.query("'" + table_name + "'", null, null, null, null, null, null);
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                starttime.add(cursor.getString(1));
                endtime.add(cursor.getString(2));
                title.add(cursor.getString(3));
                jsonarray.add(cursor.getString(4));
            }
            cursor.close();
            write_recycleview.setVisibility(View.VISIBLE);
            null_calendar_text.setVisibility(View.GONE);
        } catch (SQLiteException e) {
            write_recycleview.setVisibility(View.GONE);
            null_calendar_text.setVisibility(View.VISIBLE);
        }
        redadb.close();
        write_recycleview.setHasFixedSize(true);
        R_layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        write_recycleview.setLayoutManager(R_layoutManager);
        writeRecyAdapter = new WriteRecyAdapter(getContext(), starttime, endtime, title, jsonarray, calendar); //값이 있을때
        write_recycleview.setAdapter(writeRecyAdapter);*/
    }

    private class asyncTask extends AsyncTask {
        ArrayList starttime = new ArrayList();
        ArrayList endtime = new ArrayList();
        ArrayList title = new ArrayList();
        ArrayList jsonarray = new ArrayList();
        Calendar calendar;

        @Override
        protected Object doInBackground(Object[] objects) {
            dbManager = new DBManager(getContext(), "Write", null, 1);
            redadb = dbManager.getReadableDatabase();
            String table_name = String.valueOf(Year) + String.valueOf(Month) + String.valueOf(Day);

            calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Year);
            calendar.set(Calendar.MONTH, Month - 1);
            calendar.set(Calendar.DATE, Day);
            try {
                cursor = redadb.query("'" + table_name + "'", null, null, null, null, null, null);
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    starttime.add(cursor.getString(1));
                    endtime.add(cursor.getString(2));
                    title.add(cursor.getString(3));
                    jsonarray.add(cursor.getString(4));
                }
            } catch (SQLiteException e) {
            }
            cursor.close();
            redadb.close();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (starttime.size() > 0) {
                write_recycleview.setVisibility(View.VISIBLE);
                null_calendar_text.setVisibility(View.GONE);
            } else {
                write_recycleview.setVisibility(View.GONE);
                null_calendar_text.setVisibility(View.VISIBLE);
            }
            write_recycleview.setHasFixedSize(true);
            R_layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            write_recycleview.setLayoutManager(R_layoutManager);
            writeRecyAdapter = new WriteRecyAdapter(getContext(), starttime, endtime, title, jsonarray, calendar); //값이 있을때
            write_recycleview.setAdapter(writeRecyAdapter);
        }
    }

    ;

}
