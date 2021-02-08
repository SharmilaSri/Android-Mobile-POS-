package com.retailx.dreamdx.retailx;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.retailx.dreamdx.retailx.apicalls.ApiCalls;
import com.retailx.dreamdx.retailx.apicalls.VolleyCalls;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.util.HashMap;
import java.util.Map;

public class UserAuthorisationActivity extends AppCompatActivity implements  View.OnClickListener{

    Switch isAdmis,isViewTransaction,isGiveDiscounts,isCreateOrEdit,isManage;
    static String canViewTrans,canManageStock,canCreateEdit,canGiveDiscount="1";
    static String userId="";
    static DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_user_authorisation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.privilage_manager));

        setUpViews();

        displayInViews();
    }


    private void displayInViews(){

        try {
            Bundle extras = getIntent().getExtras();
            db=new DBHelper(this);

            userId = extras.getString(ConstantsUsed.USER_ID, "");


            if (!userId.isEmpty()) {
                Cursor infoCur = null;
                infoCur = db.getUserDetails(userId);
                if(infoCur!=null){
                while ( infoCur.moveToNext()) {
                    canViewTrans = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.VIEW_ALL_TAX)));
                    canManageStock = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.MANAGE_STOCK)));
                    canCreateEdit = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.CREATE_EDIT_PRODUCT)));
                    canGiveDiscount = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.CREATE_DISCOUNT)));

                }

                    if(canViewTrans.equalsIgnoreCase("1") && canManageStock.equalsIgnoreCase("1") && canCreateEdit.equalsIgnoreCase("1")&& canGiveDiscount.equalsIgnoreCase("1")){
                        isAdmis.setChecked(true);
                    }else {
                        if (canViewTrans.equalsIgnoreCase("1")) {
                            isViewTransaction.setChecked(true);
                        }
                        if (canManageStock.equalsIgnoreCase("1")) {
                            isManage.setChecked(true);
                        }
                        if (canCreateEdit.equalsIgnoreCase("1")) {
                            isCreateOrEdit.setChecked(true);
                        }
                        if (canGiveDiscount.equalsIgnoreCase("1")) {
                            isGiveDiscounts.setChecked(true);
                        }
                    }
                }
            }
        }catch (Exception e){

        }finally {

        }
    }

    private void setUpViews(){
        isAdmis=findViewById(R.id.is_admin_switch);
        isViewTransaction=findViewById(R.id.view_transaction_switch);
        isGiveDiscounts=findViewById(R.id.give_discounts_switch);
        isCreateOrEdit=findViewById(R.id.create_edit_products_switch);
        isManage=findViewById(R.id.manage_inventory_switch);
        isAdmis.setOnClickListener(this);
        isViewTransaction.setOnClickListener(this);
        isGiveDiscounts.setOnClickListener(this);
        isCreateOrEdit.setOnClickListener(this);
        isManage.setOnClickListener(this);
        isAdmis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isViewTransaction.setChecked(true);
                    isGiveDiscounts.setChecked(true);
                    isCreateOrEdit.setChecked(true);
                    isManage.setChecked(true);

                    isViewTransaction.setEnabled(false);
                    isGiveDiscounts.setEnabled(false);
                    isCreateOrEdit.setEnabled(false);
                    isManage.setEnabled(false);

                    isViewTransaction.setTextColor(getResources().getColor(R.color.colorTxtHintOptional_madatory));
                    isGiveDiscounts.setTextColor(getResources().getColor(R.color.colorTxtHintOptional_madatory));
                    isCreateOrEdit.setTextColor(getResources().getColor(R.color.colorTxtHintOptional_madatory));
                    isManage.setTextColor(getResources().getColor(R.color.colorTxtHintOptional_madatory));
                }else{
                    isViewTransaction.setChecked(false);
                    isGiveDiscounts.setChecked(false);
                    isCreateOrEdit.setChecked(false);
                    isManage.setChecked(false);

                    isViewTransaction.setEnabled(true);
                    isGiveDiscounts.setEnabled(true);
                    isCreateOrEdit.setEnabled(true);
                    isManage.setEnabled(true);

                    isViewTransaction.setTextColor(getResources().getColor(R.color.colorTxtCommon));
                    isGiveDiscounts.setTextColor(getResources().getColor(R.color.colorTxtCommon));
                    isCreateOrEdit.setTextColor(getResources().getColor(R.color.colorTxtCommon));
                    isManage.setTextColor(getResources().getColor(R.color.colorTxtCommon));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.is_admin_switch:
                //showDescription(1);
                break;
            case R.id.view_transaction_switch:
                //showDescription(2);
                break;
            case R.id.give_discounts_switch:
                //showDescription(3);
                break;
            case R.id.create_edit_products_switch:
                //showDescription(4);
                break;
            case R.id.manage_inventory_switch:
                //showDescription(5);
                break;

            default:
                break;
        }
    }

    public void showDescription(int requestCode){

        Intent des=new Intent(this,UserAuthorisationDescActivity.class);
        startActivityForResult(des,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void submit(View v){
        if(UtilityFunctions.isInternetAvailable() || UtilityFunctions.isNetworkConnected(this)){

            if(isAdmis.isChecked()){
                canViewTrans="1";
                canManageStock="1";
                canCreateEdit="1";
                canGiveDiscount="1";
            }else{
                if(isCreateOrEdit.isChecked())
                    canCreateEdit="1";
                else
                    canCreateEdit="0";
                if(isGiveDiscounts.isChecked())
                    canGiveDiscount="1";
                else
                    canGiveDiscount="0";
                if(isManage.isChecked())
                    canManageStock="1";
                else
                    canManageStock="0";
                if(isViewTransaction.isChecked())
                    canViewTrans="1";
                else
                    canViewTrans="0";
            }



            SharedPreference sharedPreference = SharedPreference.getInstance();

            ApiCalls apicalls=new VolleyCalls();
            Map<String,String> params = new HashMap<String, String>();
            params.put(ConstantsUsed.USER_ID,userId);
            params.put(ConstantsUsed.CLIENT_ID,sharedPreference.getValue(this,ConstantsUsed.CLIENT_ID));
            params.put("discount_cre",canGiveDiscount);
            params.put("view_all_tx",canViewTrans);
            params.put("update_stock",canManageStock);
            params.put("crud_product",canCreateEdit);
            apicalls.connectToNetworkPost(this, ConstantsUsed.URL_PRIVILAGE,ConstantsUsed.TYPE_PRIVILAGE,params);

        }
        else{
            Validator.showToast(this,"NO INTERNET");
        }
    }


    public static void apiCallBackPrivilageSuccess( Context ctx){

        if(!userId.isEmpty())
            db.updateUserPrivilage(userId,canViewTrans,canManageStock,canGiveDiscount,canCreateEdit);

        Intent login = new Intent(ctx, UserAuthorisationDescActivity.class);
        ctx.startActivity(login);
    }
}
