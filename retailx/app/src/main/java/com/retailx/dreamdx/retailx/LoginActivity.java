package com.retailx.dreamdx.retailx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.POJO.User;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout passwordLayout;
    TextInputEditText emailTxt;
    EditText pwTxt;
    Spinner spinner;
    Locale myLocale;
    String currentLanguage = "en", currentLang;

    /** TO BE REMOVED **/
    DBHelper db;
    Cursor infoCur = null;
    Cursor productDetCur=null;

    String emailDb="-1";
    String passwordDb="-1";
    String userIdDb="-1";
    String clientIdDb="-1";
    String phoneNoDb="-1";
    /*****/

    SharedPreference sharedPreference = SharedPreference.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_login);

        db=new DBHelper(this);
        emailTxt =findViewById(R.id.id_username);
        pwTxt=findViewById(R.id.id_pw);

        passwordLayout = findViewById(R.id.login_passwordLabel);

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

        getValuesFromDb();


    }


    private void getValuesFromDb(){

        try {
            infoCur = db.getUSerDetails();
            while (infoCur.moveToNext()) {
                emailDb= infoCur.getString(infoCur.getColumnIndex(ConstantsUsed.COLUMN_USER_EMAIL));
                passwordDb= infoCur.getString(infoCur.getColumnIndex(ConstantsUsed.COLUMN_PASSWORD));
                userIdDb= infoCur.getString(infoCur.getColumnIndex(ConstantsUsed.COLUMN_USER_ID));
                clientIdDb= infoCur.getString(infoCur.getColumnIndex(ConstantsUsed.COLUMN_USER_CLIENT_ID));
                phoneNoDb= infoCur.getString(infoCur.getColumnIndex(ConstantsUsed.COLUMN_USER_PHONE_NUMBER));

                User user = User.getInstance();
                user.setClient_id(clientIdDb);
                user.setEmail_id(emailDb);
                user.setMobile_no(phoneNoDb);
                user.setUser_id(userIdDb);
                User.getInstance().setInstance(user);



            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }finally {


        }
    }

    public void forgotpassword(View v){
        Intent forgotPw = new Intent(this, SubmitEmailActivity.class);
        startActivity(forgotPw);
    }

    public void setLocale(String localeName) {
            SharedPreference.getInstance().save(this, "language",localeName);
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, LoginActivity.class);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);

    }

 /*   private void checkVersion() {
         versionChecker = new VersionChecker();
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            String gradleVersion = pInfo.versionName;

            String playStoreVersion = versionChecker.execute().get();
            Log.e("Version_Code", playStoreVersion + " Gradle_Version " + gradleVersion);

            version = (TextView) findViewById(R.id.version);
            version.setText(gradleVersion);

            if (!(gradleVersion.equalsIgnoreCase(playStoreVersion))) {
                showVersionCheckDialog();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {}
    }*/


    public void showVersionCheckDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setTitle("Update Check");
        builder.setMessage("A New version of Budget Taxi is Available for your best experience");
        builder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });
        // Set other dialog properties

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    String date="";
    int totalSaleLastTime =0;
    int openingBalLastTime=0;
    public static  int TOTAL_REVENUE_LAST_TIME=0;
    public void loginClick(View v){


        final String email= emailTxt.getText().toString().trim();
        String password=pwTxt.getText().toString().trim();

       if(email.isEmpty()) {
           emailTxt.setError(getResources().getString(R.string.empty_field));

       }else if(password.isEmpty()){
           passwordLayout.setError(getResources().getString(R.string.empty_field));
           pwTxt.setHintTextColor(Color.RED);

       }else if (!Validator.validateEmail(email)) {
           emailTxt.setText("");
           emailTxt.setHint("INVALID EMAIL");
           emailTxt.setHintTextColor(Color.RED);
       }else{
           /** TO BE REMOVED **/
           if(email.equalsIgnoreCase(emailDb) && password.equalsIgnoreCase(passwordDb)) {
               sharedPreference.save(this, Constants.CLIENT_ID, User.getInstance().getClient_id());
               sharedPreference.save(this, Constants.EMAIL, User.getInstance().getEmail_id());
               sharedPreference.save(this, Constants.PHONE_NUMBER, User.getInstance().getMobile_no());
               sharedPreference.save(this, Constants.USER_ID, User.getInstance().getUser_id());

               startActivity(new Intent(this, MainActivity.class));
           }else{
               Toast.makeText(this,"Incorrect email or Password",Toast.LENGTH_LONG).show();
           }

         /*****/

           /*if(UtilityFunctions.isInternetAvailable() || UtilityFunctions.isNetworkConnected(this)){

               ApiCalls apicalls=new VolleyCalls();
               Map<String,String> params = new HashMap<String, String>();
               params.put(ConstantsUsed.EMAIL_ID,userName);
               params.put(ConstantsUsed.PASSWORD,password);
               apicalls.connectToNetworkPost(this, ConstantsUsed.URL_LOGIN,ConstantsUsed.TYPE_LOGIN,params);

           }
           else{
               Validator.showToast(this,"NO INTERNET");
           }*/
       }

    }



    public void moveToSignUp(View v){
        if(userIdDb.equalsIgnoreCase("-1")){
            Intent intent=new Intent(this,SignUpActivity.class);
            intent.putExtra("IS_USER",false);
            startActivity(intent);
        }else{
            Toast.makeText(LoginActivity.this,"Cannot sign Up twice",Toast.LENGTH_LONG).show();
        }


    }

    public void skip(View v){
        SharedPreference.getInstance().save(this, Constants.USER_ID,"-1");
        Intent sign=new Intent(this,MainActivity.class);
        startActivity(sign);
    }
}
