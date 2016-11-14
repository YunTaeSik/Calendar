package com.funnytoday.project.calendar.function;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.adapter.WriteBaseAdapter;
import com.funnytoday.project.calendar.util.Contact;

import java.util.ArrayList;
import java.util.Calendar;

public class WriteActivity extends AppCompatActivity {
    private int REQ_CODE_PICK_PICTURE = 1;

    private ActionBar actionBar;
    private TextView month_text;
    private TextView date_text;
    private TextView year_text;
    private EditText title_edit;
    private EditText content_edit;
    private ListView write_list;

    private int Year;
    private int Month;
    private int Day;
    private int DAY_OF_WEEK;

    private WriteBaseAdapter writeBaseAdapter;
    private ArrayList<String> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        getWindow().setStatusBarColor(getResources().getColor(R.color.theme));
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.write_actionbar_text));
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme)));
        actionBar.setHomeButtonEnabled(true);

        month_text = (TextView) findViewById(R.id.month_text);
        date_text = (TextView) findViewById(R.id.date_text);
        year_text = (TextView) findViewById(R.id.year_text);
        title_edit = (EditText) findViewById(R.id.title_edit);
        content_edit = (EditText) findViewById(R.id.content_edit);
        write_list = (ListView) findViewById(R.id.write_list);

        writeBaseAdapter = new WriteBaseAdapter(this, images);
        write_list.setAdapter(writeBaseAdapter);

        getCalrendar();
        month_text.setText(String.valueOf(Month));
        date_text.setText(String.valueOf(Day));
        year_text.setText(String.valueOf(Year) + "\n" + getDayOfWeek(DAY_OF_WEEK));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.write_menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gallery_btn:
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("video/*, images/*");
                startActivityForResult(i, REQ_CODE_PICK_PICTURE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_PICK_PICTURE) {
            images.add(data.getDataString());
            Log.e("test", data.getDataString());
            // writeBaseAdapter = new WriteBaseAdapter(this, images);
            writeBaseAdapter.notifyDataSetChanged();
        }
    }

    private void getCalrendar() {
        Year = getIntent().getIntExtra(Contact.YEAR, 0);
        Month = getIntent().getIntExtra(Contact.MONTH, 0);
        Day = getIntent().getIntExtra(Contact.DAY, 0);
        DAY_OF_WEEK = getIntent().getIntExtra(Contact.DAY_OF_WEEK, 0);
        if (Year == 0) { //넘겨오는 값이 없으면 오늘 날짜로
            Calendar calendar = Calendar.getInstance();
            Year = calendar.get(Calendar.YEAR);
            Month = calendar.get(Calendar.MONTH) + 1;
            Day = calendar.get(Calendar.DATE);
            DAY_OF_WEEK = calendar.get(Calendar.DAY_OF_WEEK);
        }
    }

    private String getDayOfWeek(int dayofweek) {
        switch (dayofweek) {
            case 1:
                return "일요일";
            case 2:
                return "월요일";
            case 3:
                return "화요일";
            case 4:
                return "수요일";
            case 5:
                return "목요일";
            case 6:
                return "금요일";
            case 7:
                return "토요일";
        }
        return "";
    }
}
