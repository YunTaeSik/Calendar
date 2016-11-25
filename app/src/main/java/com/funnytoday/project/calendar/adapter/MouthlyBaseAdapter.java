package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObservable;
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
import com.funnytoday.project.calendar.util.Contact;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    private SQLiteDatabase redadb;
    private String Day;
    private ArrayList arrayList;
    DataSetObservable dataSetObservable = new DataSetObservable();

    public MouthlyBaseAdapter(Context context, Calendar calendar, ArrayList arrayList) {
        this.context = context;
        this.calendar = calendar;
        this.arrayList = arrayList;
    }

    public MouthlyBaseAdapter(Context context, Calendar calendar) {
        this.context = context;
        this.calendar = calendar;
    }

    public MouthlyBaseAdapter() {
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
                AsyncTaskObject asyncTaskObject = new AsyncTaskObject(calendar, viewHolder, position + 1 - (calendar.get(Calendar.DAY_OF_WEEK) - 1));
                SearchDB searchDB = new SearchDB();
                searchDB.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, asyncTaskObject);
            }
        }

        if (position % 7 == 0) {
            viewHolder.grid_text.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else if (position % 7 == 6) {
            viewHolder.grid_text.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            viewHolder.grid_text.setTextColor(context.getResources().getColor(R.color.balck));
        }

        return convertView;
    }

    private class ViewHolder {
        private TextView grid_text;
        private ImageView write_circle;
        private TextView write_count_text;
    }

    private class AsyncTaskObject { //어싱크테스크 객체 전달을위한 클래스
        private Calendar Acalendar;
        private ViewHolder AviewHolder;
        private int ADAY;

        private AsyncTaskObject(Calendar calendar, ViewHolder viewHolder, int day) {
            this.Acalendar = calendar;
            this.AviewHolder = viewHolder;
            this.ADAY = day;
        }
    }

    private class SearchDB extends AsyncTask {
        Calendar c;
        ViewHolder v;
        int i;

        @Override
        protected Object doInBackground(Object[] objects) {
            AsyncTaskObject asyncTaskObject = (AsyncTaskObject) objects[0];
            c = asyncTaskObject.Acalendar;
            v = asyncTaskObject.AviewHolder;
            i = asyncTaskObject.ADAY;
            try {
                dbManager = new DBManager(context, "Write", null, 1);
                redadb = dbManager.getReadableDatabase();
                String table_name = String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH) + 1) + String.valueOf(i);
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

