package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;

import java.util.Locale;

public class ChangeLanguageActivity extends AppCompatActivity {
    Locale myLocale;
    String currentLanguage = "en", currentLang;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_change_language);

        spinner = (Spinner) findViewById(R.id.id_spinner_language);


        ArrayAdapter<CharSequence> sinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.language_support, android.R.layout.simple_spinner_item);
        sinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        setLocale("en");
                        break;
                    case 2:
                        setLocale("si");
                        break;
                    case 3:
                        setLocale("ta");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void setLocale(String localeName) {
        SharedPreference.getInstance().save(this, "language",localeName);
        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, SettingActivity.class);
        refresh.putExtra(currentLang, localeName);
        startActivity(refresh);

    }
}
