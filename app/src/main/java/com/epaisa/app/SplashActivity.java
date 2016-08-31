package com.epaisa.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.epesa.com.epaisa.R;

/**
 * Created by ist on 25/8/16.
 */
public class SplashActivity  extends Activity implements Runnable{


    private static int SPLASH_TIME_OUT = 1500;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_splash);
        thread = new Thread(SplashActivity.this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(SPLASH_TIME_OUT);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (InterruptedException e) {
           e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }
}
