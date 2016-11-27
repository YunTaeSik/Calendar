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
        viewHolder.calendar_text = (TextView) viewpager.findViewById(R.id.weekly_calendar_text);
        viewHolder.left_image = (ImageView) viewpager.findViewById(R.id.weekly_left_image);
        viewHolder.right_image = (ImageView) viewpager.findViewById(R.id.weekly_right_image);

        calendar = getCalendar(position);
        String cal_text = String.valueOf(this.calendar.get(Calendar.YEAR)) + "년"
                + String.valueOf(this.calendar.get(Calendar.MONTH) + 1) + "월"
                + String.valueOf(this.calendar.get(Calendar.WEEK_OF_MONTH)) + "주차";
        viewHolder.calendar_text.setText(cal_text);

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
            calendar.set(Calendar.WEEK_OF_MONTH, -Contact.VIEWPAGER_CURRENT);  //왜 +10을 해야하는지는 모르겠음..
            calendar.set(Calendar.DATE, 1);  //1일 초기화 첫요일 구하기위해 필요함
            calendar.add(Calendar.WEEK_OF_MONTH, position);

            return calendar;
        }

        private class ViewHolder {
            private GridView weekly_grid;
            private TextView calendar_text;
        private ImageView left_image, right_image;
    }
}
