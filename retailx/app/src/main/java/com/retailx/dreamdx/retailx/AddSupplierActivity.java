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
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;

public class AddSupplierActivity extends AppCompatActivity {

    String number="";
    String name="";
    TextInputEditText edtName;
    EditText edtNumber;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_add_supplier);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add/Edit");

        setUpView();
    }

    public void setUpView(){
        db=new DBHelper(this);

        edtNumber=findViewById(R.id.id_cus_number);
        edtName=findViewById(R.id.id_cus_name);
    }


    public  void saveCustomer(View v){
        try {
            if (!edtName.getText().toString().isEmpty() ) {

                String supId= AppFeatures.getTimeStamp();
                String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);


                name=edtName.getText().toString();

                if(!edtNumber.getText().toString().isEmpty())
                    number=edtNumber.getText().toString();
                else
                    number="-";

                db.insertSupplierDetails(userId,0,supId,name, number);
                ListType.getInstance().setType("SUP");
                startActivity(new Intent(this, ListItemActivity.class));
            } else {
                if (edtName.getText().toString().isEmpty()) {
                    edtName.setError(getResources().getString(R.string.empty_field));
                    edtName.setHintTextColor(Color.RED);
                } /*else {
                    edtNumber.setHint("EMPTY FIELDS");
                    edtNumber.setHintTextColor(Color.RED);
                }*/
            }
        }catch (Exception e){
            Validator.showToast(this,"Error");
        }finally {


        }
    }

    public void addFromContacts(View v){
        Intent intent = new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data != null && requestCode==1)
        {
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
