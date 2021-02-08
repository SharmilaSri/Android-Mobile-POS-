package com.retailx.dreamdx.retailx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.retailx.dreamdx.retailx.apicalls.ApiCalls;
import com.retailx.dreamdx.retailx.apicalls.VolleyCalls;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {

    private android.support.design.widget.TextInputEditText emailTxt, passwordTxt,confirmPsswordTxt;
    private static String  confirmPW,password;
    private  android.support.design.widget.TextInputLayout emaillabel,passwordLabel,confirmpasswordLabel,phoneNolabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reset Password");

        passwordTxt=findViewById(R.id.id_password);
        confirmPsswordTxt=findViewById(R.id.id_password_confirm);
    }

    public void submit(View v){

        password=passwordTxt.getText().toString().trim();
        confirmPW=confirmPsswordTxt.getText().toString().trim();
        passwordLabel=findViewById(R.id.password_label);
        confirmpasswordLabel=findViewById(R.id.confirm_password_label);

        if(password.isEmpty()){
            passwordLabel.setError(getResources().getString(R.string.empty_field));

        }else if(confirmPW.isEmpty()){
            confirmpasswordLabel.setError(getResources().getString(R.string.empty_field));

        }else if(!password.equalsIgnoreCase(confirmPW)){
            confirmpasswordLabel.setError(getResources().getString(R.string.invalid_password));

        }else {
            if (UtilityFunctions.isInternetAvailable() || UtilityFunctions.isNetworkConnected(this)) {
                ApiCalls apicalls = new VolleyCalls();
                String url = ConstantsUsed.URL_LOGIN.trim();
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", SharedPreference.getInstance().getValue(this,ConstantsUsed.USER_ID));
                params.put("new_password", password);
                apicalls.connectToNetworkPost(this, ConstantsUsed.URL_RESET_PW_, ConstantsUsed.TYPE_RESET_PW, params);

            }
        }

    }
}
