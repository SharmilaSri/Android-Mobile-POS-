package com.retailx.dreamdx.retailx;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.Adapters.TransactionReceyclerAdapter;
import com.retailx.dreamdx.retailx.POJO.TransactionSummary;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TransHistoryActivity extends AppCompatActivity {

    DBHelper db;
    private static ArrayList<TransactionSummary> transList;
    private static ArrayList<TransactionSummary> transListSearch=new ArrayList<>();
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    android.support.v7.widget.SearchView searchView;
    Button btnTotalAmount,btnDatePicker;
    LinearLayout emptyLL,notEmptyLl;
    TextView paymentType,customerName,dateValue;
    boolean iscashSelected,isCustomerSelected,isdateselected=false;
    LinearLayout layout_linear_search;
    String selectedDate,selectedCustomerId,selectedPayment="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_trans_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("History");

        db=new DBHelper(this);
        searchView= (android.support.v7.widget.SearchView)findViewById(R.id.search_list);
        recyclerView = (RecyclerView) findViewById(R.id.trans_recycler);
        btnDatePicker=findViewById(R.id.datepicker);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        btnTotalAmount=findViewById(R.id.total_amount);
        emptyLL=findViewById(R.id.empty_show);
        notEmptyLl=findViewById(R.id.full_show);
        recyclerView.setHasFixedSize(true);
        paymentType=findViewById(R.id.txt_payment_type);
        customerName=findViewById(R.id.txt_customer);
        dateValue=findViewById(R.id.txt_date);
        layout_linear_search=findViewById(R.id.layout_linear_search);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int textLength=newText.length();
                String tempTitle="";
                transListSearch.clear();


                if(transList!=null && transList.size()>=1){
                    for(int i=0;i<transList.size();i++){
                        tempTitle=transList.get(i).getTransactionId();
                        if(textLength>0 && textLength<=tempTitle.length() && !tempTitle.equalsIgnoreCase("ADD NEW")){
                            if(newText.equalsIgnoreCase(tempTitle.substring(0,textLength)) || tempTitle.contains(newText)){
                                transListSearch.add(transList.get(i));
                            }
                        }else if(textLength==0){
                            populateData("","","");
                        }else{
                            populateData("","","");
                        }


                    }

                    if(transListSearch.size()>0){
                        //transListSearch.add(0,new TransactionSummary("TRANSACTION ID","DATE","QTY","TOTAL"));//check this
                        mAdapter = new TransactionReceyclerAdapter(TransHistoryActivity.this,transListSearch);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        btnTotalAmount.setText("Total:"+ AppFeatures.format(displayTotalAmount(transListSearch
                        )));

                    }
                }


                return false;
            }
        });


        //Show today history only
        cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);

        String dayTxt=String.valueOf(day);
        if (dayTxt.length() == 1) {
            dayTxt = "0" + dayTxt;
        }

        String monthTxt=String.valueOf(month+1);
        if (monthTxt.length() == 1) {
            monthTxt = "0" + monthTxt;
        }

        String date=String.valueOf(year)+"-"+monthTxt+"-"+dayTxt;
        isdateselected=true;
        selectedDate=date;
        populateData(date,"","");
        layout_linear_search.setVisibility(View.VISIBLE);
        dateValue.setVisibility(View.VISIBLE);
        dateValue.setText(selectedDate);

    }

    public void setUpbtnTotal(){
        if (TransactionSummary.getTransList().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyLL.setVisibility(View.VISIBLE);
        } else {
            emptyLL.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        btnTotalAmount.setText("Total:"+ AppFeatures.format(displayTotalAmount(TransactionSummary.getTransList())));
    }

    double total=0.0;
    public void populateData(String date,String payemtnType,String custId) {
        Cursor transListCur = null;
        transList = new ArrayList<>();
        String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

        try {


           if(isdateselected && iscashSelected && isCustomerSelected){//all three are selected
               transListCur = db.getAllTransactionSummaryWithPaymentCustDate(selectedPayment,selectedCustomerId,selectedDate,userId);
               layout_linear_search.setVisibility(View.VISIBLE);
               paymentType.setVisibility(View.VISIBLE);
               customerName.setVisibility(View.VISIBLE);
               dateValue.setVisibility(View.VISIBLE);
           }else if(isdateselected && isCustomerSelected && !iscashSelected){
               transListCur = db.getAllTransactionSummaryWithCustDate(selectedCustomerId,selectedDate,userId);
               layout_linear_search.setVisibility(View.VISIBLE);
               customerName.setVisibility(View.VISIBLE);
               dateValue.setVisibility(View.VISIBLE);
           }else if(isdateselected && iscashSelected && !isCustomerSelected){

               transListCur = db.getAllTransactionSummaryWithPaymentTypeDate(userId,selectedPayment,selectedDate);
               layout_linear_search.setVisibility(View.VISIBLE);
               paymentType.setVisibility(View.VISIBLE);
               dateValue.setVisibility(View.VISIBLE);
           }else if(isdateselected && !iscashSelected && !isCustomerSelected){

               layout_linear_search.setVisibility(View.VISIBLE);
               dateValue.setVisibility(View.VISIBLE);
               transListCur = db.getAllTransactionSummaryWithDate(selectedDate, userId);
           }else if(iscashSelected && !isdateselected && !isCustomerSelected){
               layout_linear_search.setVisibility(View.VISIBLE);
               paymentType.setVisibility(View.VISIBLE);
               transListCur = db.getAllTransactionSummaryWithPaymentType(selectedPayment,userId);

           }else if(isCustomerSelected && iscashSelected && !isdateselected  ){
               layout_linear_search.setVisibility(View.VISIBLE);
               customerName.setVisibility(View.VISIBLE);
               paymentType.setVisibility(View.VISIBLE);
               transListCur = db.getAllTransactionSummaryWithCustomerPaymentType(selectedCustomerId,selectedPayment,userId);

           } else if(isCustomerSelected && !isdateselected && !iscashSelected ){
               layout_linear_search.setVisibility(View.VISIBLE);
               customerName.setVisibility(View.VISIBLE);
               transListCur = db.getAllTransactionSummaryWithCustomer(selectedCustomerId,userId);

           }else if(!isdateselected && !iscashSelected && !isCustomerSelected){
               transListCur = db.getAllTransactionSummary(userId);
               iscashSelected=false;
               isCustomerSelected=false;
               isdateselected=false;
               layout_linear_search.setVisibility(View.GONE);
               paymentType.setVisibility(View.GONE);
               customerName.setVisibility(View.GONE);
               dateValue.setVisibility(View.GONE);
           }else{
               transListCur = db.getAllTransactionSummary(userId);
               iscashSelected=false;
               isCustomerSelected=false;
               isdateselected=false;
               layout_linear_search.setVisibility(View.GONE);
               paymentType.setVisibility(View.GONE);
               customerName.setVisibility(View.GONE);
               dateValue.setVisibility(View.GONE);
           }

            while (transListCur.moveToNext()) {
                String transId = transListCur.getString(transListCur.getColumnIndex((ConstantsUsed.COLUMN_TRANSACTION_ID)));
                String transDate = transListCur.getString(transListCur.getColumnIndex((ConstantsUsed.COLUMN_TRANSACTION_DATE)));
                String paymentType = transListCur.getString(transListCur.getColumnIndex((ConstantsUsed.COLUMN_PAYMENT_TYPE)));
                String customerId = transListCur.getString(transListCur.getColumnIndex((ConstantsUsed.COLUMN_CUSTOMER_ID)));
                double transItem = transListCur.getDouble(transListCur.getColumnIndex((ConstantsUsed.COLUMN_TOTAL_ITEM_COUNT)));
                double transTotal = transListCur.getDouble(transListCur.getColumnIndex((ConstantsUsed.COLUMN_TRANSACTION_TOTAL_AMOUNT)));
                transList.add(new TransactionSummary(transId,transDate,paymentType,transItem,transTotal,customerId));
            }

            TransactionSummary.setTransactionList(transList);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        } finally {
            if (transListCur != null)
                transListCur.close();
            // specify an adapter (see also next example)
            if (TransactionSummary.getTransList().isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyLL.setVisibility(View.VISIBLE);
            } else {
                emptyLL.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                mAdapter = new TransactionReceyclerAdapter(this, TransactionSummary.getTransList());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                btnTotalAmount.setText("Total:" + AppFeatures.format(displayTotalAmount(TransactionSummary.getTransList())));
            }
        }


    }


    DatePickerDialog datePickerDialog;
    Calendar cldr;
    int day=0;
    int month=0;
    int year=0;
    public void showDatePicker(){

        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String monthString = String.valueOf(month+1);
                        if (monthString.length() == 1) {
                            monthString = "0" + monthString;
                        }

                        String dateString = String.valueOf(day);
                        if (dateString.length() == 1) {
                            dateString = "0" + dateString;
                        }
                        btnDatePicker.setText(String.valueOf(year).substring(2)+"/"+String.valueOf(monthString)+"/"+String.valueOf(dateString));
                        isdateselected=true;
                        selectedDate=String.valueOf(year)+"-"+String.valueOf(monthString)+"-"+String.valueOf(dateString);
                        dateValue.setText(selectedDate);
                        populateData(selectedDate,"","");

                    }
                }, year, month, day);
        datePickerDialog.show();
    }


    public void showDialogCustomer(View v){
        final HashMap<String,String> map=new HashMap<>();
        final ArrayList<String>list=new ArrayList<>();
        Cursor infoCur = null;
        String custName="";
        String custId="";
        double amount=0.0;
        try {
            String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

            infoCur = db.getAllCustomer(userId);
            while (infoCur.moveToNext()) {
                custName = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_CUSTOMER_NAME)));
                custId = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_CUSTOMER_ID)));
                map.put(custName,custId);
            }
        }catch (Exception e){
            // Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }finally {
            if(!map.isEmpty()) {
                for (String key : map.keySet()) {
                    list.add(key);
                }

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(TransHistoryActivity.this);
                builderSingle.setIcon(android.R.drawable.ic_menu_add);
                builderSingle.setTitle("Select A Customer: ");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TransHistoryActivity.this,
                        android.R.layout.select_dialog_singlechoice,list);


                builderSingle.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isCustomerSelected=true;
                        selectedCustomerId=map.get(list.get(which));
                        customerName.setText(list.get(which));
                        populateData("","",selectedCustomerId);

                    }
                });
                builderSingle.show();

            }else{
                Validator.showToast(this,"No customer available yet");
            }
        }



    }

    public void showDialog(View v){


        final ArrayList<String> list=new ArrayList<>();
        list.add("ALL");
        list.add("CASH");
        list.add("CHEQUE");
        list.add("DEBIT");
        list.add("CREDIT");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(TransHistoryActivity.this);
        builderSingle.setIcon(android.R.drawable.ic_menu_add);
        builderSingle.setTitle("SELECT CATEGORY : ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TransHistoryActivity.this,
                android.R.layout.select_dialog_singlechoice,list);


        builderSingle.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0) {
                    iscashSelected=false;
                    isdateselected=false;
                    isCustomerSelected=false;
                    populateData("", "", "");
                }else {
                    iscashSelected=true;
                    selectedPayment=list.get(which);
                    paymentType.setText(list.get(which));
                    populateData(selectedDate, selectedPayment, selectedCustomerId);
                }
            }
        });
        builderSingle.show();

    }



    private double displayTotalAmount(ArrayList<TransactionSummary> transListSearch){
        total=0.0;
        for(int i=0;i<transListSearch.size();i++){
            total=total+transListSearch.get(i).getTransactionTotalAmount();
        }

        return total;

    }

    public void closeDate(View v){
        isdateselected=false;
        dateValue.setVisibility(View.GONE);
        btnDatePicker.setText("ALL");
        populateData("","","");
    }

    public void closePayment(View v){
        iscashSelected=false;
        paymentType.setVisibility(View.GONE);
        populateData("","","");
    }

    public void closeCustmer(View v){
        isCustomerSelected=false;
        customerName.setVisibility(View.GONE);
        populateData("","","");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }
}
