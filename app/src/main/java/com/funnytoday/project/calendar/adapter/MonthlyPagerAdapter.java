package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.util.Contact;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static com.funnytoday.project.calendar.R.id.calendar_text_year;


/**
 * Created by YunTaeSik on 2016-09-11.
 */
public class MonthlyPagerAdapter extends PagerAdapter implements View.OnClickListener {
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


        viewHolder.calendar_text_layout = (RelativeLayout) viewpager.findViewById(R.id.calendar_text_layout);
        viewHolder.monthly_grid = (GridView) viewpager.findViewById(R.id.monthly_grid);
        viewHolder.calendar_text_year = (TextView) viewpager.findViewById(calendar_text_year);
        viewHolder.calendar_text_mouth = (TextView) viewpager.findViewById(R.id.calendar_text_mouth);
        viewHolder.left_image = (ImageView) viewpager.findViewById(R.id.left_image);
        viewHolder.right_image = (ImageView) viewpager.findViewById(R.id.right_image);

        calendar = getCalendar(position);
        viewHolder.calendar_text_year.setText(String.valueOf(this.calendar.get(Calendar.YEAR)));
        viewHolder.calendar_text_mouth.setText(String.valueOf(this.calendar.get(Calendar.MONTH) + 1));

        Picasso.with(context).load(R.drawable.left_arrow).fit().into(viewHolder.left_image);
        Picasso.with(context).load(R.drawable.right_arrow).fit().into(viewHolder.right_image);

        viewHolder.left_image.setOnClickListener(this);
        viewHolder.right_image.setOnClickListener(this);
        viewHolder.calendar_text_layout.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_image:
                context.sendBroadcast(new Intent(Contact.viewpager_left));
                break;
            case R.id.right_image:
                context.sendBroadcast(new Intent(Contact.viewpager_right));
                break;
       /*     case R.id.calendar_text_layout:
                Intent intent = new Intent(context, CalendarSelectDialog.class);
                intent.putExtra(Contact.YEAR, viewHolder.calendar_text_year.getText().toString());
                intent.putExtra(Contact.MONTH, viewHolder.calendar_text_mouth.getText().toString());
                context.startActivity(intent);
                break;*/
        }
    }

    private class ViewHolder {
        private RelativeLayout calendar_text_layout;
        private GridView monthly_grid;
        private TextView calendar_text_year;
        private TextView calendar_text_mouth;
        private ImageView left_image, right_image;
    }

    private Calendar getCalendar(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, (-Contact.VIEWPAGER_CURRENT) + 10);  //왜 +10을 해야하는지는 모르겠음..
        calendar.set(Calendar.DATE, 1);  //1일 초기화 첫요일 구하기위해 필요함
        calendar.add(Calendar.MONTH, position);
        return calendar;
    }

    @Override

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
