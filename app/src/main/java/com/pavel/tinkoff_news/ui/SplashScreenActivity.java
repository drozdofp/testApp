package com.pavel.tinkoff_news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pavel.tinkoff_news.R;

/**
 * Created by pavel on 19.08.2016.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private Runnable delayedStart = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashScreenActivity.this, NewsListActivity.class));
            finish();
        }
    };

    private Handler handlerStart = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handlerStart.postDelayed(delayedStart, 1500);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handlerStart.removeCallbacks(delayedStart);
    }
}
