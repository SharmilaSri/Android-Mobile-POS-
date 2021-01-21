package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.retailx.dreamdx.retailx.Adapters.SettingListAdapter;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;

import java.util.ArrayList;


public class SettingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Setting");

        recyclerView = (RecyclerView)findViewById(R.id.setting_list);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(SettingActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList <String> list=new ArrayList<>();
        list.add("Reset Password");
        list.add(getResources().getString(R.string.language));
        list.add(getResources().getString(R.string.help));
        //list.add(getResources().getString(R.string.about_us));
        list.add(getResources().getString(R.string.exit));

        mAdapter = new SettingListAdapter(SettingActivity.this,list);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }
}
