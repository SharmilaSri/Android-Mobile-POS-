package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.retailx.dreamdx.retailx.POJO.ChequeDetails;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.interfaces.ChequeInfoAddedListener;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.util.Calendar;

public class AddCheckInfoDialog extends Dialog implements
        View.OnClickListener {

    public Activity activity;
    Button btnCancel,btnAddCart,btnDatePicker;
    EditText etChequeNo,etAmount,etChequeBank;


    public AddCheckInfoDialog(Activity a) {
        super(a);
        this.activity = a;

    }

    ChequeInfoAddedListener listener;
    public void setListener(ChequeInfoAddedListener listener){
        this.listener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cheque_info);

        //this.setTitle("ADD NON INVENTORY ITEM");
        btnCancel=findViewById(R.id.id_btn_cancel);
        btnCancel.setOnClickListener(this);

        btnAddCart=findViewById(R.id.btn_confirm);
        btnAddCart.setOnClickListener(this);

        btnDatePicker=findViewById(R.id.cheque_date);
        btnDatePicker.setOnClickListener(this);

        etChequeNo=findViewById(R.id.et_name);
        etAmount=findViewById(R.id.et_amount);
        etChequeBank=findViewById(R.id.et_cheque_bank);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_btn_cancel:
                this.cancel();
                break;

            case R.id.btn_confirm:

                if(!etChequeNo.getText().toString().isEmpty() &&
                !etAmount.getText().toString().isEmpty() &&
                !etChequeBank.getText().toString().isEmpty() &&
                !btnDatePicker.getText().toString().equalsIgnoreCase("Cheque date")) {

                    ChequeDetails obj = ChequeDetails.getInstance();
                    obj.setChequeAmount(Double.parseDouble(etAmount.getText().toString()));
                    obj.setChequeBank(etChequeBank.getText().toString());
                    obj.setChequeNo(etChequeNo.getText().toString());
                    obj.setChequeDate(btnDatePicker.getText().toString());

                    listener.checkInfoAdded(obj);
                    this.cancel();
                }else{
                    if(etChequeNo.getText().toString().isEmpty()){
                        Validator.showToast(getContext(),"EMPTY Cheque Number");
                    }else if (etAmount.getText().toString().isEmpty()){
                        Validator.showToast(getContext(),"EMPTY Amount");
                    }else if(etChequeBank.getText().toString().isEmpty()){
                        Validator.showToast(getContext(),"EMPTY Cheque Bank");
                    }else if(btnDatePicker.getText().toString().equalsIgnoreCase("Cheque date")){
                        Validator.showToast(getContext(),"EMPTY Date");
                    }

                }
                break;

            case R.id.cheque_date:
                showDatePicker();
                break;


            default:
                break;
        }
    }

    DatePickerDialog datePickerDialog;
    public void showDatePicker(){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        String date=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);

        datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            btnDatePicker.setText(String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

}
