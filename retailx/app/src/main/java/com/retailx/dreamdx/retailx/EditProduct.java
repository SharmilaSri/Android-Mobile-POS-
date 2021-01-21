package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.retailx.dreamdx.retailx.Adapters.StaticPageAdapter;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;

public class EditProduct extends AppCompatActivity {

    StaticPageAdapter adapterViewPager;
    TabLayout tabLayout;
    android.support.v4.view.ViewPager vpPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_edit_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.edit_products));

        vpPager = (android.support.v4.view.ViewPager) findViewById(R.id.pager);
        adapterViewPager = new StaticPageAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(vpPager);

    }

    @Override
    public void onResume() {
        super.onResume();

        vpPager = (android.support.v4.view.ViewPager) findViewById(R.id.pager);
        adapterViewPager = new StaticPageAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateBack();
                break;

        }
        return true;
    }

    private void navigateBack(){
        startActivity(new Intent(this,MainActivity.class));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateBack();

    }
}
