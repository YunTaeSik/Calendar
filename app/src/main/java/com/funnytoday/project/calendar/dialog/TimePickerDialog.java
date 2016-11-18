package com.funnytoday.project.calendar.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.funnytoday.project.calendar.R;

public class TimePickerDialog extends Activity implements View.OnClickListener {
    private NumberPicker time_number;
    private Button ok_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_time_picker_dialog);
        setFinishOnTouchOutside(false);

        time_number = (NumberPicker) findViewById(R.id.time_number);
        ok_btn = (Button) findViewById(R.id.ok_btn);

        time_number.setMinValue(1);
        time_number.setMaxValue(24);

        ok_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_btn:
                setResult(time_number.getValue());
                finish();
                break;
        }
    }
}
