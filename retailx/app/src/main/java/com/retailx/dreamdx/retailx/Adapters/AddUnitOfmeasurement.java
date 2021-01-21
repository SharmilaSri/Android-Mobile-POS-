package com.retailx.dreamdx.retailx.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.interfaces.UnitOfMeasurementChangedListener;

import java.text.DecimalFormat;

public class AddUnitOfmeasurement extends Dialog implements
        View.OnClickListener {

    public Context activity;
    EditText etAmount;
    Button btnConfirm,btnCancel;
    String id="";


    public AddUnitOfmeasurement(Context a, String id) {
        super(a);
        this.activity = a;
        this.id=id;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_unit_measurement_amount);
        etAmount=findViewById(R.id.et_amount);



        btnConfirm=findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

        btnCancel=findViewById(R.id.id_btn_cancel);
        btnCancel.setOnClickListener(this);

        etAmount.setRawInputType(Configuration.KEYBOARD_12KEY);
        etAmount.addTextChangedListener(new TextWatcher() {
            DecimalFormat dec = new DecimalFormat("0.000");
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(!s.toString().matches("^\\ (\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{3})?$")) {

                    String userInput= ""+s.toString().replaceAll("[$,.]", "");
                    if (userInput.length() > 0) {
                        Float in=Float.parseFloat(userInput);
                        float percen = in/1000;
                        etAmount.setText(" "+dec.format(percen));
                        etAmount.setSelection(etAmount.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    UnitOfMeasurementChangedListener listener;
    public void setListener(UnitOfMeasurementChangedListener listener){
        this.listener=listener;
    }


    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_btn_cancel:
                this.cancel();
                break;

            case R.id.btn_confirm:

                if(!etAmount.getText().toString().isEmpty()) {
                    this.listener.unitOfmeasurementAdded(Double.parseDouble(etAmount.getText().toString()));
                    this.cancel();
                }
                else{
                    etAmount.setHintTextColor(Color.RED);
                    etAmount.setHint("EMPTY FIELDS");
                }


                break;


            default:
                break;
        }
    }

}
