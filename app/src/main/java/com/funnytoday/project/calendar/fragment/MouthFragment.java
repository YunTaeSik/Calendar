package com.funnytoday.project.calendar.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.adapter.MonthlyPagerAdapter;
import com.funnytoday.project.calendar.util.Contact;

import java.util.Calendar;

public class MouthFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager monthly_viewpager;
    private MonthlyPagerAdapter monthlyPagerAdapter;

    private Calendar calendar;


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
        monthly_viewpager = (ViewPager) view.findViewById(R.id.monthly_viewpager);
        calendar = getCalendar();
        //  calendar.add(Calendar.MONTH, monthly_viewpager.getCurrentItem());
        monthlyPagerAdapter = new MonthlyPagerAdapter(getContext(), calendar);
        monthly_viewpager.setAdapter(monthlyPagerAdapter);
        monthly_viewpager.setOffscreenPageLimit(3);
        monthly_viewpager.setCurrentItem(Contact.VIEWPAGER_CURRENT);
        monthly_viewpager.setOnPageChangeListener(this);
        return view;
    }

    private Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, -499);
        String test = String.valueOf(cal.get(Calendar.YEAR)) + "년" + String.valueOf(cal.get(Calendar.MONTH)) + "월"
                + String.valueOf(cal.get(Calendar.DATE)) + "일";
        Log.e("calenday", test);
        return cal;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("test", String.valueOf(position));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, -499);
        cal.add(Calendar.MONTH, monthly_viewpager.getCurrentItem());

        String test = String.valueOf(cal.get(Calendar.YEAR)) + "년" + String.valueOf(cal.get(Calendar.MONTH)) + "월"
                + String.valueOf(cal.get(Calendar.DATE)) + "일";

        Log.e("calenday", test);
        monthlyPagerAdapter = new MonthlyPagerAdapter(getContext(), cal);
        monthlyPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.e("test", String.valueOf(state));
    }
}
