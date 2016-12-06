package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.db.DBManager;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * Created by Note on 2016-11-12.
 */
public class WeeklyBaseAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder viewHolder;
    private Calendar calendar;
    private int jumpDay = 1;
    private DBManager dbManager;
    private SQLiteDatabase redadb;
    private Cursor cursor;

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
    public View getView(final int position, View convertview, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertview = layoutInflater.inflate(R.layout.weekly_gridview, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.weekly_grid_text = (TextView) convertview.findViewById(R.id.weekly_grid_text);
        viewHolder.write_count_text = (TextView) convertview.findViewById(R.id.write_count_text);
        viewHolder.write_circle = (ImageView) convertview.findViewById(R.id.write_circle);


        final int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (startDay + position <= endDay) {
            viewHolder.weekly_grid_text.setText(String.valueOf(startDay + position));
            AsyncTaskObject asyncTaskObject = new AsyncTaskObject(calendar, viewHolder, Integer.parseInt(viewHolder.weekly_grid_text.getText().toString()), 0);
            SearchDB searchDB = new SearchDB();
            searchDB.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, asyncTaskObject);
        } else {
            viewHolder.weekly_grid_text.setText(String.valueOf(jumpDay));
            AsyncTaskObject asyncTaskObject = new AsyncTaskObject(calendar, viewHolder, Integer.parseInt(viewHolder.weekly_grid_text.getText().toString()), 1);
            SearchDB searchDB = new SearchDB();
            searchDB.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, asyncTaskObject);
            jumpDay++;
        }
        if (position % 7 == 0) {
            viewHolder.weekly_grid_text.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else if (position % 7 == 6) {
            viewHolder.weekly_grid_text.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            viewHolder.weekly_grid_text.setTextColor(context.getResources().getColor(R.color.balck));
        }

        return convertview;
    }

    private class ViewHolder {
        private TextView weekly_grid_text;
        private ImageView write_circle;
        private TextView write_count_text;
    }

    private class AsyncTaskObject { //어싱크테스크 객체 전달을위한 클래스
        private Calendar Acalendar;
        private ViewHolder AviewHolder;
        private int ADAY;
        private int AJump; //0 점프X , 1 점프0

        private AsyncTaskObject(Calendar calendar, ViewHolder viewHolder, int day, int jump) {
            this.Acalendar = calendar;
            this.AviewHolder = viewHolder;
            this.ADAY = day;
            this.AJump = jump;
        }
    }

    private class SearchDB extends AsyncTask {
        private Calendar c;
        private ViewHolder v;
        private int i;
        private int jump;

        @Override
        protected Object doInBackground(Object[] objects) {
            AsyncTaskObject asyncTaskObject = (AsyncTaskObject) objects[0];
            c = asyncTaskObject.Acalendar;
            v = asyncTaskObject.AviewHolder;
            i = asyncTaskObject.ADAY;
            jump = asyncTaskObject.AJump;

            try {
                dbManager = new DBManager(context, "Write", null, 1);
                redadb = dbManager.getReadableDatabase();
                String table_name = null;
                if (jump == 0) { //기본
                    table_name = String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(c.get(Calendar.MONTH) + 1) + String.valueOf(i);
                } else {//점프했을때
                    c.add(Calendar.MONTH, 1);
                    table_name = String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(c.get(Calendar.MONTH) + 1) + String.valueOf(i);
                }
                cursor = redadb.query("'" + table_name + "'", null, null, null, null, null, null);
                int count = cursor.getCount();
                cursor.close();
                redadb.close();
                return count;
            } catch (Exception e) {
                return 0;
            }
        }

        protected void onPostExecute(Object o) {
            int count = (int) o;
            if (count > 0) {
                Picasso.with(context).load(R.drawable.write_circle_background).fit().into(v.write_circle);
                v.write_count_text.setText("X " + count);
            }
        }
    }
}
