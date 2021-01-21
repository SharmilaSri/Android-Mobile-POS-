package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;

public class StockActivity extends AppCompatActivity {

    EditText etStockOnHand,etStockMinimum;
    android.support.design.widget.TextInputLayout labelStock,minStock;
    Button btnSave;
    DBHelper db;

    String productId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_stock);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add/Update Stock");

        setUpViews();

        initialise();

        displayValues();

    }
    double stockHand=0.0;
    double stockMinimum=0.0;

    private void displayValues(){
        if(!productId.isEmpty()){
            Cursor infoCur = null;

            try {
                infoCur = db.getStockDetails(productId);
                while (infoCur.moveToNext()) {
                    stockHand = infoCur.getDouble(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_STOCK_IN_HAND)));
                    stockMinimum=infoCur.getDouble(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_STOCK_MINIMUM)));
                }
            }catch (Exception e){
                 Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }finally{

                if(stockHand>0)
                    labelStock.setHint("Reset Stock");

                etStockOnHand.setText(String.valueOf(stockHand));

                if(stockMinimum>0)
                    labelStock.setHint("Reset Stock Minimum");

                etStockMinimum.setText(String.valueOf(stockMinimum));
            }
        }
    }

    private void setUpViews(){
        db=new DBHelper(StockActivity.this);

        etStockOnHand=findViewById(R.id.edt_stock_in_hand);
        etStockMinimum=findViewById(R.id.et_stock_minimum);
        btnSave=findViewById(R.id.btnSaveStock);

        labelStock=findViewById(R.id.label_enter_stock);
        minStock=findViewById(R.id.labelMinimumStock);

    }

    private void initialise(){
        Bundle extras = getIntent().getExtras();
        String edit = extras.getString(ConstantsUsed.EDIT_PRODUCT_KEY,"false");

        productId = extras.getString(ConstantsUsed.PRODUCT_ID_KEY,"");
    }


    public void saveData(View v) {

        try {
            if (!etStockOnHand.getText().toString().isEmpty()) {
                 stockHand = Double.parseDouble(etStockOnHand.getText().toString());
                if (!productId.isEmpty()) {
                    db.updateProductStockInHand(productId, stockHand);
                }


            }

            if (!etStockMinimum.getText().toString().isEmpty()) {
                 stockMinimum =  Double.parseDouble(etStockMinimum.getText().toString());

                if (!productId.isEmpty()) {
                    db.updateProductStockMinimum(productId, stockMinimum);
                }

            }
        }catch(Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }finally{
            Intent intent=new Intent(StockActivity.this,CreateProductActivity.class);
            intent.putExtra(CreateProductActivity.KEY_STOCK,stockHand);
            intent.putExtra(CreateProductActivity.KEY_STOCK_MIN,stockMinimum);
            this.setResult(CommonStatusCodes.SUCCESS, intent);
            this.finish();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(StockActivity.this,CreateProductActivity.class);
                intent.putExtra(CreateProductActivity.KEY_STOCK,stockHand);
                intent.putExtra(CreateProductActivity.KEY_STOCK_MIN,stockMinimum);
                this.setResult(CommonStatusCodes.SUCCESS, intent);
                this.finish();
                break;
        }
        return true;
    }



    @Override
    public void onBackPressed() {

        Intent intent=new Intent(StockActivity.this,CreateProductActivity.class);
        intent.putExtra(CreateProductActivity.KEY_STOCK,stockHand);
        intent.putExtra(CreateProductActivity.KEY_STOCK_MIN,stockMinimum);
        this.setResult(CommonStatusCodes.SUCCESS, intent);
        this.finish();
    }

}
