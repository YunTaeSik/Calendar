package com.funnytoday.project.calendar.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import com.funnytoday.project.calendar.R;

public class DatePickerDialog extends Activity implements View.OnClickListener {
    private Button ok_btn;
    private DatePicker date_picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_date_picker_dialog);
        setFinishOnTouchOutside(false);

        ok_btn = (Button) findViewById(R.id.ok_btn);
        date_picker = (DatePicker) findViewById(R.id.date_picker);
        ok_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_btn:
                Intent intent = new Intent();
                intent.putExtra("0", date_picker.getYear());
                intent.putExtra("1", date_picker.getMonth());
                intent.putExtra("2", date_picker.getDayOfMonth());
                setResult(0, intent);
                finish();
                break;
        }

    }
}
