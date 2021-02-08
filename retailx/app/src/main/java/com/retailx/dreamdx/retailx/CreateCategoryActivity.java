package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.common.api.CommonStatusCodes;

import com.retailx.dreamdx.retailx.POJO.CreateOrEditFlag;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;


public class CreateCategoryActivity extends AppCompatActivity {

    DBHelper db;
    EditText edtTitle,edtDesc;
    String catId="0";
    String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_create_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.category));

        db=new DBHelper(this);


        edtTitle=(EditText) findViewById(R.id.id_categoryname);
        edtDesc=(EditText)findViewById(R.id.id_categorydesc);

        displayValuesInViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return true;
    }


    public void categoryDiscard(View v){
        if(CreateOrEditFlag.getInstance().getFlag()==3)
            startActivity(new Intent(this,EditProduct.class));
        else  if(CreateOrEditFlag.getInstance().getFlag()==1)
            startActivity(new Intent(this,CreateProductActivity.class));
         else
            startActivity(new Intent(this,MainActivity.class));
    }

    public void insertCategoryDetails(View v){

        String txtCatTilte=edtTitle.getText().toString();
        String txtCatDesc=edtDesc.getText().toString();
        txtCatDesc="0";

        try{
            if(!txtCatTilte.isEmpty() ){
                String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);
                String date=AppFeatures.getTimeStamp();

                if(catId.equalsIgnoreCase("0")) {
                    catId =  ConstantsUsed.CATEGORY + AppFeatures.getTimeStamp();
                    db.insertCategoryDetails(userId, catId, txtCatTilte.toUpperCase(), txtCatDesc, "0", 1, 1);
                }else {
                    //Validator.showToast(this,catId+"update"+txtCatTilte);
                    db.updateCategoryDetails(txtCatTilte.toUpperCase(), catId,1);
                }


            }else{
                edtTitle.setHint("EMPTY FIELD");
                edtTitle.setHintTextColor(Color.RED);
               // Toast.makeText(this,"EMPTY FIELDS",Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
             Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();

        }finally {
            db.close();

           navigateBack();
        }


    }


    private void navigateBack(){
        if(CreateOrEditFlag.getInstance().getFlag()==1){
            startActivity(new Intent(this,MainActivity.class));
        }else if(CreateOrEditFlag.getInstance().getFlag()==3){
             startActivity(new Intent(this,EditProduct.class));
        }else {
            Intent data = new Intent(this, CreateProductActivity.class);
            this.setResult(CommonStatusCodes.SUCCESS, data);
            this.finish();
        }
    }

    private void displayValuesInViews(){

        try {
            Bundle extras = getIntent().getExtras();
            catId = extras.getString(ConstantsUsed.EDIT_CAT_ID,"0");
            title = extras.getString(ConstantsUsed.EDIT_CAT_KEY,"");

        } catch (Exception e) {
            //Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }finally {
            if(!title.isEmpty())
                edtTitle.setText(title);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateBack();
                break;

            case R.id.action_delete:
                if(!catId.isEmpty()) {
                    db.deleteCategoryWdId(catId);
                    startActivity(new Intent(this,MainActivity.class));
                }
                break;
        }
        return true;
    }



    @Override
    public void onBackPressed() {
        if (true) {
            Intent data = new Intent(this,CreateProductActivity.class);
            this.setResult(CommonStatusCodes.SUCCESS, data);
            this.finish();
            //startActivity(new Intent(this,CreateProductActivity.class));
        } else {
            super.onBackPressed();
        }
    }
}
