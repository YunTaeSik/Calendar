package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;

import java.util.Calendar;

/**
 * Created by sky87 on 2016-10-31.
 */
public class MouthlyBaseAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder viewHolder;
    private Calendar calendar;
    private int count;

    public MouthlyBaseAdapter(Context context, Calendar calendar) {
        this.context = context;
        this.calendar = calendar;
    }

    @Override
    public int getCount() {
        count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)  //해당 월 총 일수 구하기
                + calendar.get(Calendar.DAY_OF_WEEK) - 1;   //시작 요일 찾기 1일위치를 찾는거므로 비어있는칸은 -1을 해줘야함
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.mouthly_gridview, parent, false);

        viewHolder = new ViewHolder();
        viewHolder.grid_text = (TextView) convertView.findViewById(R.id.grid_text);
        viewHolder.grid_text.setText(String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)));
        calendar.getMinimalDaysInFirstWeek();
        String cal_text = String.valueOf(this.calendar.get(Calendar.YEAR)) + "년"
                + String.valueOf(this.calendar.get(Calendar.MONTH) + 1) + "월";
        if (calendar.get(Calendar.DAY_OF_WEEK) - 1 > position) {  //첫날 전까지 빈칸 처리
            viewHolder.grid_text.setText(String.valueOf(""));
         /*   Calendar cal = Calendar.getInstance();
            cal.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            cal.add(Calendar.DATE, -1);
            int a = cal.get(Calendar.WEEK_OF_MONTH);
            viewHolder.grid_text.setText(String.valueOf(a));*/
        } else {
            viewHolder.grid_text.setText(String.valueOf(position + 1 - (calendar.get(Calendar.DAY_OF_WEEK) - 1))); //position은 0부터시작하므로 +1 필요 그후 첫날전까지 빼줌
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView grid_text;
    }
}
