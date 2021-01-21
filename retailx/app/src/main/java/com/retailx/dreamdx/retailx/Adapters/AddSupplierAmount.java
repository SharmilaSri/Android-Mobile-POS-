package com.retailx.dreamdx.retailx.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.interfaces.SupplierAmountChangedListener;

public class AddSupplierAmount extends Dialog implements
        View.OnClickListener {

    public Context activity;
    Double currentValue=0.0;
    TextView tvCurrentValue;
    EditText etAmount,etAmountReset;
    Button btnConfirm,btnCancel;
    double discountValue=0.0;
    double tempValueValue=0.0;
    double discountPercentage=0.0;
    String id="";

    public AddSupplierAmount(Context a, String id) {
        super(a);
        this.activity = a;
        this.id=id;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_supplier_amount);
        etAmount=findViewById(R.id.et_amount_sup);
        etAmountReset=findViewById(R.id.et_amount_reset);


        btnConfirm=findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

        btnCancel=findViewById(R.id.id_btn_cancel);
        btnCancel.setOnClickListener(this);

    }

    SupplierAmountChangedListener listener;
    public void setListener(SupplierAmountChangedListener listener){
        this.listener=listener;
    }


    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_btn_cancel:
                this.cancel();
                break;

            case R.id.btn_confirm:

                if(!etAmount.getText().toString().isEmpty()) {
                    this.listener.amountAdded(Double.parseDouble(etAmount.getText().toString()));
                    this.cancel();
                }
                else{
                    etAmount.setHintTextColor(Color.RED);
                    etAmount.setHint("EMPTY FIELDS");
                }

                if(!etAmountReset.getText().toString().isEmpty()) {
                    this.listener.amountReset(Double.parseDouble(etAmountReset.getText().toString()));
                    this.cancel();
                }

                break;

            default:
                break;
        }
    }
}
