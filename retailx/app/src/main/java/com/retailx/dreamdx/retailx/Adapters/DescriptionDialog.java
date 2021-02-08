package com.retailx.dreamdx.retailx.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.interfaces.DescriptionDialogListener;

public class DescriptionDialog extends Dialog implements
        View.OnClickListener {

    public Context activity;
    String desc;
    String btnText;



    public DescriptionDialog(Context a, String description,String btnTxt) {
        super(a);
        this.activity = a;
        desc=description;
        btnText=btnTxt;


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_description);

    }

    DescriptionDialogListener listener;
    public void setListener(DescriptionDialogListener listener){
        this.listener=listener;
    }


    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_btn_cancel:
                this.cancel();
                break;

            case R.id.btn_confirm:
                listener.setAsTrue();
                break;


            default:
                break;
        }
    }

}
