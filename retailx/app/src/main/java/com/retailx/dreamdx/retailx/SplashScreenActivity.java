package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.POJO.CreateOrEditFlag;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {

    String userId ="-1";
    public static SharedPreference sharedPreference = SharedPreference.getInstance();
    DBHelper db;
    Locale myLocale;
    LinearLayout linearLayout;
    String expiryDate="-1";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_splash_screen);


            userId = sharedPreference.getValue(this, Constants.USER_ID);
            db = new DBHelper(SplashScreenActivity.this);


            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(ConstantsUsed.SPLASH_TIME);
                    } catch (Exception e) {

                    } finally {

                        String language=SharedPreference.getInstance().getValue(getApplicationContext(), "language");
                        if(language!=null && !language.equalsIgnoreCase("-1")){
                            SharedPreference.getInstance().save(getApplicationContext(), "language",language);
                            myLocale = new Locale(language);
                            Resources res = getResources();
                            DisplayMetrics dm = res.getDisplayMetrics();
                            Configuration conf = res.getConfiguration();
                            conf.locale = myLocale;
                            res.updateConfiguration(conf, dm);
                        }


                        if (userId.equalsIgnoreCase("-1")) {
                            sharedPreference.save(getApplicationContext(), ConstantsUsed.USER_ID, "-1");
                            CreateOrEditFlag.getInstance().setFlag(0);
                            Intent loginInt = new Intent(SplashScreenActivity.this, HelpActivity.class);
                            startActivity(loginInt);
                            finish();
                        } else {


                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                            //check if app is expired
                            /*expiryDate=sharedPreference.getValue(SplashScreenActivity.this, "subscription_exp_date");
                            if(expiryDate!=null && !expiryDate.equalsIgnoreCase("-1")){
                                String curerentDate=AppFeatures.getTimeStamp("");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date current_date = format.parse(curerentDate);
                                    Date expiry_date = format.parse(expiryDate);
                                    if(current_date.after(expiry_date)){
                                        changeValues(userId);

                                    }else{
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }*/

                        }


                    }
                }
            };
            timer.start();
    }



    public void changeValues(String userId) {
        DBHelper db=new DBHelper(getApplicationContext());
        Cursor detailsCur = null;


        try {
            detailsCur=db.getSubDetails(userId);


            if ((detailsCur != null) && (detailsCur.getCount() > 0)) {
                while (detailsCur.moveToNext()) {

                    String subType = detailsCur.getString(detailsCur.getColumnIndex((ConstantsUsed.SUB_TYPE)));
                    String subValue = detailsCur.getString(detailsCur.getColumnIndex((ConstantsUsed.SUB_VALUE)));
                    SharedPreference sharedPreference = SharedPreference.getInstance();
                    sharedPreference.save(getApplicationContext(),subType, subValue);

                }
            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
        } finally {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
