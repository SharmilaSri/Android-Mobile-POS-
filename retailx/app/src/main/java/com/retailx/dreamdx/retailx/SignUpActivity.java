package com.retailx.dreamdx.retailx;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.retailx.dreamdx.retailx.POJO.PrivilageData;
import com.retailx.dreamdx.retailx.apicalls.ApiCalls;
import com.retailx.dreamdx.retailx.apicalls.VolleyCalls;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.POJO.User;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {
    private android.support.design.widget.TextInputEditText /*firtNTxt, lastNameTxt,*/ addressOneTxt,addressTwoTxt,emailTxt, passwordTxt,phoneNumberTxt,confirmPsswordTxt;
    private CheckBox vatCheck;
    private static String  confirmPW,email,password,phoneNumber,vatUser="";
    private static DBHelper db=null;
    private  android.support.design.widget.TextInputLayout emaillabel,passwordLabel,confirmpasswordLabel,phoneNolabel;
    Button signUp;

    static Boolean isUser=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_sign_up);

        checkIfUserOrAdmin();

        setUpFields();

        db=new DBHelper(this);

    }

    private void checkIfUserOrAdmin(){
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        // get data via the key
        Boolean isUserFlag = extras.getBoolean("IS_USER");
        if (isUserFlag!=null) {
            isUser=isUserFlag;
        }
    }

    private void navigationBack(){//should change
        if(isUser) {
            startActivity(new Intent(this,MainActivity.class));
        }else{
            startActivity(new Intent(this,LoginActivity.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigationBack();
                break;

        }
        return true;
    }

    private void setUpFields(){

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        emailTxt=findViewById(R.id.id_email);
        passwordTxt=findViewById(R.id.id_password);
        confirmPsswordTxt=findViewById(R.id.id_password_confirm);
        phoneNumberTxt=findViewById(R.id.id_phone_NO);
        emaillabel=findViewById(R.id.email_label);
        passwordLabel=findViewById(R.id.password_label);
        confirmpasswordLabel=findViewById(R.id.confirm_password_label);
        phoneNolabel=findViewById(R.id.phone_label);
        signUp=findViewById(R.id.id_btn_signup);

        if(isUser) {
            getSupportActionBar().setTitle("Enter user's details");
            signUp.setText("Next");
        }else {
            getSupportActionBar().setTitle("Enter your details");
            signUp.setText(getResources().getString(R.string.sign_up));
        }


    }

    public void  signUp(View v){
        SharedPreference sharedPreference = SharedPreference.getInstance();


        password=passwordTxt.getText().toString().trim();
        email=emailTxt.getText().toString().trim();
        phoneNumber=phoneNumberTxt.getText().toString().trim();
        confirmPW=confirmPsswordTxt.getText().toString().trim();

        if (email.isEmpty()){
            emailTxt.setError(getResources().getString(R.string.empty_field));
            // email_label.setErrorTextColor(ColorStateList.createFromXml());
        }else if(!Validator.validateEmail(email)){
            emailTxt.setError(getResources().getString(R.string.invalid_email));

        }

        else if(password.isEmpty()){
            passwordLabel.setError(getResources().getString(R.string.empty_field));

        }else if(confirmPW.isEmpty()){
             confirmpasswordLabel.setError(getResources().getString(R.string.empty_field));

        }else if(phoneNumber.isEmpty()){
            phoneNumberTxt.setError(getResources().getString(R.string.empty_field));
        }else if(!password.equalsIgnoreCase(confirmPW)){
            confirmpasswordLabel.setError(getResources().getString(R.string.invalid_password));

        }else if (!Validator.validatePhoneNumber(phoneNumber)){
            phoneNumberTxt.setError(getResources().getString(R.string.phone_no));
        } else {
             phoneNumberTxt.setError(null);
             passwordLabel.setError(null);
             confirmpasswordLabel.setError(null);


             /*.. TO BE REMOVED ...*/
            User user =User.getInstance();
            user.setClient_id("0");
            user.setEmail_id(email);
            user.setMobile_no(phoneNumber);
            user.setUser_id("U"+ AppFeatures.getTimeStamp());
            User.getInstance().setInstance(user);

            sharedPreference.save(this, Constants.OTP,"0000");//hard coded should come from backend
            sharedPreference.save(this, Constants.CLIENT_ID,User.getInstance().getClient_id());
            sharedPreference.save(this, Constants.EMAIL,User.getInstance().getEmail_id() );
            sharedPreference.save(this, Constants.PHONE_NUMBER,User.getInstance().getMobile_no());
            sharedPreference.save(this, Constants.USER_ID,User.getInstance().getUser_id());

            db.insertUserDetails(email ,phoneNumber,User.getInstance().getUser_id(),User.getInstance().getClient_id(),password,1,1);//signed up and becoming an admin

            Intent login = new Intent(this, MainActivity.class);
            this.startActivity(login);
            /*.. TO BE REMOVED ...*/

            /*... NEEDS TO BE INCLUDED ...*/
            //volley call
          /*  if(UtilityFunctions.isInternetAvailable() || UtilityFunctions.isNetworkConnected(this)) {

                ApiCalls apicalls = new VolleyCalls();
                String url = ConstantsUsed.URL_LOGIN.trim();
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_id", email);
                params.put("password", password);
                params.put("contact_id",phoneNumber);

              if(isUser) {
                  String clientId=sharedPreference.getValue(this,Constants.CLIENT_ID);
                  String userId=sharedPreference.getValue(this,Constants.USER_ID);
                  params.put("client_id", clientId);

              }else
                  params.put("client_id","0");


                apicalls.connectToNetworkPost(this, ConstantsUsed.URL_REGISTER, ConstantsUsed.TYPE_LOGIN, params);

            }else{

                Validator.showToast(this,"Please Connect To Internet");
            }*/
            /*... NEEDS TO BE INCLUDED ...*/

        }
    }

    public static void apiCallBackOtp(String otp, Context ctx){


        db=new DBHelper(ctx);

        if(isUser) {//creating the user as an admin
            // db.insertUserDetails(User.getInstance().getEmail_id() ,User.getInstance().getMobile_no(),User.getInstance().getUser_id(),User.getInstance().getClient_id(),0,1);//signed up and becoming an admin
            db.insertUserDetails(email ,phoneNumber,User.getInstance().getUser_id(),User.getInstance().getClient_id(),"1",0,1);//signed up and becoming an admin

            Intent login = new Intent(ctx, UserAuthorisationActivity.class);
            login.putExtra(ConstantsUsed.USER_ID,User.getInstance().getUser_id());
            ctx.startActivity(login);
        }else {

            SharedPreference sharedPreference = SharedPreference.getInstance();

            sharedPreference.save(ctx, Constants.OTP,otp);
            sharedPreference.save(ctx, Constants.CLIENT_ID,User.getInstance().getClient_id());
            sharedPreference.save(ctx, Constants.EMAIL,User.getInstance().getEmail_id() );
            sharedPreference.save(ctx, Constants.PHONE_NUMBER,User.getInstance().getMobile_no());
            sharedPreference.save(ctx, Constants.USER_ID,User.getInstance().getUser_id());

            //Validator.showToast(ctx,otp);

            db.insertUserDetails(email ,phoneNumber,User.getInstance().getUser_id(),User.getInstance().getClient_id(),"",1,1);//signed up and becoming an admin

            Intent login = new Intent(ctx, LoginActivity.class);
            ctx.startActivity(login);
        }
    }

    public static void apiCallBackSuccess(String response, Context ctx, JSONObject reader){
        SharedPreference sharedPreference = SharedPreference.getInstance();


        if (response.equalsIgnoreCase("success")) {

            sharedPreference.save(ctx, Constants.CLIENT_ID,User.getInstance().getClient_id());
            sharedPreference.save(ctx, Constants.EMAIL,User.getInstance().getEmail_id() );
            sharedPreference.save(ctx, Constants.PHONE_NUMBER,User.getInstance().getMobile_no());
            sharedPreference.save(ctx, Constants.USER_ID,User.getInstance().getUser_id());

            /*this code is duplicated in Utility function -apiCallBackOtpSuccess*/
            sharedPreference.save(ctx, ConstantsUsed.IS_ADMIN, PrivilageData.getInstance().getIs_admin());
            sharedPreference.saveInt(ctx, ConstantsUsed.CREATE_DISCOUNT, PrivilageData.getInstance().getCreate_discount());
            sharedPreference.saveInt(ctx, ConstantsUsed.CREATE_EDIT_PRODUCT,PrivilageData.getInstance().getCreate_edit_product() );
            sharedPreference.saveInt(ctx, ConstantsUsed.MANAGE_STOCK,PrivilageData.getInstance().getManage_stock());
            sharedPreference.saveInt(ctx, ConstantsUsed.VIEW_ALL_TAX,PrivilageData.getInstance().getView_all_tx());

            /*this code is duplicated in Utility function -apiCallBackOtpSuccess*/

            /*this code is duplicated in Utility function -apiCallBackOtpSuccess*/

            UtilityFunctions.apiCallBackOtpSuccess(ctx,reader);

            /*try {
                String basic_features_value = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(0).getString("basic_features");
                String billprint_value = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(1).getString("billprint");
                String viewreport_value = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(2).getString("viewreport");
                String viewhistory_value = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(3).getString("viewhistory");
                String customerprofile = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(4).getString("customerprofile");
                String multiusers = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(5).getString("multiusers");
                String billshare = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(6).getString("billshare");
                String managestock = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(7).getString("managestock");
                String creditcard = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(8).getString("creditcard");
                String qrcode = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(9).getString("qrcode");
                String cheque = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(10).getString("cheque");
                String subscription_exp_date_value = reader.getJSONObject("subscription_data").getString("subscription_exp_date");

                sharedPreference.save(ctx,"basic_features", basic_features_value);
                sharedPreference.save(ctx, "billprint", billprint_value);
                sharedPreference.save(ctx,"viewreport",viewreport_value );
                sharedPreference.save(ctx, "viewhistory",viewhistory_value);
                sharedPreference.save(ctx, "customerprofile",customerprofile);
                sharedPreference.save(ctx, "multiusers",multiusers);
                sharedPreference.save(ctx, "billshare",billshare);
                sharedPreference.save(ctx, "managestock",managestock);
                sharedPreference.save(ctx, "creditcard",creditcard);
                sharedPreference.save(ctx, "qrcode",qrcode);
                sharedPreference.save(ctx, "cheque",cheque);

                sharedPreference.save(ctx, "subscription_exp_date",subscription_exp_date_value);


                // free version values
                basic_features_value = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(0).getString("basic_features");
                billprint_value = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(1).getString("billprint");
                viewreport_value = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(2).getString("viewreport");
                viewhistory_value = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(3).getString("viewhistory");
                customerprofile = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(4).getString("customerprofile");
                multiusers = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(5).getString("multiusers");
                billshare = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(6).getString("billshare");
                managestock = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(7).getString("managestock");
                creditcard = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(8).getString("creditcard");
                qrcode = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(9).getString("qrcode");
                cheque = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(10).getString("cheque");

                DBHelper db=new DBHelper(ctx);
                db.insertSubDetails(User.getInstance().getUser_id(),"basic_features",basic_features_value);
                db.insertSubDetails(User.getInstance().getUser_id(),"billprint",billprint_value);
                db.insertSubDetails(User.getInstance().getUser_id(),"viewreport",viewreport_value);
                db.insertSubDetails(User.getInstance().getUser_id(),"viewhistory",viewhistory_value);
                db.insertSubDetails(User.getInstance().getUser_id(),"customerprofile",customerprofile);
                db.insertSubDetails(User.getInstance().getUser_id(),"multiusers",multiusers);
                db.insertSubDetails(User.getInstance().getUser_id(),"billshare",billshare);
                db.insertSubDetails(User.getInstance().getUser_id(),"managestock",managestock);
                db.insertSubDetails(User.getInstance().getUser_id(),"creditcard",creditcard);
                db.insertSubDetails(User.getInstance().getUser_id(),"qrcode",qrcode);
                db.insertSubDetails(User.getInstance().getUser_id(),"cheque",cheque);


            }catch(Exception e){
                Toast.makeText(ctx,e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }*/

            /*this code is duplicated in Utility function -apiCallBackOtpSuccess*/

           /* sharedPreference.save(ctx, ConstantsUsed.IS_ADMIN, PrivilageData.getInstance().getIs_admin());
            sharedPreference.saveInt(ctx, ConstantsUsed.CREATE_DISCOUNT, PrivilageData.getInstance().getCreate_discount());
            sharedPreference.saveInt(ctx, ConstantsUsed.CREATE_EDIT_PRODUCT,PrivilageData.getInstance().getCreate_edit_product() );
            sharedPreference.saveInt(ctx, ConstantsUsed.MANAGE_STOCK,PrivilageData.getInstance().getManage_stock());
            sharedPreference.saveInt(ctx, ConstantsUsed.VIEW_ALL_TAX,PrivilageData.getInstance().getView_all_tx());*/




            db=new DBHelper(ctx);
            db.insertUserDetails(User.getInstance().getEmail_id() ,User.getInstance().getMobile_no(),User.getInstance().getUser_id(),User.getInstance().getClient_id(),"1",1,1);//signed up and becoming an admin


            Intent login = new Intent(ctx, MainActivity.class);
            ctx.startActivity(login);
        }
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(isUser) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }else{
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

    }
}
