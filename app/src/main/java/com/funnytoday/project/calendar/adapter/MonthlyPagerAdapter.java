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
    public Object instantiateItem(ViewGroup container, int position) {
        viewpager = LayoutInflater.from(context).inflate(R.layout.monthly_viewpager, null);
        viewHolder = new ViewHolder();

        viewHolder.monthly_grid = (GridView) viewpager.findViewById(R.id.monthly_grid);
        viewHolder.calendar_text = (TextView) viewpager.findViewById(R.id.calendar_text);

        calendar = getCalendar(position);
        String cal_text = String.valueOf(this.calendar.get(Calendar.YEAR)) + "년"
                + String.valueOf(this.calendar.get(Calendar.MONTH) + 1) + "월";
        viewHolder.calendar_text.setText(cal_text);


        mouthlyBaseAdapter = new MouthlyBaseAdapter(context, calendar);
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
        private TextView calendar_text;
    }

    private Calendar getCalendar(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, (-Contact.VIEWPAGER_CURRENT) + 10);  //왜 +10을 해야하는지는 모르겠음..
        calendar.set(Calendar.DATE, 1);  //1일 초기화 첫요일 구하기위해 필요함
        calendar.add(Calendar.MONTH, position);
        return calendar;
    }
}
