package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.retailx.dreamdx.retailx.Syncronisation.SyncFunctions;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.Validator;

public class CrashActivity extends AppCompatActivity {

    String brand,model,device,phone_id,phone_product,sdk,release,incremental,description;
    DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        getSupportActionBar().setTitle("Sorry!");

        db=new DBHelper(this);
        String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);


        Bundle extras = getIntent().getExtras();

        brand = extras.getString("brand", "");
        model = extras.getString("model", "");
        device = extras.getString("device", "");
        phone_id = extras.getString("phone_id", "");
        phone_product = extras.getString("phone_product", "");
        sdk = extras.getString("sdk", "");
        release = extras.getString("release", "");
        incremental = extras.getString("incremental", "");
        description = extras.getString("description", "");


        db.insertErrorDetails(userId,brand,model,device,phone_id,phone_product,sdk,release,incremental,description,1);


    }

    public void reportToContinue(View v){
        if(UtilityFunctions.isNetworkConnected(this ) || UtilityFunctions.isInternetAvailable()){
            String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);
            SyncFunctions.syncWithServer(this,userId);

            Intent loginInt = new Intent(this, SplashScreenActivity.class);
            startActivity(loginInt);
            finish();
        }else {
            Validator.showToast(this,"Please Enable internet and try again");
        }
    }
}
