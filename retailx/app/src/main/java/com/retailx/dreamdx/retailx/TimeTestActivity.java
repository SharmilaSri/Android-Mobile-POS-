package com.retailx.dreamdx.retailx;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class TimeTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_test);

        startWatch();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences mPrefs = this.getSharedPreferences("TIME",this.MODE_PRIVATE);
        runnimgMinutes = mPrefs.getInt("TIME",0);
        if(runnimgMinutes>0 && runnimgMinutes >=60){
            hour=runnimgMinutes/60;
            minutes=runnimgMinutes%60;
            sec=0;

            ((TextView) findViewById(R.id.sec)).setText(String.valueOf(sec));
            ((TextView) findViewById(R.id.hour)).setText(String.valueOf(hour));
            ((TextView) findViewById(R.id.min)).setText(String.valueOf(minutes));

        }
    }


    private void startWatch() {


        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                sec=sec+1;

                                if(sec==60) {//increase minute,reset sec
                                    minutes = minutes + 1;
                                    runnimgMinutes=runnimgMinutes+1;
                                    sec=0;
                                    save();//shared pref
                                }

                                if(minutes==60){//increase hour,reset minute
                                    hour=hour+1;
                                    minutes=0;
                                }

                                ((TextView) findViewById(R.id.sec)).setText(String.valueOf(sec));
                                ((TextView) findViewById(R.id.hour)).setText(String.valueOf(hour));
                                ((TextView) findViewById(R.id.min)).setText(String.valueOf(minutes));


                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

    }


    int runnimgMinutes=0;
    int hour=0;
    int sec=0;
    int minutes=0;

    private void save(){
        SharedPreferences mPrefs = getSharedPreferences("TIME",this.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        try{
            prefsEditor.putInt("TIME", runnimgMinutes);
            Toast.makeText(this,String.valueOf("Saved"+runnimgMinutes),Toast.LENGTH_LONG).show();
        }catch(Exception e){

        }

        prefsEditor.commit();
    }
}
