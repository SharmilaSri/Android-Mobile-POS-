package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.Adapters.StaticHelpAdapter;
import com.retailx.dreamdx.retailx.POJO.CreateOrEditFlag;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;

public class HelpActivity extends AppCompatActivity {

    StaticHelpAdapter adapterViewPager;
    ImageView first,second,third;
    TextView skinBtn,nextBtn;
    int pagePosition=0;
    android.support.v4.view.ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_help);

        if(CreateOrEditFlag.getInstance().getFlag()==1){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Help");
        }else{
            getSupportActionBar().hide();
        }

        first=findViewById(R.id.first_page);
        second=findViewById(R.id.second_page);
        third=findViewById(R.id.third_page);

        skinBtn=findViewById(R.id.skip);
        nextBtn=findViewById(R.id.next);

        vpPager = (android.support.v4.view.ViewPager) findViewById(R.id.pager);
        adapterViewPager = new StaticHelpAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);


        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {

                pagePosition=position;

                switch (position) {
                    case 0:
                        nextBtn.setText("Next");
                        skinBtn.setVisibility(View.GONE);
                        first.setImageDrawable(getResources().getDrawable(R.drawable.orange_round));
                        second.setImageDrawable(getResources().getDrawable(R.drawable.grey_round));
                        third.setImageDrawable(getResources().getDrawable(R.drawable.grey_round));
                        break;
                    case 1:
                        nextBtn.setText("Next");
                        first.setImageDrawable(getResources().getDrawable(R.drawable.grey_round));
                        second.setImageDrawable(getResources().getDrawable(R.drawable.orange_round));
                        third.setImageDrawable(getResources().getDrawable(R.drawable.grey_round));
                        break;
                    case 2:
                        skinBtn.setVisibility(View.GONE);
                        nextBtn.setText("Finish");
                        first.setImageDrawable(getResources().getDrawable(R.drawable.grey_round));
                        second.setImageDrawable(getResources().getDrawable(R.drawable.grey_round));
                        third.setImageDrawable(getResources().getDrawable(R.drawable.orange_round));
                        break;
                }
            }

                // This method will be invoked when the current page is scrolled
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // Code goes here

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // Code goes here
                }
            });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(CreateOrEditFlag.getInstance().getFlag()==1)
                    startActivity(new Intent(this,SettingActivity.class));
                break;

        }
        return true;
    }

    public void skip(View v){
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void next(View v){
        switch (pagePosition) {
            case 0:
                vpPager.setCurrentItem(1);
                break;
            case 1:
                vpPager.setCurrentItem(2);
                break;
            case 2:
                if(CreateOrEditFlag.getInstance().getFlag()==1)
                    startActivity(new Intent(this,MainActivity.class));
                else
                    startActivity(new Intent(this,LoginActivity.class));

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            if(CreateOrEditFlag.getInstance().getFlag()==1)
                startActivity(new Intent(this,SettingActivity.class));
    }
}
