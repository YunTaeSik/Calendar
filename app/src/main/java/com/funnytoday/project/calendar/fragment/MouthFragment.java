package com.funnytoday.project.calendar.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.adapter.MonthlyPagerAdapter;
import com.funnytoday.project.calendar.util.Contact;

public class MouthFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager monthly_viewpager;
    private MonthlyPagerAdapter monthlyPagerAdapter;


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
        monthlyPagerAdapter = new MonthlyPagerAdapter(getContext());
        monthly_viewpager = (ViewPager) view.findViewById(R.id.monthly_viewpager);

        monthly_viewpager.setAdapter(monthlyPagerAdapter);
        monthly_viewpager.setOffscreenPageLimit(10);
        monthly_viewpager.setCurrentItem(Contact.VIEWPAGER_CURRENT);
        return view;
    }


}
