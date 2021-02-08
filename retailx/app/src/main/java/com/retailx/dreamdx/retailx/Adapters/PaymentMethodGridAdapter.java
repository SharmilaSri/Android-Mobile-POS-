package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.POJO.PaymentMethod;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.interfaces.PaymentMethodSelectedListener;

import java.util.ArrayList;

public class PaymentMethodGridAdapter
        extends ArrayAdapter {


    private final Activity context;
    private  ArrayList<PaymentMethod> pMethod;


    public PaymentMethodGridAdapter(Activity context, ArrayList<PaymentMethod> pMethod) {
        super(context, R.layout.grid_item_payment_method, pMethod);

        this.context=context;
        this.pMethod=pMethod;

    }
    double total=0.00;
    PaymentMethodSelectedListener listener;
    public void setListener(PaymentMethodSelectedListener listener){
        this.listener=listener;
    }



    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.grid_item_payment_method, null,true);

        TextView payMethodNameTxt = (TextView) rowView.findViewById(R.id.id_txt_paymethod);
        ImageView payImageview=rowView.findViewById(R.id.id_img_payimage);
        payMethodNameTxt.setText(pMethod.get(position).getMethodName());
        payImageview.setImageDrawable(context.getResources().getDrawable(pMethod.get(position).getImageDrawable()));

        LinearLayout gridLayoutItem=rowView.findViewById(R.id.id_grid_payment_method_layout);
        gridLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.notifyPaymentMethodSelected(pMethod.get(position).getMethodName());

            }
        });

        return rowView;

    };

}
