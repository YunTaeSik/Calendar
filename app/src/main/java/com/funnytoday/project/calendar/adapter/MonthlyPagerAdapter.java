package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.util.Contact;

import java.util.Calendar;


/**
 * Created by YunTaeSik on 2016-09-11.
 */
public class MonthlyPagerAdapter extends PagerAdapter {
    private View viewpager;
    private Context context;
    private ViewHolder viewHolder;
    private MouthlyBaseAdapter mouthlyBaseAdapter;
    private Calendar calendar;


    public MonthlyPagerAdapter(Context context, Calendar calendar) {
        this.context = context;
        this.calendar = calendar;
    }

    @Override
    public int getCount() {
        return Contact.VIEWPAGER_MAX;
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        viewpager = LayoutInflater.from(context).inflate(R.layout.monthly_viewpager, null);

        viewHolder = new ViewHolder();
        mouthlyBaseAdapter = new MouthlyBaseAdapter(context, calendar);
        viewHolder.monthly_grid = (GridView) viewpager.findViewById(R.id.monthly_grid);
        viewHolder.calendar_text = (TextView) viewpager.findViewById(R.id.calendar_text);
        viewHolder.monthly_grid.setAdapter(mouthlyBaseAdapter);
        String calendar_text = String.valueOf(calendar.get(Calendar.YEAR)) + "년"
                + String.valueOf(calendar.get(Calendar.MONTH)) + "월";

        viewHolder.calendar_text.setText(calendar_text);
        container.addView(viewpager);
        return viewpager;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public float getPageWidth(int position) {
        return 1f;
    }

    private class ViewHolder {
         GridView monthly_grid;
        private TextView calendar_text;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
