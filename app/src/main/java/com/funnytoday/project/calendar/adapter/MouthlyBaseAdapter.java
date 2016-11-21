package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.db.DBManager;
import com.funnytoday.project.calendar.util.Contact;

import java.util.Calendar;

/**
 * Created by sky87 on 2016-10-31.
 */
public class MouthlyBaseAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder viewHolder;
    private Calendar calendar;
    private int count;
    private Cursor cursor;
    private DBManager dbManager;
    private String Day;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.mouthly_gridview, parent, false);

        viewHolder = new ViewHolder();
        viewHolder.grid_text = (TextView) convertView.findViewById(R.id.grid_text);
        viewHolder.write_circle = (ImageView) convertView.findViewById(R.id.write_circle);
        viewHolder.write_count_text = (TextView) convertView.findViewById(R.id.write_count_text);

        if (calendar.get(Calendar.DAY_OF_WEEK) - 1 > position) {  //첫날 전까지 빈칸 처리
            viewHolder.grid_text.setText(String.valueOf(""));
        } else {
            Day = String.valueOf(position + 1 - (calendar.get(Calendar.DAY_OF_WEEK) - 1)); //position은 0부터시작하므로 +1 필요 그후 첫날전까지 빼줌
            viewHolder.grid_text.setText(Day);
            viewHolder.grid_text.setBackground(context.getResources().getDrawable(R.drawable.day_background));
            if (viewHolder.grid_text.getText().toString() != null) {
                viewHolder.grid_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Contact.WRITE_CLICK);
                        intent.putExtra(Contact.YEAR, (calendar.get(Calendar.YEAR)));
                        intent.putExtra(Contact.MONTH, (calendar.get(Calendar.MONTH) + 1));
                        intent.putExtra(Contact.DAY, (position + 1 - (calendar.get(Calendar.DAY_OF_WEEK) - 1)));
                        context.sendBroadcast(intent);
                    }
                });
                try {
                   /* AsyncTask asyncTask1 = asyncTask;
                    asyncTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, position + 1 - (calendar.get(Calendar.DAY_OF_WEEK) - 1));*/
                } catch (IllegalStateException e) {

                }
                //asyncTask.execute();
            }
        }

        if (position % 7 == 0) {
            viewHolder.grid_text.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else if (position % 7 == 6) {
            viewHolder.grid_text.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            viewHolder.grid_text.setTextColor(context.getResources().getColor(R.color.balck));
        }
        //Picasso.with(context).load(R.drawable.write_circle_background_two).fit().into(viewHolder.write_circle);

        return convertView;
    }

    private class ViewHolder {
        private TextView grid_text;
        private ImageView write_circle;
        private TextView write_count_text;
    }

    private AsyncTask asyncTask = new AsyncTask<Integer, Integer, Integer>() {
        @Override
        protected Integer doInBackground(Integer... integers) {
            int test = Integer.parseInt(integers.toString());
            try {
                dbManager = new DBManager(context, "Write", null, 1);
                SQLiteDatabase redadb = dbManager.getReadableDatabase();
                String table_name = String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH) + 1) + test;
                cursor = redadb.rawQuery("select * from '" + table_name + "'", null);
            } catch (SQLiteException e) {

            }
            if (cursor.getCount() > 0) {
                return cursor.getCount();
            } else {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            int test = Integer.parseInt(integer.toString());
            Log.e("test", String.valueOf(test));
        }
        /*   @Override
        protected Object doInBackground(Object[] objects) {
            int test = objects;
            try {
                dbManager = new DBManager(context, "Write", null, 1);
                SQLiteDatabase redadb = dbManager.getReadableDatabase();
                String table_name = String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH) + 1) + Day;
                cursor = redadb.rawQuery("select * from '" + table_name + "'", null);
            } catch (SQLiteException e) {

            }
            if (cursor.getCount() > 0) {
                return cursor.getCount();
            } else {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            int a = (int) o;
            Log.e("onPostExecute", String.valueOf(a));
        *//*    if (cursor_m.getCount() > 0) {
                Picasso.with(context).load(R.drawable.write_circle_background).fit().into(viewHolder.write_circle);
                viewHolder.write_count_text.setText("X " + String.valueOf(cursor_m.getCount()));
            }
            cursor_m.close();*//*
            cursor.close();
            dbManager.close();
        }*/
    };
}
