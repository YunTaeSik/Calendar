package com.funnytoday.project.calendar.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.adapter.WeeklyPagerAdapter;
import com.funnytoday.project.calendar.util.Contact;

public class WeekFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager weekly_viewpager;
    private int currentposition;
    private WeeklyPagerAdapter weeklyPagerAdapter;


    public WeekFragment() {
    }

    public static WeekFragment newInstance(String param1, String param2) {
        WeekFragment fragment = new WeekFragment();
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
        View view = inflater.inflate(R.layout.fragment_week, container, false);

        setIntentFilter();

        currentposition = Contact.VIEWPAGER_CURRENT;
        weekly_viewpager = (ViewPager) view.findViewById(R.id.weekly_viewpager);
        weeklyPagerAdapter = new WeeklyPagerAdapter(getContext());
        weekly_viewpager.setAdapter(weeklyPagerAdapter);
        weekly_viewpager.setCurrentItem(Contact.VIEWPAGER_CURRENT);
        weekly_viewpager.setOnPageChangeListener(this);

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

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {

    }

    private void setIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contact.viewpager_left_w);
        intentFilter.addAction(Contact.viewpager_right_w);
        intentFilter.addAction(Contact.SAVE_DB);
        getContext().registerReceiver(broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Contact.viewpager_left_w)) {
                weekly_viewpager.setCurrentItem(weekly_viewpager.getCurrentItem() - 1);
            } else if (intent.getAction().equals(Contact.viewpager_right_w)) {
                weekly_viewpager.setCurrentItem(weekly_viewpager.getCurrentItem() + 1);
            } else if (intent.getAction().equals(Contact.SAVE_DB)) {
                weeklyPagerAdapter.notifyDataSetChanged();
            }
        }
    };


}
