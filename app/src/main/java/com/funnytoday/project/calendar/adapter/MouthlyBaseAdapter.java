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

    public MouthlyBaseAdapter(Context context, Calendar calendar) {
        this.context = context;
        this.calendar =calendar;
    }

    @Override
    public int getCount() {
        return 31;
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
        viewHolder.grid_text.setText(String.valueOf(position));

        return convertView;
    }

    private class ViewHolder {
        private TextView grid_text;
    }

}
