package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.interfaces.NonInventoryCreatedListener;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.POJO.User;

public class AddNonInventoryItemDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    Button btnCancel,btnAddCart;
    EditText etName,etAmount;
    Switch swcreateProduct;




    public AddNonInventoryItemDialog(Activity a) {
        super(a);
        this.activity = a;

    }

    NonInventoryCreatedListener listener;
    public void setListener(NonInventoryCreatedListener listener){
        this.listener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_non_inventory);

        //this.setTitle("ADD NON INVENTORY ITEM");
        btnCancel=findViewById(R.id.id_btn_cancel);
        btnCancel.setOnClickListener(this);

        btnAddCart=findViewById(R.id.btn_confirm);
        btnAddCart.setOnClickListener(this);

        swcreateProduct=findViewById(R.id.id_create_switch);
        swcreateProduct.setOnClickListener(this);

        etName=findViewById(R.id.et_name);
        etAmount=findViewById(R.id.et_amount);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_btn_cancel:
                this.cancel();
                break;

            case R.id.btn_confirm:
                addToCart();
                break;

            default:
                break;
        }
    }

    private void  addToCart(){
        DBHelper db=new DBHelper(activity);

        String title="NON-INVENTRY";
        String productId=  User.getInstance().getClient_id() + ConstantsUsed.MOBILE + AppFeatures.getTimeStamp();

        if(!etAmount.getText().toString().isEmpty() && !etName.getText().toString().isEmpty()) {

            //if(!etName.getText().toString().isEmpty())
            title=etName.getText().toString();
            Product product = new Product(title, Double.parseDouble(etAmount.getText().toString()),Double.parseDouble(etAmount.getText().toString()), "NO_IMAGE",
                    productId, 0, 0,"0",1.0,"");
            this.listener.itemAddedToCart(product);
            String userId = SharedPreference.getInstance().getValue(getContext(), Constants.USER_ID);

            if(swcreateProduct.isChecked()){
                db.insertProductDetails(userId,productId,title,title,"0","1","" +
                                "" +
                        "","","",Double.parseDouble(etAmount.getText().toString()),0.0,AppFeatures.getTimeStamp("dateOnly")
                        ,"NO_IMAGE",1,1,1);
                this.listener.itemCreated(product);
            }

            this.cancel();
        }else if(etAmount.getText().toString().isEmpty()){
            etAmount.setHint("ENTER AMOUNT");
            etAmount.setHintTextColor(Color.RED);
        }else if(etName.getText().toString().isEmpty()){
            etName.setHint("ENTER NAME");
            etName.setHintTextColor(Color.RED);
        }

    }
}
