package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.retailx.dreamdx.retailx.POJO.ListType;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;

public class AddCustomerActivity extends AppCompatActivity {

    String number="";
    String name="";
    String email="";
    EditText edtNumber,edtEmail;
    TextInputEditText edtName;
    DBHelper db;
    String customerId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_add_customer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.add_edit));


        setUpView();

        displayValuesInViews();
    }


    private void displayValuesInViews(){

        try {
            Bundle extras = getIntent().getExtras();
            customerId = extras.getString("token","");
            if (!customerId.isEmpty()) {
                Cursor infoCur = null;
                try {
                    infoCur = db.getCustomerDetailsWdCustomerId(customerId);
                    while (infoCur.moveToNext()) {
                        name = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_CUSTOMER_NAME)));
                        number=infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_CUSTOMER_NUMBER)));
                        email=infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_CUSTOMER_EMAIL)));


                    }
                }catch (Exception e){
                    // Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }finally{
                    if(!name.isEmpty())
                        edtName.setText(name);

                    if(!number.isEmpty())
                        edtNumber.setText(number);

                    if(!email.isEmpty())
                        edtEmail.setText(email);
                }
            }

        } catch (Exception e) {
            //Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }finally {

        }
    }

    public void setUpView(){
        db=new DBHelper(this);

        edtNumber=findViewById(R.id.id_cus_number);
        edtName=findViewById(R.id.id_cus_name);
        edtEmail=findViewById(R.id.id_cus_email);
    }

    public  void setImage (View v){

    }

    public  void saveCustomer(View v){
        try {
            if (!edtName.getText().toString().isEmpty() ) {

                String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);


                name=edtName.getText().toString();

                if(!edtNumber.getText().toString().isEmpty())
                    number=edtNumber.getText().toString();
                else
                    number="0";

                if(!edtEmail.getText().toString().isEmpty())
                    email=edtEmail.getText().toString();
                else
                    email="0";

                if(customerId.isEmpty()) {
                    customerId=AppFeatures.getTimeStamp();
                    db.insertCustomerDetails(userId, customerId, name, number, email, ConstantsUsed.GENDER_C, "1", 1);
                }else
                    db.updateCustomerDetails(customerId,name, number,email, ConstantsUsed.GENDER_C, "1",1);


                ListType.getInstance().setType("CUST");
                startActivity(new Intent(this, ListItemActivity.class));
            } else {
                if (edtName.getText().toString().isEmpty()) {
                    edtName.setError(getResources().getString(R.string.empty_field));
                    edtName.setHintTextColor(Color.RED);
                }
            }
        }catch (Exception e){
            Validator.showToast(this,"Error"+e.getMessage().toString());
        }finally {


        }
    }

    static int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1000;
    public void addFromContacts(View v){
        /*if (ContextCompat.checkSelfPermission(AddCustomerActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(AddCustomerActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

            } else {
                ActivityCompat.requestPermissions(AddCustomerActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, 1);
        }*/

        Intent intent = new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*if(requestCode==MY_PERMISSIONS_REQUEST_READ_CONTACTS){
            Intent intent = new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, 1);
        }else */if(data != null && requestCode==MY_PERMISSIONS_REQUEST_READ_CONTACTS){
            Uri uri = data.getData();

            if(uri != null)
            {
                Cursor c = null;

                try
                {
                    c = getContentResolver().query(uri, null
                            // new String[] {
                            //ContactsContract.CommonDataKinds.Phone.NUMBER,
                            //ContactsContract.CommonDataKinds.Phone.TYPE},
                            , null, null, null);

                    if(c != null && c.moveToFirst())
                    {
                        number= c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        name = c.getString(0);
                        edtNumber.setText(String.valueOf(number));
                        edtName.setText(name);
                    }
                }catch (Exception e){
                    Validator.showToast(this,"Error");
                }
                finally
                {
                    if(c != null && !c.isClosed())
                        c.close();
                }
            }
        }
    }
}
