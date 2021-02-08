package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.retailx.dreamdx.retailx.POJO.ListType;
import com.retailx.dreamdx.retailx.Adapters.ListAdapter;
import com.retailx.dreamdx.retailx.POJO.Person;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;

import java.util.ArrayList;

public class ListItemActivity extends AppCompatActivity {

    DBHelper db;
    ArrayList<String> transIdList=new ArrayList<>();
    ArrayList<Person> personList=new ArrayList<>();
    ArrayList<Person> personListSearch=new ArrayList<>();

    Button btn,create;
    public  static int ADD_CUST_REQUEST_CODE=100;
    SearchView serach;
    LinearLayout empty;
    TextView emptyText;
    ListView listview;
    android.support.v7.widget.SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_saved_order_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db=new DBHelper(this);

        btn=findViewById(R.id.id_title);
        empty=findViewById(R.id.empty_show);
        emptyText=findViewById(R.id.empty_list_text);
        listview=findViewById(R.id.saved_order_list);
        searchView= findViewById(R.id.search_list);
        create=findViewById(R.id.id_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(ListType.getInstance().getType().equalsIgnoreCase("CUST")) {
                        moveToAddCustomer();
                    }else  if (ListType.getInstance().getType().equalsIgnoreCase("USER")){
                       moveToAddUser();
                    }


                }
        });

    }




    @Override
    protected void onResume() {
        super.onResume();

        if(ListType.getInstance().getType().equalsIgnoreCase("CUST")){
            getSupportActionBar().setTitle(getResources().getString(R.string.customer_list));
            setUpCustList();

            if(Person.getSelectedCustomer()!=null) {
                btn.setVisibility(View.VISIBLE);
                btn.setText(getResources().getString(R.string.remove_cust));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Person.resetCustomer();
                        startActivityForResult(new Intent(ListItemActivity.this, MainActivity.class), ADD_CUST_REQUEST_CODE);
                    }
                });
            }

            searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    int textLength=newText.length();

                    searchList(textLength,newText);

                    return false;
                }
            });

            // Get the search close button image view
            ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);

            // Set on click listener
            closeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setUpCustList();
                }
            });


        }else if(ListType.getInstance().getType().equalsIgnoreCase("SAVED_ORDER")){
            btn.setVisibility(View.GONE);
            create.setVisibility(View.GONE);
            getSupportActionBar().setTitle("SAVED ORDER");
            setUpList();
        }else if(ListType.getInstance().getType().equalsIgnoreCase("SUP")){
            getSupportActionBar().setTitle("SUPPLIERS");
            setUpListSuppliers();

            searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    int textLength=newText.length();

                    searchList(textLength,newText);

                    return false;
                }
            });

            // Get the search close button image view
            ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);

            // Set on click listener
            closeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setUpListSuppliers();
                }
            });
        }else if (ListType.getInstance().getType().equalsIgnoreCase("USER")){

            getSupportActionBar().setTitle(getResources().getString(R.string.user));
            setUpUserLists();
        }
    }

    private void setUpUserLists(){
        Cursor infoCur = null;
        String email="";
        String phone="";
        String id="";
        personList.clear();
        try {
            String clientId = SharedPreference.getInstance().getValue(this, Constants.CLIENT_ID);

            infoCur = db.getAllUsers(clientId,0);
            while (infoCur.moveToNext()) {
                email = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_USER_EMAIL)));
                phone = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_USER_PHONE_NUMBER)));
                id = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.USER_ID)));
                personList.add(new Person(email,phone,"",id,0.0));
            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }finally {
            if(personList.isEmpty()) {
                listIsEmpty("Oops! No user is available yet...Let's create your first user");
            }else {
                listIsNotEmpty();
                ListType.getInstance().setType("USER");
                ListAdapter adapater = new ListAdapter(this, personList, 1);
                listview.setAdapter(adapater);
            }
        }
    }

    private void searchList(int textLength,String newText){
        String tempTitle="";
        personListSearch.clear();

        if(personList!=null && personList.size()>=1){
            for(int i=0;i<personList.size();i++){
                tempTitle=personList.get(i).getName();
                if(textLength>0 && textLength<=tempTitle.length() && !tempTitle.equalsIgnoreCase("ADD NEW")){
                    if(newText.equalsIgnoreCase(tempTitle.substring(0,textLength)) || tempTitle.contains(newText.toUpperCase())){
                        personListSearch.add(personList.get(i));
                    }
                }else if(textLength==0){
                    if(ListType.getInstance().getType().equalsIgnoreCase("CUST"))
                        setUpCustList();
                    else
                        setUpListSuppliers();

                }


            }

            if(personListSearch.size()>0){

                ListType.getInstance().setType("CUST");
                ListAdapter adapater = new ListAdapter(ListItemActivity.this, personListSearch, 1);
                listview.setAdapter(adapater);
                adapater.notifyDataSetChanged();
            }
        }
    }

    public void setUpListSuppliers(){
        Cursor infoCur = null;
        String custName="";
        String custId="";
        double amount=0.0;
        personList.clear();
        String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);
        try {
            infoCur = db.getAllSuppliers(userId);
            while (infoCur.moveToNext()) {
                custName = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_SUPPLIER_NAME)));
                custId = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_SUPPLIER_ID)));
                amount=infoCur.getDouble(infoCur.getColumnIndex(ConstantsUsed.COLUMN_TOTAL_AMOUNT));

                personList.add(new Person(custName,"","",custId,amount));
            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }finally {
            if(personList.isEmpty()) {
                listIsEmpty("Oops! No Supplier is available yet...Let's create your first Supplier");
            }else {
                listIsNotEmpty();
                ListType.getInstance().setType("SUP");
                ListAdapter adapater = new ListAdapter(this, personList, 1);
                listview.setAdapter(adapater);
            }
        }
    }



    public void setUpCustList(){
        Cursor infoCur = null;
        Cursor custCur = null;
        String custName="";
        String custId="";
        double amount=0.0;
        personList.clear();
        try {
            String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

            infoCur = db.getAllCustomer(userId);
            while (infoCur.moveToNext()) {
                custName = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_CUSTOMER_NAME)));
                custId = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_CUSTOMER_ID)));
                custCur=db.getTotalAmountForCustId(custId);
                while (custCur.moveToNext()) {
                    amount=custCur.getDouble(custCur.getColumnIndex("Total"));
                }
                personList.add(new Person(custName,"","",custId,amount));
            }
        }catch (Exception e){
            // Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }finally {
            if(personList.isEmpty()) {
               listIsEmpty("Oops! No Customer is available yet...Let's create your first customer");
            }else {
                listIsNotEmpty();
                ListType.getInstance().setType("CUST");
                ListAdapter adapater = new ListAdapter(this, personList, 1);
                listview.setAdapter(adapater);
            }
        }

    }

    private void listIsEmpty(String text){
        empty.setVisibility(View.VISIBLE);
        emptyText.setText(text);
        findViewById(R.id.saved_order_list).setVisibility(View.GONE);
        searchView.setVisibility(View.GONE);
    }


    private void listIsNotEmpty(){
        empty.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);
        listview = findViewById(R.id.saved_order_list);
        listview.setVisibility(View.VISIBLE);


    }

    public void setUpList(){
        Cursor infoCur = null;
        String transId="";
        transIdList.clear();
        String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

        try {
            infoCur = db.getAllSavedOrders(userId);
            while (infoCur.moveToNext()) {
                transId = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_TRANSACTION_ID)));
                transIdList.add(transId);
             }
        }catch (Exception e){
            // Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }finally {
            if(transIdList.isEmpty()) {
                listIsEmpty("Oops! No Saved Order is available yet...");
            }else {
                listIsNotEmpty();
                ListType.getInstance().setType("SAVED_ORDER");
                ListAdapter adapater = new ListAdapter(this, transIdList);
                listview.setAdapter(adapater);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_add, menu);
        if(ListType.getInstance().getType().equalsIgnoreCase("SAVED_ORDER")) {

            MenuItem item=menu.findItem(R.id.action_add_people);
            item.setVisible(false);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add_people) {

            if(ListType.getInstance().getType().equalsIgnoreCase("CUST")){
                startActivity(new Intent(ListItemActivity.this,AddCustomerActivity.class));
            }else if (ListType.getInstance().getType().equalsIgnoreCase("SUP")){
                startActivity(new Intent(ListItemActivity.this,AddSupplierActivity.class));
            }else if (ListType.getInstance().getType().equalsIgnoreCase("USER")){
                moveToAddUser();

            }

        }else if  (id == android.R.id.home) {
            navigateBack();
        }
        return true;

    }

    public void moveToAddCustomer(){
        startActivity(new Intent(ListItemActivity.this,AddCustomerActivity.class));

    }

    public void moveToAddUser(){
        //startActivity(new Intent(ListItemActivity.this,SignUpActivity.class));
        Intent intent=new Intent(this,SignUpActivity.class);
        intent.putExtra("IS_USER",true);
        startActivity(intent);
    }

    public void finishThisActivity(){
        Intent data = new Intent(this, MainActivity.class);
        this.setResult(CommonStatusCodes.SUCCESS, data);
        this.finish();
    }


    private void navigateBack(){
        startActivity(new Intent(ListItemActivity.this,MainActivity.class));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateBack();
    }
}
