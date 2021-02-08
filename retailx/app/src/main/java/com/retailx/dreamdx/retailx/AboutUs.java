package com.retailx.dreamdx.retailx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.retailx.dreamdx.retailx.utils.ExceptionHandler;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About Us");
    }

}
