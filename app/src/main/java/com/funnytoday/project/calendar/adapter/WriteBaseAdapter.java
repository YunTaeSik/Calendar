package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.function.WriteActivity;
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
        return images.size() + 1;
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
        viewHolder.write_list_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("test", "afterTextChanged" + position);
                WriteActivity.content.add(position, viewHolder.write_list_edit.getText().toString());
            }
        });
        if (position > 0) {
            Picasso.with(context).load(Uri.parse(images.get(position - 1))).into(viewHolder.write_list_image);
        }
        try {
            viewHolder.write_list_edit.setText(WriteActivity.content.get(position));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView write_list_image;
        private EditText write_list_edit;
    }
}
