package com.funnytoday.project.calendar.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.util.Contact;
import com.squareup.picasso.Picasso;

/**
 * Created by sky87 on 2016-11-22.
 */

public class CalendarSelectDialog extends Activity implements View.OnClickListener {
    private ImageView cs_left_image, cs_right_image;
    private TextView calendar_select_year_text;
    private String Year;
    private String Month = "1";
    private RadioGroup one_radiogroup;
    private RadioGroup two_radiogroup;

    private RadioButton radioButtons[] = new RadioButton[12];
    private int radio_id[] = {R.id.one_btn, R.id.two_btn, R.id.three_btn, R.id.four_btn, R.id.five_btn, R.id.six_btn,
            R.id.seven_btn, R.id.eight_btn, R.id.nine_btn, R.id.ten_btn, R.id.eleven_btn, R.id.twelve_btn};
    private Button cancle_btn, ok_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_calendar_select_dialog);
        setFinishOnTouchOutside(false);
        cs_left_image = (ImageView) findViewById(R.id.cs_left_image);
        cs_right_image = (ImageView) findViewById(R.id.cs_right_image);
        calendar_select_year_text = (TextView) findViewById(R.id.calendar_select_year_text);
        ok_btn = (Button) findViewById(R.id.ok_btn);
        cancle_btn = (Button) findViewById(R.id.cancle_btn);
        one_radiogroup = (RadioGroup) findViewById(R.id.one_radiogroup);
        two_radiogroup = (RadioGroup) findViewById(R.id.two_radiogroup);
        Picasso.with(this).load(R.drawable.cs_left_arrow_image).into(cs_left_image);
        Picasso.with(this).load(R.drawable.cs_right_arrow_image).into(cs_right_image);
        Year = getIntent().getStringExtra(Contact.YEAR);
        Month = getIntent().getStringExtra(Contact.MONTH);
        calendar_select_year_text.setText(Year);
        cs_right_image.setOnClickListener(this);
        cs_left_image.setOnClickListener(this);
        ok_btn.setOnClickListener(this);
        cancle_btn.setOnClickListener(this);
        for (int i = 0; i < radio_id.length; i++) {
            radioButtons[i] = (RadioButton) findViewById(radio_id[i]);
            radioButtons[i].setOnClickListener(this);
        }
        radioButtons[0].setChecked(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cs_right_image:
                int add = Integer.parseInt(calendar_select_year_text.getText().toString()) + 1;
                calendar_select_year_text.setText(String.valueOf(add));
                break;
            case R.id.cs_left_image:
                int sub = Integer.parseInt(calendar_select_year_text.getText().toString()) - 1;
                calendar_select_year_text.setText(String.valueOf(sub));
                break;
            case R.id.one_btn:
                two_radiogroup.clearCheck();
                Month = "1";
                break;
            case R.id.two_btn:
                two_radiogroup.clearCheck();
                Month = "2";
                break;
            case R.id.three_btn:
                Month = "3";
                two_radiogroup.clearCheck();
                break;
            case R.id.four_btn:
                Month = "4";
                two_radiogroup.clearCheck();
                break;
            case R.id.five_btn:
                Month = "5";
                two_radiogroup.clearCheck();
                break;
            case R.id.six_btn:
                Month = "6";
                two_radiogroup.clearCheck();
                break;
            case R.id.seven_btn:
                Month = "7";
                one_radiogroup.clearCheck();
                break;
            case R.id.eight_btn:
                Month = "8";
                one_radiogroup.clearCheck();
                break;
            case R.id.nine_btn:
                Month = "9";
                one_radiogroup.clearCheck();
                break;
            case R.id.ten_btn:
                Month = "10";
                one_radiogroup.clearCheck();
                break;
            case R.id.eleven_btn:
                Month = "11";
                one_radiogroup.clearCheck();
                break;
            case R.id.twelve_btn:
                Month = "12";
                one_radiogroup.clearCheck();
                break;
            case R.id.ok_btn:
                finish();
                Intent intent = new Intent(Contact.SELECT_CALENDAR);
                intent.putExtra(Contact.YEAR, calendar_select_year_text.getText().toString());
                intent.putExtra(Contact.MONTH, Month);
                sendBroadcast(intent);
                break;
            case R.id.cancle_btn:
                finish();
                break;
        }
    }

}
