package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.interfaces.NonInventoryCreatedListener;

public class SubscriptionDialog extends Dialog implements
        View.OnClickListener {

    public Activity activity;
    Button btnCancel,btnAddCart;
    EditText etName,etAmount;
    Switch swcreateProduct;




    public SubscriptionDialog(Activity a) {
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
        setContentView(R.layout.activity_subscription);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_btn_cancel:
                this.cancel();
                break;

            case R.id.btn_confirm:
                break;

            default:
                break;
        }
    }

}
