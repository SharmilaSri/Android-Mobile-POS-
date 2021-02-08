package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.POJO.ProductDetailsListGrid;
import com.retailx.dreamdx.retailx.POJO.TransactionDeatils;
import com.retailx.dreamdx.retailx.POJO.TransactionSummary;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.SaveTransactionActivity;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

import java.util.ArrayList;

public class transactionDetailsDialog extends Dialog implements
        View.OnClickListener {

    public Activity activity;
    Button btnCancel,btnprint;
    DBHelper Db;
    ArrayList<ProductDetailsListGrid> list;
    TextView txtTransId,txtTransDate,txtTransTotalAmount,txtchequeId,txtCustName;
    TransactionSummary transSummaryObj;
    Spinner spinner;
    String chequeId="";
    String chequeStatus="";
    String creditStatus="";
    String creditId="";
    LinearLayout linear_cheque;



    public transactionDetailsDialog(Activity a,TransactionSummary obj) {
        super(a);
        this.activity = a;
        transSummaryObj=obj;
        Db=new DBHelper(this.activity);

    }



    ListView listView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<TransactionDeatils> detailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_transaction_details);

        listView=findViewById(R.id.exp_trans_details);
        txtTransId=findViewById(R.id.transactionId);
        txtTransDate=findViewById(R.id.transactionDate);
        txtTransTotalAmount=findViewById(R.id.transTotal);
        txtchequeId=findViewById(R.id.checqueId);
        txtCustName=findViewById(R.id.custName);
        linear_cheque=findViewById(R.id.linear_cheque);
        linear_cheque.setVisibility(View.GONE);
        txtTransDate.setText(transSummaryObj.getTransactionDate());
        txtTransId.setText("Id:"+transSummaryObj.getTransactionId());
        txtTransTotalAmount.setText("Total:"+transSummaryObj.getTransactionTotalAmount());
        btnCancel=findViewById(R.id.id_btn_cancel);
        btnCancel.setOnClickListener(this);
        btnprint=findViewById(R.id.print_history);
        btnprint.setOnClickListener(this);

        spinner = (Spinner) findViewById(R.id.id_cheque_status);

        layoutManager = new LinearLayoutManager(activity);

        detailList=new ArrayList<>();
        list=new ArrayList<>();

        Cursor detailsCur=null;
        Cursor chequeDetailsCur=null;
        Cursor creditDetailsCur=null;
        Cursor productTitleCur=null;
        String productTitle="";
        Double unitPrice=0.0;
        try {
            detailsCur=Db.getAllTransactionDetails(transSummaryObj.getTransactionId());

            while (detailsCur.moveToNext()) {
                String productId = detailsCur.getString(detailsCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_ID)));
                Double total = detailsCur.getDouble(detailsCur.getColumnIndex((ConstantsUsed.COLUMN_TOTAL_ITEM_AMOUNT)));
                double transItem = detailsCur.getDouble(detailsCur.getColumnIndex((ConstantsUsed.COLUMN_TOTAL_ITEM_UNITS)));

                productTitleCur=Db.getProductTitleWithId(productId);
                while (productTitleCur.moveToNext()) {
                    productTitle = productTitleCur.getString(productTitleCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_TITLE)));
                    unitPrice = productTitleCur.getDouble(productTitleCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_SELLING_PRICE)));

                }

                list.add(new  ProductDetailsListGrid(transItem,total,productTitle,unitPrice,0.00,productId,0,0,0.0,"1",0,""));//to be changed

            }


        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        } finally {
            if (detailsCur != null)
                detailsCur.close();

            if (productTitleCur != null)
                productTitleCur.close();
        }

        try{
            chequeDetailsCur=Db.getChequesDeatails(transSummaryObj.getTransactionId());

            while (chequeDetailsCur.moveToNext()) {
                 chequeId = chequeDetailsCur.getString(chequeDetailsCur.getColumnIndex((ConstantsUsed.COLUMN_CHEQUE_ID)));
                 chequeStatus = chequeDetailsCur.getString(chequeDetailsCur.getColumnIndex((ConstantsUsed.COLUMN_CHEQUE_STATUS)));

            }
        }catch (Exception e){
            Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();

        }finally {
            if(chequeDetailsCur !=null)
                chequeDetailsCur.close();
            if(!chequeId.isEmpty()) {
                txtchequeId.setText("Change Status of "+chequeId);
            }

            if(!chequeStatus.isEmpty()) {
                linear_cheque.setVisibility(View.VISIBLE);
                ArrayAdapter<CharSequence> sinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.cheque_status, android.R.layout.simple_spinner_item);
                sinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(sinnerAdapter);

                int position=0;
                if (chequeStatus.equalsIgnoreCase("PENDING"))
                    position=0;
                else if(chequeStatus.equalsIgnoreCase("BANKED"))
                    position=1;
                else if(chequeStatus.equalsIgnoreCase("CLEARED"))
                    position=2;
                else if(chequeStatus.equalsIgnoreCase("ENR"))
                    position=3;
                else if(chequeStatus.equalsIgnoreCase("RD"))
                    position=4;
                else if(chequeStatus.equalsIgnoreCase("TECHNICAL ERROR"))
                    position=5;
                else if(chequeStatus.equalsIgnoreCase("EXPIRED"))
                    position=6;
                spinner.setSelection(position);
            }
        }


        try{
            creditDetailsCur=Db.getCreditDetails(transSummaryObj.getTransactionId());

            while (creditDetailsCur.moveToNext()) {
                creditStatus = creditDetailsCur.getString(creditDetailsCur.getColumnIndex((ConstantsUsed.COLUMN_CREDIT_STATUS)));
                creditId = creditDetailsCur.getString(creditDetailsCur.getColumnIndex((ConstantsUsed.COLUMN_CREDIT_ID)));

            }
        }catch (Exception e){
            Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();

        }finally {
            if(creditDetailsCur !=null)
                creditDetailsCur.close();


            if(!creditStatus.isEmpty()) {
                linear_cheque.setVisibility(View.VISIBLE);
                ArrayAdapter<CharSequence> sinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.credit_status, android.R.layout.simple_spinner_item);
                sinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(sinnerAdapter);
                int position=0;
                if (creditStatus.equalsIgnoreCase("PENDING"))
                    position=0;
                else if(creditStatus.equalsIgnoreCase("CLEARED"))
                    position=1;

                spinner.setSelection(position);
            }
        }
        DetailsListAdapter adapter = new DetailsListAdapter(activity, list,"1");//flag 1 to hide delete button

        listView.setAdapter(adapter);


        displayCustomerDetails();

    }

    public void displayCustomerDetails(){
        Cursor custNameCursor=null;
        String custName="";
        try {
            if(!transSummaryObj.getCustId().equalsIgnoreCase("0")) {
                custNameCursor = Db.getCustomerDetailsWdCustomerId(transSummaryObj.getCustId());

                while (custNameCursor.moveToNext()) {
                    custName = custNameCursor.getString(custNameCursor.getColumnIndex((ConstantsUsed.COLUMN_CUSTOMER_NAME)));

                }

            }


        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        } finally {
            if(!custName.isEmpty())
                txtCustName.setText(custName);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_btn_cancel:
                this.cancel();
                if(!chequeStatus.isEmpty()) {
                    int position = 0;
                    position = spinner.getSelectedItemPosition();
                    String status = "PENDING";
                    if (position == 0)
                        status = "PENDING";
                    else if (position == 1)
                        status = "BANKED";
                    else if (position == 2)
                        status = "CLEARED";
                    else if (position == 3)
                        status = "ENR";
                    else if (position == 4)
                        status = "RD";
                    else if (position == 5)
                        status = "TECHNICAL ERROR";
                    else if (position == 6)
                        status = "EXPIRED";

                    Db.updateChequeInfo(chequeId, status);
                }else if(!creditStatus.isEmpty()){
                    int position = 0;
                    position = spinner.getSelectedItemPosition();
                    String status = "PENDING";
                    if (position == 0)
                        status = "PENDING";
                    else if (position == 1)
                        status = "CLEARED";

                    Db.updateCreditInfo(creditId, status);
                }
                break;

            case R.id.print_history:
                this.cancel();
                print();
                break;

            default:
                break;
        }
    }

    private void print(){
        Intent newIntent = new Intent(activity, SaveTransactionActivity.class);
        newIntent.putExtra("TOKEN", transSummaryObj.getTransactionId());
        activity.startActivity(newIntent);

    }

}
