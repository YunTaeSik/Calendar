package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.util.Contact;

/**
 * Created by YunTaeSik on 2016-09-11.
 */
public class MonthlyPagerAdapter extends PagerAdapter {
    private View viewpager;
    private Context context;
    private ViewHolder viewHolder;
    private MouthlyBaseAdapter mouthlyBaseAdapter;


    public MonthlyPagerAdapter(Context context) {
        this.context = context;
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
    public Object instantiateItem(ViewGroup container, final int position) {
        viewpager = LayoutInflater.from(context).inflate(R.layout.monthly_viewpager, null);

        viewHolder = new ViewHolder();
        mouthlyBaseAdapter = new MouthlyBaseAdapter(context);

        viewHolder.monthly_grid = (GridView) viewpager.findViewById(R.id.monthly_grid);
        viewHolder.monthly_grid.setAdapter(mouthlyBaseAdapter);
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
        private GridView monthly_grid;
    }
}
