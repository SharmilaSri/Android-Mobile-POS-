package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.interfaces.DiscountAddedListener;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.text.DecimalFormat;

public class AddDiscountDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    Double currentValue=0.0;
    TextView tvCurrentValue;
    EditText etDiscountAmount,etDiscountPercentage;
    Button btnConfirm,btnCancel;
    double discountValue=0.0;
    double tempValueValue=0.0;
    double discountPercentage=0.0;

    public AddDiscountDialog(Activity a,double currentValue) {
        super(a);
        this.activity = a;
        this.currentValue=currentValue;

    }

    boolean isTypingPercentage=false;
    boolean isTypingAmount=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_discount);
        tvCurrentValue=findViewById(R.id.current_value);
        etDiscountAmount=findViewById(R.id.et_amount_discount);
        etDiscountPercentage=findViewById(R.id.et_amount_percentage_discount);

        etDiscountPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(!isTypingAmount && !isTypingPercentage) {

                    isTypingPercentage = true;
                    isTypingAmount = false;
                }
                // Toast.makeText(SaveTransactionActivity.this,"before",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(isTypingPercentage && !isTypingAmount) {
                    try {
                        discountPercentage = Double.parseDouble(etDiscountPercentage.getText().toString());
                        discountValue = (discountPercentage / 100) * Product.getSelectedProductTotal();
                        tempValueValue = currentValue - discountValue;
                        tvCurrentValue.setText(String.valueOf(tempValueValue));
                        etDiscountAmount.setText(String.valueOf(discountValue));

                    } catch (Exception e) {
                        //Toast.makeText(SaveTransactionActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(etDiscountPercentage.getText().toString().isEmpty()) {
                    etDiscountAmount.setText("0.0");
                    tvCurrentValue.setText(String.valueOf(currentValue));
                    discountValue=0.0;
                    tempValueValue=currentValue;
                }

                isTypingPercentage=false;

            }
        });


        etDiscountAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(!isTypingAmount && !isTypingPercentage) {

                    isTypingAmount = true;
                    isTypingPercentage = false;
                }
                // Toast.makeText(SaveTransactionActivity.this,"before",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isTypingAmount && !isTypingPercentage) {
                    try {

                        discountValue = Double.parseDouble(etDiscountAmount.getText().toString());
                        tempValueValue = currentValue - discountValue;
                        tvCurrentValue.setText(String.valueOf(tempValueValue));

                        if (discountValue > 0.0 && currentValue > 0.0) {
                            discountPercentage = (discountValue / currentValue) * 100;
                        }

                        etDiscountPercentage.setText(String.valueOf(new DecimalFormat("##.##").format(discountPercentage)));

                    } catch (Exception e) {
                        //Toast.makeText(SaveTransactionActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(etDiscountAmount.getText().toString().isEmpty()) {
                    etDiscountPercentage.setText("0.0");
                    tvCurrentValue.setText(String.valueOf(currentValue));
                    discountValue=0.0;
                    tempValueValue=currentValue;
                }

                isTypingAmount=false;

            }
        });

        btnConfirm=findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

        btnCancel=findViewById(R.id.id_btn_cancel);
        btnCancel.setOnClickListener(this);

        tvCurrentValue.setText(String.valueOf(AppFeatures.format(currentValue)));

    }

    DiscountAddedListener listener;
    public void setListener(DiscountAddedListener listener){
        this.listener=listener;
    }


    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_btn_cancel:
                this.cancel();
                break;

            case R.id.btn_confirm:

                if(!etDiscountAmount.getText().toString().isEmpty()) {

                    if(tempValueValue>0 ) {
                        this.listener.discountAdded(discountValue, tempValueValue,discountPercentage);
                        this.cancel();
                    }else{
                        /*etDiscountAmount.setText("");
                        etDiscountAmount.setHintTextColor(Color.RED);
                        etDiscountAmount.setHint("INCORRECT VALUE");*/
                        Validator.showToast(getContext(),"INCORRECT DISCOUNT AMOUNT");
                    }
                }else{
                    /*etDiscountAmount.setHintTextColor(Color.RED);
                    etDiscountAmount.setHint("EMPTY FIELDS");*/
                    Validator.showToast(getContext(),"EMPTY DISCOUNT AMOUNT");
                }
                break;


            default:
                break;
        }
    }

}
