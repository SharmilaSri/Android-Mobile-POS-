package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.retailx.dreamdx.retailx.Adapters.DetailsListAdapter;
import com.retailx.dreamdx.retailx.POJO.OpenBalReductionDetails;
import com.retailx.dreamdx.retailx.POJO.OpeningBalance;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.util.ArrayList;

public class OpeningBalActivity extends AppCompatActivity {
    DBHelper db;
    ArrayList<OpenBalReductionDetails> detailsList=new ArrayList<>();
    DetailsListAdapter adapter;
    EditText etOpeningBalance;
    Double openingBalance;
    public static String REVENUE_ID="";
    ListView ltsView;
    TextView reasonTxt;
    public static double ENTERED_OPENING__BALANCE=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_bal);

        db=new DBHelper(OpeningBalActivity.this);
        etOpeningBalance =findViewById(R.id.id_opening_balance);
        etOpeningBalance.setHint("OPENING BAL : "+String.valueOf(LoginActivity.TOTAL_REVENUE_LAST_TIME));
        ltsView=findViewById(R.id.id_opening_bal);
        reasonTxt=findViewById(R.id.labelTxt);

        etOpeningBalance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Toast.makeText(SaveTransactionActivity.this,"before",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    OpeningBalance.getInstance().setEnteredOpeningBal(Double.valueOf(etOpeningBalance.getText().toString()));
                }catch (Exception e){
                    Toast.makeText(OpeningBalActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    public void goToLogin(View v){
        //((TextView) findViewById(R.id.labelTxt)).setVisibility();
        try{
            REVENUE_ID= AppFeatures.getTimeStamp();
            if(etOpeningBalance.getText().toString()!=null && !etOpeningBalance.getText().toString().isEmpty()) {
                openingBalance = Double.parseDouble(etOpeningBalance.getText().toString());
                OpeningBalance.getInstance().setEnteredOpeningBal(openingBalance);

                if( LoginActivity.TOTAL_REVENUE_LAST_TIME!=0 && openingBalance<LoginActivity.TOTAL_REVENUE_LAST_TIME) {
                    ENTERED_OPENING__BALANCE=openingBalance;

                    detailsList.add(new OpenBalReductionDetails("",0.0));
                    adapter=new DetailsListAdapter(this,detailsList,1);
                    ltsView.setVisibility(View.VISIBLE);
                    ltsView.setAdapter(adapter);

                    reasonTxt.setVisibility(View.VISIBLE);
                    reasonTxt.setText("ENTERED AMOUNT IS LESS THAN "+LoginActivity.TOTAL_REVENUE_LAST_TIME +". PLEASE EXPLAIN");

                }else if(LoginActivity.TOTAL_REVENUE_LAST_TIME!=0 && openingBalance>LoginActivity.TOTAL_REVENUE_LAST_TIME) {

                    Toast.makeText(this,"ENTERED AMOUNT IS LESS THAN"+LoginActivity.TOTAL_REVENUE_LAST_TIME +". PLEASE EXPLAIN",Toast.LENGTH_SHORT).show();

                }else if(openingBalance==LoginActivity.TOTAL_REVENUE_LAST_TIME){
                    db.insertOpeningBalInfo(AppFeatures.getTimeStamp("dateOnly"), openingBalance, REVENUE_ID, 0.0, 0.0, 0.0);
                    startActivity(new Intent(this,MainActivity.class));

                }else if(LoginActivity.TOTAL_REVENUE_LAST_TIME==0){//first time entering the app

                    startActivity(new Intent(this,MainActivity.class));

                }else
                    Validator.showToast(this,"Enter Valid opening Balance");
            }else{
                Validator.showToast(this,"Enter Opening Balance");
            }

        }catch(Exception e){

        }finally{

        }
    }

    public void skip(View v){
        startActivity(new Intent(this,MainActivity.class));
    }

    public  void addItem(OpenBalReductionDetails obj){
        detailsList=new ArrayList<>();
        OpenBalReductionDetails.setOpenDetList(obj);
        detailsList.addAll(OpenBalReductionDetails.getOpenDetList());
        detailsList.add(new OpenBalReductionDetails("",0.0));//empty obj for the empty add box
        adapter=new DetailsListAdapter(this,detailsList,1);
        ListView ltsView=findViewById(R.id.id_opening_bal);
        ltsView.setAdapter(adapter);
    }
}
