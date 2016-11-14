package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.funnytoday.project.calendar.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by YunTaeSik on 2016-11-14.
 */
public class WriteBaseAdapter extends BaseAdapter {
    private ViewHolder viewHolder;
    private Context context;
    private ArrayList<String> images = new ArrayList<>();

    public WriteBaseAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.write_listview, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.write_list_image = (ImageView) convertView.findViewById(R.id.write_list_image);
        viewHolder.write_list_edit = (EditText) convertView.findViewById(R.id.write_list_edit);

        Picasso.with(context).load(images.get(position)).fit().into(viewHolder.write_list_image);
        return convertView;
    }

    private class ViewHolder {
        private ImageView write_list_image;
        private EditText write_list_edit;
    }
}
