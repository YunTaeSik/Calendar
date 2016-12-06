package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.util.Contact;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * Created by Note on 2016-11-12.
 */
public class WeeklyPagerAdapter extends PagerAdapter implements View.OnClickListener {
    private Context context;
    private View viewpager;
    private ViewHolder viewHolder;
    private WeeklyBaseAdapter weeklyBaseAdapter;
    private Calendar calendar;

    public WeeklyPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Contact.VIEWPAGER_MAX;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        viewpager = LayoutInflater.from(context).inflate(R.layout.weekly_viewpager, null);
        viewHolder = new ViewHolder();

        viewHolder.weekly_grid = (GridView) viewpager.findViewById(R.id.weekly_grid);
        viewHolder.calendar_text_year = (TextView) viewpager.findViewById(R.id.calendar_text_year);
        viewHolder.calendar_text_mouth = (TextView) viewpager.findViewById(R.id.calendar_text_mouth);
        viewHolder.calendar_text_week = (TextView) viewpager.findViewById(R.id.calendar_text_week);
        viewHolder.left_image = (ImageView) viewpager.findViewById(R.id.weekly_left_image);
        viewHolder.right_image = (ImageView) viewpager.findViewById(R.id.weekly_right_image);

        calendar = getCalendar(position);
        viewHolder.calendar_text_year.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        viewHolder.calendar_text_mouth.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        viewHolder.calendar_text_week.setText(String.valueOf(calendar.get(Calendar.WEEK_OF_MONTH)) + " week");

        Picasso.with(context).load(R.drawable.left_arrow).fit().into(viewHolder.left_image);
        Picasso.with(context).load(R.drawable.right_arrow).fit().into(viewHolder.right_image);

        viewHolder.left_image.setOnClickListener(this);
        viewHolder.right_image.setOnClickListener(this);

        weeklyBaseAdapter = new WeeklyBaseAdapter(context, calendar);
        viewHolder.weekly_grid.setAdapter(weeklyBaseAdapter);

        container.addView(viewpager);


        return viewpager;
    }

    @Override
    public void onClick(View view) {

    }

    private Calendar getCalendar(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, -Contact.VIEWPAGER_CURRENT);  //왜 +10을 해야하는지는 모르겠음..
        calendar.add(Calendar.WEEK_OF_MONTH, position);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar;
    }

    private class ViewHolder {
        private GridView weekly_grid;
        private ImageView left_image, right_image;
        private TextView calendar_text_year;
        private TextView calendar_text_mouth;
        private TextView calendar_text_week;
    }
}
