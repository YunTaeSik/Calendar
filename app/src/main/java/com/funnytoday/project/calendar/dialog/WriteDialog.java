package com.funnytoday.project.calendar.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.funnytoday.project.calendar.R;
import com.squareup.picasso.Picasso;

/**
 * Created by YunTaeSik on 2016-11-11.
 */
public class WriteDialog extends Activity implements View.OnClickListener {

    private ImageView close_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_writedilaog);
        //  setFinishOnTouchOutside(true);

        close_image = (ImageView) findViewById(R.id.close_image);
        close_image.setOnClickListener(this);
        Picasso.with(this).load(R.drawable.close_image).fit().into(close_image);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_image:
                finish();
                break;
        }
    }
}
