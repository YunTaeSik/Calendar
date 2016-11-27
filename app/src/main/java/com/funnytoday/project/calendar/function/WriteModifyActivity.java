package com.funnytoday.project.calendar.function;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.funnytoday.project.calendar.dialog.TimePickerDialog;
import com.funnytoday.project.calendar.dialog.WriteDeleteDialog;
import com.funnytoday.project.calendar.util.Contact;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class WriteModifyActivity extends AppCompatActivity implements View.OnClickListener {
    private int REQ_CODE_PICK_PICTURE = 1;
    private int Time_PIRKER_START = 2;
    private int Time_PIRKER_END = 3;
    private int DELETE_DIALOG = 4;
    private String MODIFY_MODE = "OFF";

    private String Year;
    private String Month;
    private String DAY;
    private String DAY_OF_WEEK;
    private int position;

    private ActionBar actionBar;
    private MenuItem delete_btn;
    private MenuItem modify_btn;
    private MenuItem gallery_btn;
    private MenuItem finish_btn;
    private EditText title_edit;
    private EditText content_edit;
    private TextView month_text;
    private TextView date_text;
    private TextView year_text;
    private TextView start_time_text;
    private TextView end_time_text;
    private LinearLayout write_addlayout;

    private DBManager dbManager;
    private SQLiteDatabase redadb;
    private Cursor cursor;
    private String curor_id;
    private String starttime;
    private String endtime;
    private String title;
    private String jsonarray;
    private ArrayList<String> content_list = new ArrayList<>();
    private ArrayList<String> image_list = new ArrayList<>();

    private int start = 1;
    private int end = 1;
    private int add_count = 0;
    private Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_modify);
        getWindow().setStatusBarColor(getResources().getColor(R.color.theme));
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.write_see_text));
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme)));
        actionBar.setHomeButtonEnabled(true);
        canvas = new Canvas();

        Year = getIntent().getStringExtra(Contact.YEAR);
        Month = getIntent().getStringExtra(Contact.MONTH);
        DAY = getIntent().getStringExtra(Contact.DAY);
        DAY_OF_WEEK = getIntent().getStringExtra(Contact.DAY_OF_WEEK);
        position = getIntent().getIntExtra(Contact.POSITION, 0);

        month_text = (TextView) findViewById(R.id.month_text);
        date_text = (TextView) findViewById(R.id.date_text);
        year_text = (TextView) findViewById(R.id.year_text);
        start_time_text = (TextView) findViewById(R.id.start_time_text);
        end_time_text = (TextView) findViewById(R.id.end_time_text);
        write_addlayout = (LinearLayout) findViewById(R.id.write_addlayout);


        month_text.setText(Month);
        date_text.setText(DAY);
        year_text.setText(Year + "\n" + getDayOfWeek(Integer.parseInt(DAY_OF_WEEK)));

        title_edit = (EditText) findViewById(R.id.title_edit);
        content_edit = (EditText) findViewById(R.id.content_edit);
        title_edit.setFocusableInTouchMode(false);
        content_edit.setFocusableInTouchMode(false);

        ReadDB();
        start_time_text.setOnClickListener(this);
        end_time_text.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.write_modify_actionbar, menu);
        delete_btn = menu.findItem(R.id.delete_btn);
        modify_btn = menu.findItem(R.id.modify_btn);
        gallery_btn = menu.findItem(R.id.gallery_btn);
        finish_btn = menu.findItem(R.id.finish_btn);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_btn:
                Intent intent = new Intent(this, WriteDeleteDialog.class);
                startActivityForResult(intent, DELETE_DIALOG);
                break;
            case R.id.modify_btn:
                actionBar.setTitle(getString(R.string.write_modify_text));
                delete_btn.setVisible(false);
                modify_btn.setVisible(false);
                gallery_btn.setVisible(true);
                finish_btn.setVisible(true);
                title_edit.setFocusableInTouchMode(true); //읽기전용
                title_edit.setFocusable(true);
                title_edit.requestFocus();
                title_edit.setSelection(title.length());
                content_edit.setFocusableInTouchMode(true);  //읽기전용
                for (int i = 1; i < write_addlayout.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) write_addlayout.getChildAt(i);
                    RelativeLayout relativeLayout = (RelativeLayout) linearLayout.getChildAt(0);
                    RelativeLayout relativeLayout2 = (RelativeLayout) relativeLayout.getChildAt(1);
                    EditText editText = (EditText) linearLayout.getChildAt(1);
                    editText.setFocusableInTouchMode(true);   //읽기전용
                    relativeLayout2.setVisibility(View.VISIBLE);
                }
                MODIFY_MODE = "ON";
                break;
            case R.id.gallery_btn:
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("video/*, images/*");
                startActivityForResult(i, REQ_CODE_PICK_PICTURE);
                break;
            case R.id.finish_btn:
                if (end > start) {
                    if (title_edit.getText().toString().equals("")) {
                        Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                    } else {
                        finish();
                        saveContent();
                    }
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
            case R.id.start_time_text:
                if (MODIFY_MODE.equals("ON")) {
                    startActivityForResult(new Intent(this, TimePickerDialog.class), Time_PIRKER_START);
                }
                break;
            case R.id.end_time_text:
                if (MODIFY_MODE.equals("ON")) {
                    startActivityForResult(new Intent(this, TimePickerDialog.class), Time_PIRKER_END);
                }
                break;
            case R.id.delete_image_layout:
                if (MODIFY_MODE.equals("ON")) {
                    for (int i = 1; i < write_addlayout.getChildCount(); i++) {
                        LinearLayout linearLayout = (LinearLayout) write_addlayout.getChildAt(i);
                        RelativeLayout relativeLayout = (RelativeLayout) linearLayout.getChildAt(0);
                        EditText editText = (EditText) linearLayout.getChildAt(1);
                        RelativeLayout relativeLayout2 = (RelativeLayout) relativeLayout.getChildAt(1);
                        if (String.valueOf(view.getTag()).equals(String.valueOf(relativeLayout2.getTag()))) {
                            image_list.remove(i - 1);
                            write_addlayout.removeViewAt(i);
                            if (i > 1) {
                                if (write_addlayout.getChildCount() - 1 <= 1) {
                                    content_edit.append("\n" + editText.getText().toString());
                                } else {
                                    LinearLayout addlinear = (LinearLayout) write_addlayout.getChildAt(i - 1);  //전위치 에 edittext 추가
                                    EditText addedit = (EditText) addlinear.getChildAt(1);
                                    addedit.append("\n" + editText.getText().toString()); //
                                }
                            } else {
                                content_edit.append("\n" + editText.getText().toString());
                            }
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

                image_list.add(data.getDataString()); //images data list 추가
                write_addlayout.addView(itemview);

                delete_image_layout.setTag(add_count);
                add_count++;
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
        } else if (requestCode == DELETE_DIALOG) {
            if (resultCode == 1) {
                deleteDB();
                finish();
                sendBroadcast(new Intent(Contact.SAVE_DB));
            }
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

    private void ReadDB() {
        dbManager = new DBManager(this, "Write", null, 1);
        redadb = dbManager.getReadableDatabase();
        String table_name = String.valueOf(Year) + String.valueOf(Month) + String.valueOf(DAY);
        cursor = redadb.query("'" + table_name + "'", null, null, null, null, null, null);
        cursor.moveToPosition(position);
        curor_id = cursor.getString(0);
        starttime = cursor.getString(1);
        endtime = cursor.getString(2);
        title = cursor.getString(3);
        jsonarray = cursor.getString(4);

        JSONObject jsnobject = null;
        try {
            jsnobject = new JSONObject(jsonarray);
            JSONArray content = jsnobject.getJSONArray("content");
            JSONArray images = jsnobject.getJSONArray("images");
            for (int i = 0; i < content.length(); i++) {
                content_list.add(content.get(i).toString());
            }
            for (int i = 0; i < images.length(); i++) {
                image_list.add(images.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        content_edit.setText(content_list.get(0));

        start_time_text.setText(starttime + "\nHour");
        end_time_text.setText(endtime + "\nHour");
        start = Integer.parseInt(starttime);
        end = Integer.parseInt(endtime);

        title_edit.setText(title);
        for (int i = 0; i < image_list.size(); i++) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout itemview = (LinearLayout) layoutInflater.inflate(R.layout.write_item_view, null);
            ImageView imageView = (ImageView) itemview.findViewById(R.id.write_item_image);
            EditText editText = (EditText) itemview.findViewById(R.id.write_item_edit);
            ImageView delete = (ImageView) itemview.findViewById(R.id.item_image_delete);
            RelativeLayout delete_image_layout = (RelativeLayout) itemview.findViewById(R.id.delete_image_layout);
            Picasso.with(this).load(R.drawable.delete_image).into(delete);
            delete_image_layout.setOnClickListener(this);

            Bitmap image_bitmap = null;
            try {
                image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(image_list.get(i)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (canvas.getMaximumBitmapHeight() / 8 < image_bitmap.getHeight()
                    || canvas.getMaximumBitmapWidth() / 8 < image_bitmap.getWidth()) {
                Picasso.with(this).load(Uri.parse(image_list.get(i))).resize(300, 1000).into(imageView);
            } else {
                Picasso.with(this).load(Uri.parse(image_list.get(i))).into(imageView);
            }
            editText.setText(content_list.get(i + 1));
            editText.setFocusableInTouchMode(false);
            delete_image_layout.setVisibility(View.GONE);
            delete_image_layout.setTag(add_count);
            add_count++;
            write_addlayout.addView(itemview);
        }
    }

    private void saveContent() {
        content_list.clear();
        content_list.add(content_edit.getText().toString());
        for (int i = 1; i < write_addlayout.getChildCount(); i++) {  //
            LinearLayout layout = (LinearLayout) write_addlayout.getChildAt(i);
            EditText editText = (EditText) layout.getChildAt(1);
            content_list.add(editText.getText().toString());
        }
        asyncTask.execute();
    }

    private void deleteDB() {
        dbManager = new DBManager(getApplicationContext(), "Write", null, 1);
        SQLiteDatabase db = dbManager.getWritableDatabase();
        String table_name = Year + month_text.getText().toString() + DAY;
        db.delete("'" + table_name + "'", "_id=?", new String[]{curor_id});
        db.close();
    }

    private AsyncTask asyncTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                JSONObject json = new JSONObject();
                json.put("images", new JSONArray(image_list));
                json.put("content", new JSONArray(content_list));
                jsonarray = json.toString();
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
                String table_name = Year + month_text.getText().toString() + DAY;
                ContentValues values = new ContentValues();
                values.put("start", String.valueOf(start));
                values.put("end", String.valueOf(end));
                values.put("title", title_edit.getText().toString());
                values.put("json", jsonarray);
                db.update("'" + table_name + "'", values, "_id=?", new String[]{curor_id});
                db.close();
            } catch (SQLiteException e) {
                e.printStackTrace();
                Log.e("WriteActivity", "DB 저장 실패");
            }
            sendBroadcast(new Intent(Contact.SAVE_DB));

        }
    };
}
