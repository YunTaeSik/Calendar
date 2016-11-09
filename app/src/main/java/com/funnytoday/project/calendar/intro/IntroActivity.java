package com.funnytoday.project.calendar.intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.main.MainActivity;


/**
 * Created by YunTaeSik on 2016-08-15.
 */
public class IntroActivity extends Activity {

    private Handler h;//핸들러 선언
    private Animation alphaAni;
    private Animation alphaAni_one;
    private Animation alphaAni_two;
    private Animation alphaAni_three;
    private Animation alphaAni_four;
    private TextView[] loading_circle = new TextView[4];
    private TextView loge_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //인트로화면이므로 타이틀바를 없앤다
        setContentView(R.layout.activity_intro);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        loading_circle[0] = (TextView) findViewById(R.id.loading_circle_one);
        loading_circle[1] = (TextView) findViewById(R.id.loading_circle_two);
        loading_circle[2] = (TextView) findViewById(R.id.loading_circle_three);
        loading_circle[3] = (TextView) findViewById(R.id.loading_circle_four);
        loge_text = (TextView) findViewById(R.id.loge_text);


        alphaAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        alphaAni_one = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_one);
        alphaAni_two = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_two);
        alphaAni_three = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_three);
        alphaAni_four = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_four);


        // loge_text.startAnimation(alphaAni);
        loading_circle[0].startAnimation(alphaAni_one);
        loading_circle[1].startAnimation(alphaAni_two);
        loading_circle[2].startAnimation(alphaAni_three);
        loading_circle[3].startAnimation(alphaAni_four);


        h = new Handler(); //딜래이를 주기 위해 핸들러 생성
        h.postDelayed(mrun, 1800); // 딜레이 ( 런어블 객체는 mrun, 시간 2초)

    }

    Runnable mrun = new Runnable() {
        @Override
        public void run() {

            Intent i = new Intent(IntroActivity.this, MainActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                Intent i = new Intent(IntroActivity.this, MainActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        h.removeCallbacks(mrun);
    }
}
