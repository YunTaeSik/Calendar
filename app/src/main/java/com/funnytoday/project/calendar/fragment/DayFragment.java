package com.funnytoday.project.calendar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.adapter.DayPagerAdapter;
import com.funnytoday.project.calendar.function.WriteActivity;
import com.funnytoday.project.calendar.util.Contact;
import com.squareup.picasso.Picasso;

public class DayFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2 = "test";
    private ViewPager day_viewpager;
    private ImageView write_add_btn;
    private DayPagerAdapter dayPagerAdapter;

    public DayFragment() {
    }

    public static DayFragment newInstance(String param1, String param2) {
        DayFragment fragment = new DayFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        day_viewpager = (ViewPager) view.findViewById(R.id.day_viewpager);
        write_add_btn = (ImageView) view.findViewById(R.id.write_add_btn);
        Picasso.with(getContext()).load(R.drawable.write_add_image).fit().into(write_add_btn);
        write_add_btn.setOnClickListener(this);
        dayPagerAdapter = new DayPagerAdapter(getContext());
        day_viewpager.setAdapter(dayPagerAdapter);
        day_viewpager.setCurrentItem(Contact.VIEWPAGER_CURRENT);
        day_viewpager.setOffscreenPageLimit(2);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.write_add_btn:
                Intent intent = new Intent(getContext(), WriteActivity.class);
                startActivity(intent);
                break;

        }
    }
}
