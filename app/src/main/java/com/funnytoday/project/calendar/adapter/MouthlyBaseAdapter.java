package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.dialog.WriteDialog;

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
        viewHolder.write_circle = (ImageView) convertView.findViewById(R.id.write_circle);

        String cal_text = String.valueOf(this.calendar.get(Calendar.YEAR)) + "년"
                + String.valueOf(this.calendar.get(Calendar.MONTH) + 1) + "월";

        if (calendar.get(Calendar.DAY_OF_WEEK) - 1 > position) {  //첫날 전까지 빈칸 처리
            viewHolder.grid_text.setText(String.valueOf(""));
        } else {
            viewHolder.grid_text.setText(String.valueOf(position + 1 - (calendar.get(Calendar.DAY_OF_WEEK) - 1))); //position은 0부터시작하므로 +1 필요 그후 첫날전까지 빼줌
            viewHolder.grid_text.setBackground(context.getResources().getDrawable(R.drawable.day_background));
        }

        if (position % 7 == 0) {
            viewHolder.grid_text.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else if (position % 7 == 6) {
            viewHolder.grid_text.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            viewHolder.grid_text.setTextColor(context.getResources().getColor(R.color.balck));
        }
        if (viewHolder.grid_text.getText().toString() != null) {
            viewHolder.grid_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WriteDialog.class);
                    context.startActivity(intent);
                }
            });
        }
        //Picasso.with(context).load(R.drawable.write_circle_background).fit().into(viewHolder.write_circle);

        return convertView;
    }

    private class ViewHolder {
        private TextView grid_text;
        private ImageView write_circle;
    }
}
