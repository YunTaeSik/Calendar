package com.funnytoday.project.calendar.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.funnytoday.project.calendar.R;

public class AlarmActivity extends AppCompatActivity {
    Button btnok;
    Button btncancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        btnok= (Button)findViewById(R.id.ok);
        btncancel=(Button) findViewById(R.id.cancel);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
