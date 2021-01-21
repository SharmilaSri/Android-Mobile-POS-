package com.retailx.dreamdx.retailx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.retailx.dreamdx.retailx.apicalls.ApiCalls;
import com.retailx.dreamdx.retailx.apicalls.VolleyCalls;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.util.HashMap;
import java.util.Map;

public class SubmitEmailActivity extends AppCompatActivity {

    EditText emailtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_submit_email);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reset Password");

        emailtxt=findViewById(R.id.id_email);

    }

    public void submit(View v){
        String email=emailtxt.getText().toString();
        if(email.isEmpty()){
            emailtxt.setError(getResources().getString(R.string.empty_field));
        }else{

            if(UtilityFunctions.isInternetAvailable() || UtilityFunctions.isNetworkConnected(this)){

                ApiCalls apicalls=new VolleyCalls();
                Map<String,String> params = new HashMap<String, String>();
                params.put(ConstantsUsed.EMAIL_ID,email);
                apicalls.connectToNetworkPost(this, ConstantsUsed.URL_SUBMIT_,ConstantsUsed.TYPE_SUBMIT,params);
                Validator.showToast(this,"We sent an email to "+email+" with a new password");

            }
            else{
                Validator.showToast(this,"NO INTERNET");
            }
        }

    }
}
