package com.retailx.dreamdx.retailx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.POJO.SubscriptionType;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;

public class PaymentScreenActivity extends AppCompatActivity {

    TextView labelType,labelAmount,labelTotal,labelTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_payment_screen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment");

        labelType=findViewById(R.id.labelTxtType);
        labelAmount=findViewById(R.id.labelTxtAmount);
        labelTotal=findViewById(R.id.labelTxtTotal);
        labelTotalAmount=findViewById(R.id.labelTxtTotalAmount);

        labelType.setText("Selected ("+SubscriptionType.getInstance().getTile()+"):");

        labelAmount.setText(String.valueOf(SubscriptionType.getInstance().getPrice())+"/="+" * "+String.valueOf(SubscriptionType.getInstance().getSelectedMonth())+"months");

        labelTotal.setText("Total :");


        labelTotalAmount.setText(String.valueOf(SubscriptionType.getInstance().getSelectedMonth()*SubscriptionType.getInstance().getPrice())+
                "/=");
    }
}
