package com.funnytoday.project.calendar.function;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.db.DBManager;
import com.funnytoday.project.calendar.dialog.DatePickerDialog;
import com.funnytoday.project.calendar.dialog.TimePickerDialog;
import com.funnytoday.project.calendar.util.Contact;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    private int Year;
    private int Month;
    private int Day;
    private int DAY_OF_WEEK;

    // private WriteBaseAdapter writeBaseAdapter;
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> content = new ArrayList<>();

    private LinearLayout write_addlayout;

    private TextView start_time_text;
    private TextView end_time_text;
    private int start = 0;
    private int end = 0;

    private Canvas canvas;
    private DBManager dbManager;
    private String jsonArray;

    private int add_count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        getWindow().setStatusBarColor(getResources().getColor(R.color.theme));
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.write_actionbar_text));
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme)));
        actionBar.setHomeButtonEnabled(true);

        canvas = new Canvas();

        month_text = (TextView) findViewById(R.id.month_text);
        date_text = (TextView) findViewById(R.id.date_text);
        year_text = (TextView) findViewById(R.id.year_text);
        title_edit = (EditText) findViewById(R.id.title_edit);
        content_edit = (EditText) findViewById(R.id.content_edit);
        //write_list = (ListView) findViewById(R.id.write_list);
        start_time_text = (TextView) findViewById(R.id.start_time_text);
        end_time_text = (TextView) findViewById(R.id.end_time_text);

        write_addlayout = (LinearLayout) findViewById(R.id.write_addlayout);

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
                //i.setType( "images/*");
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
            case R.id.delete_image_layout:
                Toast.makeText(this, String.valueOf(view.getTag()), Toast.LENGTH_SHORT).show();
                for (int i = 1; i < write_addlayout.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) write_addlayout.getChildAt(i);
                    RelativeLayout relativeLayout = (RelativeLayout) linearLayout.getChildAt(0);
                    EditText editText = (EditText) linearLayout.getChildAt(1);
                    RelativeLayout relativeLayout2 = (RelativeLayout) relativeLayout.getChildAt(1);
                    if (String.valueOf(view.getTag()).equals(String.valueOf(relativeLayout2.getTag()))) {
                        images.remove(i - 1);
                        write_addlayout.removeViewAt(i);
                        if (write_addlayout.getChildCount() - 1 <= 1) {
                            content_edit.append("\n" + editText.getText().toString());
                        } else {
                            LinearLayout addlinear = (LinearLayout) write_addlayout.getChildAt(i - 1);  //전위치 에 edittext 추가
                            EditText addedit = (EditText) addlinear.getChildAt(1);
                            addedit.append("\n" + editText.getText().toString()); //
                        }
                    }
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_PICK_PICTURE) { //갤러리 선택
            try {
                add_count++;

                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout itemview = (LinearLayout) layoutInflater.inflate(R.layout.write_item_view, null);
                ImageView imageView = (ImageView) itemview.findViewById(R.id.write_item_image);
                ImageView delete = (ImageView) itemview.findViewById(R.id.item_image_delete);
                RelativeLayout delete_image_layout = (RelativeLayout) itemview.findViewById(R.id.delete_image_layout);

                Picasso.with(this).load(R.drawable.delete_image).into(delete);
                delete_image_layout.setOnClickListener(this);


                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                if (canvas.getMaximumBitmapHeight() / 8 < image_bitmap.getHeight()
                        || canvas.getMaximumBitmapWidth() / 8 < image_bitmap.getWidth()) {
                    Picasso.with(this).load(Uri.parse(data.getDataString())).resize(300, 1000).into(imageView);
                } else {
                    Picasso.with(this).load(Uri.parse(data.getDataString())).into(imageView);
                }

                images.add(data.getDataString()); //images data list 추가
                write_addlayout.addView(itemview);

                int indexValue = write_addlayout.indexOfChild(itemview);
                itemview.setTag(Integer.toString(indexValue));

                delete_image_layout.setTag(Integer.toString(indexValue)); //delete tag 추가

            } catch (NullPointerException e) {
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        content.add(content_edit.getText().toString());
        for (int i = 1; i < write_addlayout.getChildCount(); i++) {  //
            LinearLayout layout = (LinearLayout) write_addlayout.getChildAt(i);
            EditText editText = (EditText) layout.getChildAt(1);
            content.add(editText.getText().toString());
        }
        asyncTask.execute();
    }

    private AsyncTask asyncTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                JSONObject json = new JSONObject();
                json.put("images", new JSONArray(images));
                json.put("content", new JSONArray(content));
                jsonArray = json.toString();
                Log.e("json", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                dbManager = new DBManager(getApplicationContext(), "Write", null, 1);
                SQLiteDatabase db = dbManager.getWritableDatabase();
                String table_name = Year + month_text.getText().toString() + Day;
                db.execSQL("CREATE TABLE if not exists '" + table_name + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, start TEXT, end TEXT, title TEXT, json TEXT);");
                ContentValues values = new ContentValues();
                values.put("start", start_time_text.getText().toString());
                values.put("end", end_time_text.getText().toString());
                values.put("title", title_edit.getText().toString());
                values.put("json", jsonArray);
                db.insert("'" + table_name + "'", null, values);
                db.close();
            } catch (SQLiteException e) {
                e.printStackTrace();
                Log.e("WriteActivity", "DB 저장 실패");
            }
             sendBroadcast(new Intent(Contact.SAVE_DB));
           /* MouthlyBaseAdapter mouthlyBaseAdapter = new MouthlyBaseAdapter();
            mouthlyBaseAdapter.test();*/
            //Toast.makeText(getApplicationContext(), String.valueOf(mouthlyBaseAdapter.getCount()), Toast.LENGTH_SHORT).show();
        }
    };

    private void Read() {
   /*     JSONObject jsnobject = new JSONObject(arrayList);
        JSONArray ja = jsnobject.getJSONArray("content");
        ArrayList<String> listdata = new ArrayList<>();
        for (int i = 0; i < ja.length(); i++) {
            listdata.add(ja.get(i).toString());
        }*/
    }
}
