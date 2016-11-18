package com.funnytoday.project.calendar.function;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.dialog.DatePickerDialog;
import com.funnytoday.project.calendar.dialog.TimePickerDialog;
import com.funnytoday.project.calendar.util.Contact;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    private int REQ_CODE_PICK_PICTURE = 1;
    private int Time_PIRKER_START = 2;
    private int Time_PIRKER_END = 3;
    private int DATE_PIRKER = 4;

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

    // private WriteBaseAdapter writeBaseAdapter;
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> content = new ArrayList<>();

    private LinearLayout test;

    private TextView start_time_text;
    private TextView end_time_text;
    private int start = 0;
    private int end = 0;

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
        //  content_edit = (EditText) findViewById(R.id.content_edit);
        //write_list = (ListView) findViewById(R.id.write_list);
        start_time_text = (TextView) findViewById(R.id.start_time_text);
        end_time_text = (TextView) findViewById(R.id.end_time_text);

        test = (LinearLayout) findViewById(R.id.test);


     /*   writeBaseAdapter = new WriteBaseAdapter(this, images);
        write_list.setAdapter(writeBaseAdapter);
*/
        getCalrendar();
        month_text.setText(String.valueOf(Month));
        date_text.setText(String.valueOf(Day));
        year_text.setText(String.valueOf(Year) + "\n" + getDayOfWeek(DAY_OF_WEEK));

        start_time_text.setOnClickListener(this);
        end_time_text.setOnClickListener(this);
        month_text.setOnClickListener(this);
        date_text.setOnClickListener(this);

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
            case R.id.finish_btn:
                if (end > start) {
                    finish();
                    saveContent();
                } else {
                    Toast.makeText(this, "종료시간이 시작시간보다 느려야합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.month_text:
                startActivityForResult(new Intent(this, DatePickerDialog.class), DATE_PIRKER);
                break;
            case R.id.date_text:
                startActivityForResult(new Intent(this, DatePickerDialog.class), DATE_PIRKER);
                break;
            case R.id.start_time_text:
                startActivityForResult(new Intent(this, TimePickerDialog.class), Time_PIRKER_START);
                break;
            case R.id.end_time_text:
                startActivityForResult(new Intent(this, TimePickerDialog.class), Time_PIRKER_END);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_PICK_PICTURE) { //갤러리 선택
            ImageView imageView = new ImageView(this);
            EditText editText = new EditText(this);
            Picasso.with(this).load(Uri.parse(data.getDataString())).into(imageView);
            images.add(data.getDataString()); //저장
            test.addView(imageView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            test.addView(editText);
        } else if (requestCode == Time_PIRKER_START) { //시작 시간 선택
            start = resultCode;
            start_time_text.setText(String.valueOf(start) + "\n" + "Hour");
        } else if (requestCode == Time_PIRKER_END) {  //종료시간 선택
            end = resultCode;
            end_time_text.setText(String.valueOf(end) + "\n" + "Hour");
        } else if (requestCode == DATE_PIRKER) {  //데이터 피커 선택할때
            try {
                Year = data.getIntExtra("0", 0);
                Month = data.getIntExtra("1", 0);
                Day = data.getIntExtra("2", 0);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Year, Month, Day);
                DAY_OF_WEEK = calendar.get(Calendar.DAY_OF_WEEK);
                month_text.setText(String.valueOf(Month + 1));
                date_text.setText(String.valueOf(Day));
                year_text.setText(String.valueOf(Year) + "\n" + getDayOfWeek(DAY_OF_WEEK));
            } catch (NullPointerException e) {
            }

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

    private void saveContent() {

        for (int i = 1; i < test.getChildCount(); i += 2) {  //홀수 포문
            EditText editText = (EditText) test.getChildAt(i);
            content.add(editText.getText().toString());
        }

        for (int i = 0; i < images.size(); i++) {
            Log.e("images", images.get(i));
        }
        for (int i = 0; i < content.size(); i++) {
            Log.e("content", content.get(i));
        }
    }

}
