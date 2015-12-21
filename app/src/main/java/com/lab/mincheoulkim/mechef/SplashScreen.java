package com.lab.mincheoulkim.mechef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by mincheoulkim on 11/9/15.
 */
public class SplashScreen extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        // Remove App title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        */
        setContentView(R.layout.splash_screen);

        // As sleep takes milliseconds as its parameters, 3000 is equal to 3 seconds.
        // After the delay Main activity will show to users
        //TODO Define Timer Thread and Start Thread so that we shows see the splash screen before Main Activity
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(500);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Splash screen activity should disappear when user press previous button
        // as onPause is a method of Activity class which comes into play when the user leaves the activity.
        //TODO Finish Splash screen at onPuase
        finish();
    }
}


