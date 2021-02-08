package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.POJO.ProductDetailsListGrid;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.interfaces.CountChangedListener;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.Validator;

public class NumberSelectorDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    public Button btnConfirm,btnCancel,btnEdit;
    EditText et_count,edit_price;
    double count=0;
    public static double lastCount=0.0;
    Product product =null;
    ProductDetailsListGrid details=null;
    int flag=0;
    EditText stockEdt;
    double stock=0.0;
    TextView countLabel;


    public NumberSelectorDialog(Activity a, Product p,int flag) {//flag=0
        super(a);
        this.activity = a;
        this.product =p;
        this.flag=flag;
        lastCount=0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_number_selector);
         edit_price=findViewById(R.id.edit_price);
        btnCancel=findViewById(R.id.id_btn_cancel);
        btnConfirm=findViewById(R.id.btn_confirm);
        stockEdt=findViewById(R.id.stock_et);
        countLabel=findViewById(R.id.et_count_label);
        et_count= findViewById(R.id.et_count);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        stock=product.getStockInHand();

        SharedPreference sharedPreference = SharedPreference.getInstance();
        if(sharedPreference.getInt(activity,ConstantsUsed.CREATE_EDIT_PRODUCT)==0) {
            edit_price.setEnabled(false);
        }
        if(sharedPreference.getInt(activity,ConstantsUsed.MANAGE_STOCK)==0) {
            stockEdt.setEnabled(false);
        }

        if(product.getUnit_of_measure().equalsIgnoreCase("0")){
            et_count.setVisibility(View.GONE);
            countLabel.setVisibility(View.GONE);
        }


        if(flag==0) {
            stockEdt.setHint(String.valueOf(stock));
            lastCount = Product.getSelectedItemCountWdProductId(product.getProduct_id());
            edit_price.setHint(String.valueOf(product.getUnit_selling_price()));
            et_count.setHint(String.valueOf(Product.getSelectedItemCountWdProductId(product.getProduct_id())));
        }else if(flag==1){
            stockEdt.setHint(String.valueOf(stock));
            lastCount = Product.getSelectedItemCountWdProductId(details.getProductId());
            edit_price.setHint(String.valueOf(product.getUnit_selling_price()));
            et_count.setHint(String.valueOf(Product.getSelectedItemCountWdProductId(details.getProductId())));
        }

    }

    CountChangedListener listener;
    public void setListener(CountChangedListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          /*  case R.id.btn_decrease_count:
                if(count>=1) {
                    this.listener.notifyCountDecreased(1);
                    count = count-1;
                    lastCount=count;
                    if(flag==0)
                        et_count.setText(String.valueOf(Product.getSelectedItemCountWdProductId(product.getProduct_id())));
                    else if(flag==1)
                        et_count.setText(String.valueOf(Product.getSelectedItemCountWdProductId(details.getProductId())));

                }
                break;
            case R.id.btn_increase:
                if(count>=0) {
                    this.listener.notifyCountIncreased(1);
                    count = count+1;
                    lastCount=count;
                    if(flag==0)
                        et_count.setText(String.valueOf(Product.getSelectedItemCountWdProductId(product.getProduct_id())));
                    else if(flag==1)
                        et_count.setText(String.valueOf(Product.getSelectedItemCountWdProductId(details.getProductId())));
                }
                break;*/

            case R.id.btn_confirm:
                if(!et_count.getText().toString().isEmpty()){
                    count=Integer.parseInt(et_count.getText().toString());

                    if(count>=0 && count>lastCount){
                        this.listener.notifyCountIncreased(count-lastCount);
                    }else if(count>=0 && count<lastCount){
                        this.listener.notifyCountDecreased(lastCount-count);
                    }else{
                        Validator.showToast(getContext(),"An Error");
                    }
                    lastCount=count;
                }

                if(!edit_price.getText().toString().isEmpty()){
                  this.listener.priceChanged(Double.parseDouble(edit_price.getText().toString()));
                }

                if(!stockEdt.getText().toString().isEmpty()){
                    if(Double.parseDouble(stockEdt.getText().toString())>0.0){
                        this.listener.stockChanged(Double.parseDouble(stockEdt.getText().toString())+stock);
                    }
                }

                this.cancel();

                break;
            case R.id.id_btn_cancel:
                this.cancel();
                break;


           /* case R.id.id_btn_edit:
                this.listener.notifyEditBtnClicked();
                break;*/
            default:
                break;
        }
    }

}