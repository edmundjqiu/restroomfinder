package com.edmundjqiu.unhackathongui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    private static final int SPLASH_SCREEN_PAUSE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(
            new Runnable() {
                @Override
                public void run() {
                    // After the pause, start the real main activity, end this one
                    Intent i = new Intent(SplashActivity.this, Emergency.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_SCREEN_PAUSE);

    }
}
