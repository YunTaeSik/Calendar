package com.funnytoday.project.calendar.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.adapter.WeeklyPagerAdapter;
import com.funnytoday.project.calendar.util.Contact;

public class WeekFragment extends Fragment implements ViewPager.OnPageChangeListener{
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
        currentposition = Contact.VIEWPAGER_CURRENT;
        weekly_viewpager = (ViewPager) view.findViewById(R.id.weekly_viewpager);
        weeklyPagerAdapter = new WeeklyPagerAdapter(getContext());
        weekly_viewpager.setAdapter(weeklyPagerAdapter);
        weekly_viewpager.setCurrentItem(Contact.VIEWPAGER_CURRENT);
        weekly_viewpager.setOnPageChangeListener(this);
        return view;
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
}
