package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.db.DBManager;
import com.funnytoday.project.calendar.util.Contact;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import static com.funnytoday.project.calendar.R.id.calendar_text_year;

/**
 * Created by YunTaeSik on 2016-12-02.
 */
public class DayPagerAdapter extends PagerAdapter implements View.OnClickListener {
    private View viewpager;
    private Context context;
    private ViewHolder viewHolder;
    private Calendar calendar;
    private WriteRecyAdapter writeRecyAdapter;
    private RecyclerView.LayoutManager R_layoutManager;
    private DBManager dbManager;
    private SQLiteDatabase redadb;
    ArrayList starttime = new ArrayList();
    ArrayList endtime = new ArrayList();
    ArrayList title = new ArrayList();
    ArrayList jsonarray = new ArrayList();


    public DayPagerAdapter(Context context) {
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
        viewpager = LayoutInflater.from(context).inflate(R.layout.day_viewpager, null);
        viewHolder = new ViewHolder();


        viewHolder.calendar_text_layout = (RelativeLayout) viewpager.findViewById(R.id.calendar_text_layout);
        viewHolder.calendar_text_year = (TextView) viewpager.findViewById(calendar_text_year);
        viewHolder.calendar_text_mouth = (TextView) viewpager.findViewById(R.id.calendar_text_mouth);
        viewHolder.calendar_text_date = (TextView) viewpager.findViewById(R.id.calendar_text_date);
        viewHolder.left_image = (ImageView) viewpager.findViewById(R.id.left_image);
        viewHolder.right_image = (ImageView) viewpager.findViewById(R.id.right_image);
        viewHolder.write_recycleview = (RecyclerView) viewpager.findViewById(R.id.write_recycleview);
        viewHolder.empty_text = (TextView) viewpager.findViewById(R.id.empty_text);

        calendar = getCalendar(position);

        viewHolder.calendar_text_year.setText(String.valueOf(this.calendar.get(Calendar.YEAR)));
        viewHolder.calendar_text_mouth.setText(String.valueOf(this.calendar.get(Calendar.MONTH) + 1));
        viewHolder.calendar_text_date.setText(String.valueOf(this.calendar.get(Calendar.DATE)));

        Picasso.with(context).load(R.drawable.left_arrow).fit().into(viewHolder.left_image);
        Picasso.with(context).load(R.drawable.right_arrow).fit().into(viewHolder.right_image);

        viewHolder.left_image.setOnClickListener(this);
        viewHolder.right_image.setOnClickListener(this);
        viewHolder.calendar_text_layout.setOnClickListener(this);
        dbManager = new DBManager(context, "Write", null, 1);
        redadb = dbManager.getReadableDatabase();
        String table_name = viewHolder.calendar_text_year.getText().toString() + viewHolder.calendar_text_mouth.getText().toString()
                + viewHolder.calendar_text_date.getText().toString();
        try {
            Cursor cursor = redadb.query("'" + table_name + "'", null, null, null, null, null, null);
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                starttime.add(cursor.getString(1));
                endtime.add(cursor.getString(2));
                title.add(cursor.getString(3));
                jsonarray.add(cursor.getString(4));
            }
            viewHolder.empty_text.setVisibility(View.GONE);
            viewHolder.write_recycleview.setVisibility(View.VISIBLE);
            viewHolder.write_recycleview.setHasFixedSize(true);
            R_layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            viewHolder.write_recycleview.setLayoutManager(R_layoutManager);
            writeRecyAdapter = new WriteRecyAdapter(context, starttime, endtime, title, jsonarray, calendar); //값이 있을때
            viewHolder.write_recycleview.setAdapter(writeRecyAdapter);
        } catch (SQLiteException e) {
            viewHolder.empty_text.setVisibility(View.VISIBLE);
            viewHolder.write_recycleview.setVisibility(View.GONE);
        }
  /*     */
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
        }
    }

    private class ViewHolder {
        private RelativeLayout calendar_text_layout;
        private TextView calendar_text_year;
        private TextView calendar_text_mouth;
        private TextView calendar_text_date;
        private TextView empty_text;
        private ImageView left_image, right_image;
        private RecyclerView write_recycleview;
    }

    private Calendar getCalendar(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, (-Contact.VIEWPAGER_CURRENT) + 2);  //왜 +10을 해야하는지는 모르겠음..
        calendar.add(Calendar.DATE, position);
        return calendar;
    }

    @Override

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

