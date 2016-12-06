package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;

import java.util.Calendar;

/**
 * Created by Note on 2016-11-12.
 */
public class WeeklyBaseAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder viewHolder;
    private Calendar calendar;
    int jumpDay = 1;

    public WeeklyBaseAdapter(Context context, Calendar calender) {
        this.context = context;
        this.calendar = calender;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertview = layoutInflater.inflate(R.layout.weekly_gridview, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.weekly_grid_text = (TextView) convertview.findViewById(R.id.weekly_grid_text);
        viewHolder.weekly_write_circle = (ImageView) convertview.findViewById(R.id.weekly_write_circle);

   /*     if (calendar.get(Calendar.DAY_OF_WEEK) -1 > position) {  //첫날 전까지 빈칸 처리
            viewHolder.weekly_grid_text.setText(String.valueOf(""));
        } else {
            viewHolder.weekly_grid_text.setText(String.valueOf(position + 1 - (calendar.get(Calendar.DAY_OF_WEEK) - 1))); //position은 0부터시작하므로 +1 필요 그후 첫날전까지 빼줌
            viewHolder.weekly_grid_text.setBackground(context.getResources().getDrawable(R.drawable.day_background));
        }
*/

        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (startDay + position <= endDay) {
            viewHolder.weekly_grid_text.setText(String.valueOf(startDay + position));
        } else {
            viewHolder.weekly_grid_text.setText(String.valueOf(jumpDay));
            jumpDay++;
        }
        Log.e("test", String.valueOf(startDay) + String.valueOf(endDay));
        if (position % 7 == 0) {
            viewHolder.weekly_grid_text.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else if (position % 7 == 6) {
            viewHolder.weekly_grid_text.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            viewHolder.weekly_grid_text.setTextColor(context.getResources().getColor(R.color.balck));
        }
        if (viewHolder.weekly_grid_text.getText().toString() != null) {
            viewHolder.weekly_grid_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }


        return convertview;
    }

    private class ViewHolder {
        private TextView weekly_grid_text;
        private ImageView weekly_write_circle;
    }
}
