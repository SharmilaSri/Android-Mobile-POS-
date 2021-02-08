package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.retailx.dreamdx.retailx.utils.ExceptionHandler;

public class UserAuthorisationDescActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_user_authorisation_desc);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.success));
    }

    public void goBackToMain(View v){
        Intent login = new Intent(this, MainActivity.class);
        startActivity(login);
    }
}
