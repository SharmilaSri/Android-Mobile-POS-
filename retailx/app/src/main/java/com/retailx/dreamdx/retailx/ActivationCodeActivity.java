
package com.retailx.dreamdx.retailx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.retailx.dreamdx.retailx.apicalls.ApiCalls;
import com.retailx.dreamdx.retailx.apicalls.VolleyCalls;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.POJO.User;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.util.HashMap;
import java.util.Map;

public class ActivationCodeActivity extends AppCompatActivity {
    EditText one,two,three,four,otp_code;
    String /*oneCode,twoCode,threeCode,fourCode=""*,*/otpTxt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_activation_code);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Verification");
        otp_code=findViewById(R.id.code_one);

        /*one=findViewById(R.id.code_one);
        two=findViewById(R.id.code_two);
        three=findViewById(R.id.code_three);
        four=findViewById(R.id.code_four);

        one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length() == 1) {

                    one.clearFocus();

                    two.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(two, InputMethodManager.SHOW_IMPLICIT);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length() == 1) {

                    two.clearFocus();

                    three.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(three, InputMethodManager.SHOW_IMPLICIT);

                } else if (charSequence.length() == 0) {

                    two.clearFocus();
                    one.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(one, InputMethodManager.SHOW_IMPLICIT);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length() == 1) {

                    three.clearFocus();

                    four.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(four, InputMethodManager.SHOW_IMPLICIT);

                } else if (charSequence.length() == 0) {

                    three.clearFocus();
                    two.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(two, InputMethodManager.SHOW_IMPLICIT);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 0) {

                    four.clearFocus();
                    three.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(three, InputMethodManager.SHOW_IMPLICIT);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

    }

    public void resendCode(View v){
        if(UtilityFunctions.isInternetAvailable() || UtilityFunctions.isNetworkConnected(this)) {
            ApiCalls api = new VolleyCalls();
            Map<String, String> params = new HashMap<String, String>();
            params.put(ConstantsUsed.EMAIL_ID, User.getInstance().getEmail_id());
            api.connectToNetworkPost(this, ConstantsUsed.URL_RE_SUBMIT_OTP, ConstantsUsed.TYPE_OTP_RE_SUBMIT,
                    params);
        }else{
            Validator.showToast(this,"NO INTERNET");
        }
    }


    public void activateOtp(View v){
        //oneCode=one.getText().toString();
       // twoCode=two.getText().toString();
       // threeCode=three.getText().toString();
        //fourCode=four.getText().toString();
        otpTxt=otp_code.getText().toString();

        if(!otpTxt.isEmpty() /*&& !twoCode.isEmpty() && !threeCode.isEmpty() && !fourCode.isEmpty()*/){
           //String otpCode=oneCode+twoCode+threeCode+fourCode;

               ApiCalls api = new VolleyCalls();
               Map<String, String> params = new HashMap<String, String>();
               params.put(ConstantsUsed.EMAIL_ID, User.getInstance().getEmail_id());
               params.put(ConstantsUsed.OTP, otpTxt);
               api.connectToNetworkPost(this, ConstantsUsed.URL_SUBMIT_OTP, ConstantsUsed.TYPE_OTP_SUBMIT,
                       params);
        }else{
            Validator.showToast(this,"EMPTY FIELDS");
        }
    }


}
