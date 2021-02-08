package com.retailx.dreamdx.retailx.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

public class DBHelper extends SQLiteOpenHelper {

    Context con;

    public DBHelper(Context context){
        super(context, ConstantsUsed.DATABASE_NAME,null,3);
        con=context;
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_PRODUCT_DETAILS+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.COLUMN_PRODUCT_ID+" text,"
                        +ConstantsUsed.COLUMN_PRODUCT_TITLE+" text,"
                        +ConstantsUsed.COLUMN_PRODUCT_DESCRIPTION+" text,"
                        +ConstantsUsed.COLUMN_PRODUCT_CAT_ID+" text,"
                        +ConstantsUsed.COLUMN_PRODUCT_UNIT_OF_MEASURE+" text,"
                        +ConstantsUsed.COLUMN_PRODUCT_SERIAL_CODE+" text,"
                        +ConstantsUsed.COLUMN_PRODUCT_BRAND_CODE+" text,"
                        +ConstantsUsed.COLUMN_PRODUCT_SUPPLIER_CODE+" text,"
                        +ConstantsUsed.COLUMN_PRODUCT_BUYING_PRICE+" double,"
                        +ConstantsUsed.COLUMN_PRODUCT_SELLING_PRICE+" double,"
                        +ConstantsUsed.COLUMN_PRODUCT_CREATED_DATE+" text,"
                        +ConstantsUsed.COLUMN_PRODUCT_IMAGE_PATH+" text,"
                        +ConstantsUsed.COLUMN_PRODUCT_ACTIVE_STATUS+" int,"
                        +ConstantsUsed.COLUMN_PRODUCT_STOCK_MINIMUM+" double,"
                        +ConstantsUsed.COLUMN_PRODUCT_STOCK_IN_HAND+" double,"
                        +ConstantsUsed.COLUMN_PRODUCT_IMAGE_DB_STATUS+" int,"//1 not uploaded, 0 after uploaded
                        +ConstantsUsed.DB_STATUS+" int)"//1 not uploaded, 0 after uploaded
        );

        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_CATEGORY_DETAILS+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.COLUMN_CAT_ID+" text,"
                        +ConstantsUsed.COLUMN_CAT_TITLE+" text,"
                        +ConstantsUsed.COLUMN_CAT_DESCRIPTION+" text,"
                        +ConstantsUsed.COLUMN_CAT_DATE+" text,"
                        +ConstantsUsed.COLUMN_CATEGORY_ACTIVE_STATUS+" int,"
                        +ConstantsUsed.COLUMN_CAT_DB_STATUS+" int)"
        );


        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_USER+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_EMAIL+" text,"
                        +ConstantsUsed.COLUMN_USER_PHONE_NUMBER+" text,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.COLUMN_USER_CLIENT_ID+" text,"
                        +ConstantsUsed.COLUMN_PASSWORD+" text,"
                        +ConstantsUsed.VIEW_ALL_TAX+" text,"
                        +ConstantsUsed.CREATE_EDIT_PRODUCT+" text,"
                        +ConstantsUsed.MANAGE_STOCK+" text,"
                        +ConstantsUsed.CREATE_DISCOUNT+" text,"
                        +ConstantsUsed.IS_ADMIN+" int,"
                        +ConstantsUsed.DB_STATUS+" int)"
        );


        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.COLUMN_CUSTOMER_ID+" text,"
                        +ConstantsUsed.COLUMN_TRANSACTION_ID+" text,"
                        +ConstantsUsed.COLUMN_TRANSACTION_DATE+" text,"
                        +ConstantsUsed.COLUMN_TOTAL_ITEM_COUNT+" double,"//0 for expense
                        +ConstantsUsed.COLUMN_TRANSACTION_TOTAL_AMOUNT+" double,"
                        +ConstantsUsed.COLUMN_TAX_DOUBLE+" double,"//0 for expense
                        +ConstantsUsed.COLUMN_TOTAL_DISCOUNT+" double,"//0 for expense
                        +ConstantsUsed.COLUMN_TOTAL_AMOUNT+" double,"//same as transaction_total_amount-need to change
                        +ConstantsUsed.COLUMN_TRANSACTION_TYPE+" int,"//if 1- income ,if 0-expense
                        +ConstantsUsed.COLUMN_PAYMENT_TYPE+" text,"//default cash for expense
                        +ConstantsUsed.COLUMN_SAVE_ORDER_FLAG+" int,"//1- save,0-transaction
                        +ConstantsUsed.DB_STATUS+" int)"

        );


        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_TRANSACTION_DETAILS+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_TRANSACTION_ID+" text,"
                        +ConstantsUsed.COLUMN_TRANSACTION_DATE+" text,"
                        +ConstantsUsed.COLUMN_PRODUCT_ID+" text,"
                        +ConstantsUsed.COLUMN_TOTAL_ITEM_UNITS+" double,"
                        +ConstantsUsed.COLUMN_TOTAL_ITEM_AMOUNT+" double,"
                        +ConstantsUsed.COLUMN_TOTAL_BUYING_AMOUNT+" double,"
                        +ConstantsUsed.COLUMN_TOTAL_ITEM_TAX+" double,"
                        +ConstantsUsed.DB_STATUS+" int)"
        );


        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_CUSTOMER+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.COLUMN_CUSTOMER_ID+" text,"
                        +ConstantsUsed.COLUMN_CUSTOMER_NAME+" text,"
                        +ConstantsUsed.COLUMN_CUSTOMER_NUMBER+" text,"
                        +ConstantsUsed.COLUMN_CUSTOMER_GENDER+" text,"
                        +ConstantsUsed.COLUMN_CUSTOMER_EMAIL+" text,"
                        +ConstantsUsed.COLUMN_IS_VAT+" text,"
                        +ConstantsUsed.DB_STATUS+" int)"
        );

        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_SUPPLLIER+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.COLUMN_SUPPLIER_ID+" text,"
                        +ConstantsUsed.COLUMN_SUPPLIER_NAME+" text,"
                        +ConstantsUsed.COLUMN_TOTAL_AMOUNT+" double,"
                        +ConstantsUsed.COLUMN_SUPPLIER_NUMBER+" text)"

        );


        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_BUSSINESS_INFO+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.COLUMN_BUSSINESS_ID+" text,"
                        +ConstantsUsed.COLUMN_BUSSINESS_NAME+" text,"
                        +ConstantsUsed.COLUMN_BUSSINESS_NUMBER+" text,"
                        +ConstantsUsed.COLUMN_BUSSINESS_ADDRESS+" text,"
                        +ConstantsUsed.COLUMN_BUSSINESS_LOGO+" text,"
                        +ConstantsUsed.DB_STATUS+" int)"
        );

        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_REVENUE_SUMMARY +
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_DATE+" text,"
                        +ConstantsUsed.COLUMN_OPENING_BAL+" double,"
                        +ConstantsUsed.COLUMN_REVENUE_ID+" text,"
                        +ConstantsUsed.COLUMN_TOTAL_EXPENSE+" double,"
                        +ConstantsUsed.COLUMN_TOTAL_SALE+" double,"
                        +ConstantsUsed.COLUMN_TOTAL_REVENUE+" double,"
                        +ConstantsUsed.DB_STATUS+" int)"
        );

        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_CHEQUE+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.COLUMN_CHEQUE_ID+" text,"
                        +ConstantsUsed.COLUMN_TRANSACTION_ID+" text,"
                        +ConstantsUsed.COULMN_CHEQUE_NO+" text,"
                        +ConstantsUsed.COLUMN_CHEQUE_AMOUNT +" double,"
                        +ConstantsUsed.COLUMN_CHEQUE_BANK +" text,"
                        +ConstantsUsed.COLUMN_CHEQUE_DATE +" text,"
                       // +ConstantsUsed.COLUMN_CHEQUE_COLLECTED_DATE +" text,"
                        +ConstantsUsed.COLUMN_CHEQUE_STATUS +" text,"//1-pending,2-realised,3-bounced
                        +ConstantsUsed.DB_STATUS+" int)"
        );


        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_CREDIT+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.COLUMN_CREDIT_ID+" text,"
                        +ConstantsUsed.COLUMN_TRANSACTION_ID+" text,"
                        +ConstantsUsed.COLUMN_CREDIT_STATUS +" text,"//1-pending,2-paid
                        +ConstantsUsed.DB_STATUS+" int)"
        );


        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_ERROR_REPORT+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.BRAND+" text,"
                        +ConstantsUsed.MODEL+" text,"
                        +ConstantsUsed.DEVICE+" text,"
                        +ConstantsUsed.PHONE_ID+" text,"
                        +ConstantsUsed.PHONE_PRODUCT+" text,"
                        +ConstantsUsed.SDK+" text,"
                        +ConstantsUsed.RELEASE+" text,"
                        +ConstantsUsed.INCREMENTAL+" text,"
                        +ConstantsUsed.DESCRIPTION+" text,"
                        +ConstantsUsed.DB_STATUS+" int)"
        );

        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_SUB_DETAILS+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.SUB_TYPE+" text,"
                        +ConstantsUsed.SUB_VALUE+" text)"
        );

        db.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_USER_DETAILS+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.COLUMN_USER_PHONE_NUMBER+" text,"
                        +ConstantsUsed.COLUMN_PASSWORD+" text,"
                        +ConstantsUsed.COLUMN_USER_EMAIL+" text)"

        );

    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(
                        "create table if not exists "+ConstantsUsed.TABLE_PRODUCT_DETAILS+
                                "(id integer primary key,"
                                +ConstantsUsed.COLUMN_USER_ID+" text,"
                                +ConstantsUsed.COLUMN_PRODUCT_ID+" text,"
                                +ConstantsUsed.COLUMN_PRODUCT_TITLE+" text,"
                                +ConstantsUsed.COLUMN_PRODUCT_DESCRIPTION+" text,"
                                +ConstantsUsed.COLUMN_PRODUCT_CAT_ID+" text,"
                                +ConstantsUsed.COLUMN_PRODUCT_UNIT_OF_MEASURE+" text,"
                                +ConstantsUsed.COLUMN_PRODUCT_SERIAL_CODE+" text,"
                                +ConstantsUsed.COLUMN_PRODUCT_BRAND_CODE+" text,"
                                +ConstantsUsed.COLUMN_PRODUCT_SUPPLIER_CODE+" text,"
                                +ConstantsUsed.COLUMN_PRODUCT_BUYING_PRICE+" double,"
                                +ConstantsUsed.COLUMN_PRODUCT_SELLING_PRICE+" double,"
                                +ConstantsUsed.COLUMN_PRODUCT_CREATED_DATE+" text,"
                                +ConstantsUsed.COLUMN_PRODUCT_IMAGE_PATH+" text,"
                                +ConstantsUsed.COLUMN_PRODUCT_ACTIVE_STATUS+" int,"
                                +ConstantsUsed.COLUMN_PRODUCT_STOCK_MINIMUM+" double,"
                                +ConstantsUsed.COLUMN_PRODUCT_STOCK_IN_HAND+" double,"
                                +ConstantsUsed.COLUMN_PRODUCT_IMAGE_DB_STATUS+" int,"//1 not uploaded, 0 after uploaded
                                +ConstantsUsed.DB_STATUS+" int)"//1 not uploaded, 0 after uploaded
                );

        database.execSQL(
                        "create table if not exists "+ConstantsUsed.TABLE_CATEGORY_DETAILS+
                                "(id integer primary key,"
                                +ConstantsUsed.COLUMN_USER_ID+" text,"
                                +ConstantsUsed.COLUMN_CAT_ID+" text,"
                                +ConstantsUsed.COLUMN_CAT_TITLE+" text,"
                                +ConstantsUsed.COLUMN_CAT_DESCRIPTION+" text,"
                                +ConstantsUsed.COLUMN_CAT_DATE+" text,"
                                +ConstantsUsed.COLUMN_CATEGORY_ACTIVE_STATUS+" int,"
                                +ConstantsUsed.COLUMN_CAT_DB_STATUS+" int)"
                );


        database.execSQL(
                        "create table if not exists "+ConstantsUsed.TABLE_USER+
                                "(id integer primary key,"
                                +ConstantsUsed.COLUMN_USER_EMAIL+" text,"
                                +ConstantsUsed.COLUMN_USER_PHONE_NUMBER+" text,"
                                +ConstantsUsed.COLUMN_USER_ID+" text,"
                                +ConstantsUsed.COLUMN_USER_CLIENT_ID+" text,"
                                +ConstantsUsed.COLUMN_PASSWORD+" text,"
                                +ConstantsUsed.VIEW_ALL_TAX+" text,"
                                +ConstantsUsed.CREATE_EDIT_PRODUCT+" text,"
                                +ConstantsUsed.MANAGE_STOCK+" text,"
                                +ConstantsUsed.CREATE_DISCOUNT+" text,"
                                +ConstantsUsed.IS_ADMIN+" int,"
                                +ConstantsUsed.DB_STATUS+" int)"
                );


        database.execSQL(
                        "create table if not exists "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+
                                "(id integer primary key,"
                                +ConstantsUsed.COLUMN_USER_ID+" text,"
                                +ConstantsUsed.COLUMN_CUSTOMER_ID+" text,"
                                +ConstantsUsed.COLUMN_TRANSACTION_ID+" text,"
                                +ConstantsUsed.COLUMN_TRANSACTION_DATE+" text,"
                                +ConstantsUsed.COLUMN_TOTAL_ITEM_COUNT+" double,"//0 for expense
                                +ConstantsUsed.COLUMN_TRANSACTION_TOTAL_AMOUNT+" double,"
                                +ConstantsUsed.COLUMN_TAX_DOUBLE+" double,"//0 for expense
                                +ConstantsUsed.COLUMN_TOTAL_DISCOUNT+" double,"//0 for expense
                                +ConstantsUsed.COLUMN_TOTAL_AMOUNT+" double,"//same as transaction_total_amount-need to change
                                +ConstantsUsed.COLUMN_TRANSACTION_TYPE+" int,"//if 1- income ,if 0-expense
                                +ConstantsUsed.COLUMN_PAYMENT_TYPE+" text,"//default cash for expense
                                +ConstantsUsed.COLUMN_SAVE_ORDER_FLAG+" int,"//1- save,0-transaction
                                +ConstantsUsed.DB_STATUS+" int)"

                );


        database.execSQL(
                        "create table if not exists "+ConstantsUsed.TABLE_TRANSACTION_DETAILS+
                                "(id integer primary key,"
                                +ConstantsUsed.COLUMN_TRANSACTION_ID+" text,"
                                +ConstantsUsed.COLUMN_TRANSACTION_DATE+" text,"
                                +ConstantsUsed.COLUMN_PRODUCT_ID+" text,"
                                +ConstantsUsed.COLUMN_TOTAL_ITEM_UNITS+" double,"
                                +ConstantsUsed.COLUMN_TOTAL_ITEM_AMOUNT+" double,"
                                +ConstantsUsed.COLUMN_TOTAL_BUYING_AMOUNT+" double,"
                                +ConstantsUsed.COLUMN_TOTAL_ITEM_TAX+" double,"
                                +ConstantsUsed.DB_STATUS+" int)"
                );


        database.execSQL(
                        "create table if not exists "+ConstantsUsed.TABLE_CUSTOMER+
                                "(id integer primary key,"
                                +ConstantsUsed.COLUMN_USER_ID+" text,"
                                +ConstantsUsed.COLUMN_CUSTOMER_ID+" text,"
                                +ConstantsUsed.COLUMN_CUSTOMER_NAME+" text,"
                                +ConstantsUsed.COLUMN_CUSTOMER_NUMBER+" text,"
                                +ConstantsUsed.COLUMN_CUSTOMER_GENDER+" text,"
                                +ConstantsUsed.COLUMN_CUSTOMER_EMAIL+" text,"
                                +ConstantsUsed.COLUMN_IS_VAT+" text,"
                                +ConstantsUsed.DB_STATUS+" int)"
                );

        database.execSQL(
                        "create table if not exists "+ConstantsUsed.TABLE_SUPPLLIER+
                                "(id integer primary key,"
                                +ConstantsUsed.COLUMN_USER_ID+" text,"
                                +ConstantsUsed.COLUMN_SUPPLIER_ID+" text,"
                                +ConstantsUsed.COLUMN_SUPPLIER_NAME+" text,"
                                +ConstantsUsed.COLUMN_TOTAL_AMOUNT+" double,"
                                +ConstantsUsed.COLUMN_SUPPLIER_NUMBER+" text)"

                );


        database.execSQL(
                        "create table if not exists "+ConstantsUsed.TABLE_BUSSINESS_INFO+
                                "(id integer primary key,"
                                +ConstantsUsed.COLUMN_USER_ID+" text,"
                                +ConstantsUsed.COLUMN_BUSSINESS_ID+" text,"
                                +ConstantsUsed.COLUMN_BUSSINESS_NAME+" text,"
                                +ConstantsUsed.COLUMN_BUSSINESS_NUMBER+" text,"
                                +ConstantsUsed.COLUMN_BUSSINESS_ADDRESS+" text,"
                                +ConstantsUsed.COLUMN_BUSSINESS_LOGO+" text,"
                                +ConstantsUsed.DB_STATUS+" int)"
                );

        database.execSQL(
                        "create table if not exists "+ConstantsUsed.TABLE_REVENUE_SUMMARY +
                                "(id integer primary key,"
                                +ConstantsUsed.COLUMN_DATE+" text,"
                                +ConstantsUsed.COLUMN_OPENING_BAL+" double,"
                                +ConstantsUsed.COLUMN_REVENUE_ID+" text,"
                                +ConstantsUsed.COLUMN_TOTAL_EXPENSE+" double,"
                                +ConstantsUsed.COLUMN_TOTAL_SALE+" double,"
                                +ConstantsUsed.COLUMN_TOTAL_REVENUE+" double,"
                                +ConstantsUsed.DB_STATUS+" int)"
                );

        database.execSQL(
                        "create table if not exists "+ConstantsUsed.TABLE_CHEQUE+
                                "(id integer primary key,"
                                +ConstantsUsed.COLUMN_USER_ID+" text,"
                                +ConstantsUsed.COLUMN_CHEQUE_ID+" text,"
                                +ConstantsUsed.COLUMN_TRANSACTION_ID+" text,"
                                +ConstantsUsed.COULMN_CHEQUE_NO+" text,"
                                +ConstantsUsed.COLUMN_CHEQUE_AMOUNT +" double,"
                                +ConstantsUsed.COLUMN_CHEQUE_BANK +" text,"
                                +ConstantsUsed.COLUMN_CHEQUE_DATE +" text,"
                                // +ConstantsUsed.COLUMN_CHEQUE_COLLECTED_DATE +" text,"
                                +ConstantsUsed.COLUMN_CHEQUE_STATUS +" text,"//1-pending,2-realised,3-bounced
                                +ConstantsUsed.DB_STATUS+" int)"
                );


        database.execSQL(
                        "create table if not exists "+ConstantsUsed.TABLE_CREDIT+
                                "(id integer primary key,"
                                +ConstantsUsed.COLUMN_USER_ID+" text,"
                                +ConstantsUsed.COLUMN_CREDIT_ID+" text,"
                                +ConstantsUsed.COLUMN_TRANSACTION_ID+" text,"
                                +ConstantsUsed.COLUMN_CREDIT_STATUS +" text,"//1-pending,2-paid
                                +ConstantsUsed.DB_STATUS+" int)"
                );


        database.execSQL(
                        "create table if not exists "+ConstantsUsed.TABLE_ERROR_REPORT+
                                "(id integer primary key,"
                                +ConstantsUsed.COLUMN_USER_ID+" text,"
                                +ConstantsUsed.BRAND+" text,"
                                +ConstantsUsed.MODEL+" text,"
                                +ConstantsUsed.DEVICE+" text,"
                                +ConstantsUsed.PHONE_ID+" text,"
                                +ConstantsUsed.PHONE_PRODUCT+" text,"
                                +ConstantsUsed.SDK+" text,"
                                +ConstantsUsed.RELEASE+" text,"
                                +ConstantsUsed.INCREMENTAL+" text,"
                                +ConstantsUsed.DESCRIPTION+" text,"
                                +ConstantsUsed.DB_STATUS+" int)"
                );

        database.execSQL(
                "create table if not exists "+ConstantsUsed.TABLE_SUB_DETAILS+
                        "(id integer primary key,"
                        +ConstantsUsed.COLUMN_USER_ID+" text,"
                        +ConstantsUsed.SUB_TYPE+" text,"
                        +ConstantsUsed.SUB_VALUE+" text)"
        );




    }


    public long insertSubDetails(String userid,String type,
                                   String value
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_USER_ID, userid);
        contentValues.put(ConstantsUsed.SUB_TYPE, type);
        contentValues.put(ConstantsUsed.SUB_VALUE, value);


        return db.insert(ConstantsUsed.TABLE_SUB_DETAILS, null, contentValues);
    }

    public long insertErrorDetails(String userid,String brand,
                                     String model,String device,String phone_id,String phone_product,String sdk,String release,
                                     String incremental,String desc,int db_status
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_USER_ID, userid);
        contentValues.put(ConstantsUsed.BRAND, brand);
        contentValues.put(ConstantsUsed.MODEL, model);
        contentValues.put(ConstantsUsed.DEVICE, device);
        contentValues.put(ConstantsUsed.PHONE_ID, phone_id);
        contentValues.put(ConstantsUsed.PHONE_PRODUCT, phone_product);
        contentValues.put(ConstantsUsed.SDK, sdk);
        contentValues.put(ConstantsUsed.RELEASE, release);
        contentValues.put(ConstantsUsed.INCREMENTAL, incremental);
        contentValues.put(ConstantsUsed.DESCRIPTION, desc);
       contentValues.put(ConstantsUsed.DB_STATUS, db_status);


        return db.insert(ConstantsUsed.TABLE_ERROR_REPORT, null, contentValues);
    }


    public long insertProductDetails(String userid,String productid,
    String title,String desc,String categoryId,String unit,String serialCode,String brandcode,
    String supplierCode,double sellingPrice,double buyingPrice,
    String date,String imagePath, int activeStatus,int image_db_status,int db_status
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_USER_ID, userid);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_ID, productid);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_TITLE, title);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_DESCRIPTION, desc);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_CAT_ID, categoryId);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_UNIT_OF_MEASURE, unit);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_SERIAL_CODE, serialCode);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_BRAND_CODE, brandcode);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_SUPPLIER_CODE, supplierCode);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_SELLING_PRICE, sellingPrice);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_BUYING_PRICE, buyingPrice);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_CREATED_DATE, date);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_IMAGE_PATH, imagePath);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_ACTIVE_STATUS, activeStatus);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_IMAGE_DB_STATUS, image_db_status);
        contentValues.put(ConstantsUsed.DB_STATUS, db_status);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_STOCK_IN_HAND, 0.0);//default value
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_STOCK_MINIMUM, 0.0);//default value



        return db.insert(ConstantsUsed.TABLE_PRODUCT_DETAILS, null, contentValues);
    }


    public long insertChequeDetails(String userId,String chequeId,String chequeNo,
                                     double amount,String bank,String date,String transactionId,String chequeStatus,int db_status
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_USER_ID, userId);
        contentValues.put(ConstantsUsed.COLUMN_CHEQUE_ID, chequeId);
        contentValues.put(ConstantsUsed.COULMN_CHEQUE_NO, chequeNo);
        contentValues.put(ConstantsUsed.COLUMN_CHEQUE_AMOUNT, amount);
        contentValues.put(ConstantsUsed.COLUMN_CHEQUE_DATE, date);
        //contentValues.put(ConstantsUsed.COLUMN_CHEQUE_COLLECTED_DATE, collectedDate);
        contentValues.put(ConstantsUsed.COLUMN_CHEQUE_BANK, bank);
        contentValues.put(ConstantsUsed.COLUMN_TRANSACTION_ID, transactionId);
        contentValues.put(ConstantsUsed.COLUMN_CHEQUE_STATUS, chequeStatus);
        contentValues.put(ConstantsUsed.DB_STATUS, db_status);



        return db.insert(ConstantsUsed.TABLE_CHEQUE, null, contentValues);
    }

    public long insertCreditDetails(String userId,String creditId,String transId,int db_status
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_USER_ID, userId);
        contentValues.put(ConstantsUsed.COLUMN_CREDIT_ID, creditId);
        contentValues.put(ConstantsUsed.COLUMN_TRANSACTION_ID, transId);
        contentValues.put(ConstantsUsed.COLUMN_CREDIT_STATUS, "PENDING");
        contentValues.put(ConstantsUsed.DB_STATUS, db_status);

        return db.insert(ConstantsUsed.TABLE_CREDIT, null, contentValues);
    }

    public long updateProductStockInHand
            (String productId,double stockInHand){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_STOCK_IN_HAND, stockInHand);
        return db.update(ConstantsUsed.TABLE_PRODUCT_DETAILS, contentValues, ConstantsUsed.COLUMN_PRODUCT_ID+" = ?",new String[] { productId });

    }

    public long updateProductStockMinimum(String productId,double stockMinimum){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_STOCK_MINIMUM, stockMinimum);
        return db.update(ConstantsUsed.TABLE_PRODUCT_DETAILS, contentValues, ConstantsUsed.COLUMN_PRODUCT_ID+" = ?",new String[] { productId });
    }

    public long saveOrders(String savedOrderId, byte[] image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_SAVED_ORDER_ID, savedOrderId);
        contentValues.put(ConstantsUsed.COLUMN_SAVED_DATA, image);
        return db.insert(ConstantsUsed.TABLE_SAVED_ORDER, null, contentValues);
    }


    public long updateProductDetails(String userid,String productid,
                                      String title,String desc,String categoryId,String unit,String serialCode,String brandcode,
                                      String supplierCode,double sellingPrice,double buyingPrice,
                                      String date,String imagePath, int activeStatus,int image_db_status,int db_status
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_USER_ID, userid);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_ID, productid);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_TITLE, title);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_DESCRIPTION, desc);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_CAT_ID, categoryId);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_UNIT_OF_MEASURE, unit);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_SERIAL_CODE, serialCode);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_BRAND_CODE, brandcode);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_SUPPLIER_CODE, supplierCode);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_SELLING_PRICE, sellingPrice);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_BUYING_PRICE, buyingPrice);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_CREATED_DATE, date);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_IMAGE_PATH, imagePath);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_ACTIVE_STATUS, activeStatus);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_IMAGE_DB_STATUS, image_db_status);
        contentValues.put(ConstantsUsed.DB_STATUS, db_status);

        return db.update(ConstantsUsed.TABLE_PRODUCT_DETAILS, contentValues, ConstantsUsed.COLUMN_PRODUCT_ID+" = ?",new String[] { productid });

       // return db.update(ConstantsUsed.TABLE_PRODUCT_DETAILS, contentValues, ConstantsUsed.COLUMN_PRODUCT_ID+"="+productid, null);
    }


    public long updateCategoryDetails (String title,String catId,int db_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_CAT_TITLE, title);
        contentValues.put(ConstantsUsed.COLUMN_CAT_DB_STATUS, db_status);

        return db.update(ConstantsUsed.TABLE_CATEGORY_DETAILS, contentValues, ConstantsUsed.COLUMN_CAT_ID+" = ?",new String[] { catId });
    }

    public long updateUserTableDbStatus(int flag,String userName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.DB_STATUS, flag);

        return db.update(ConstantsUsed.TABLE_USER, contentValues, ConstantsUsed.COLUMN_USER_EMAIL+" = ?",new String[] { userName });

    }


    public long updateCategoryTableDbStatus(int flag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_CAT_DB_STATUS, flag);

        return db.update(ConstantsUsed.TABLE_CATEGORY_DETAILS, contentValues, ConstantsUsed.COLUMN_CAT_DB_STATUS+" = ?",new String[] { "1" });

    }

    public long updateProductTableDbStatus(int flag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.DB_STATUS, flag);

        return db.update(ConstantsUsed.TABLE_PRODUCT_DETAILS, contentValues, ConstantsUsed.DB_STATUS+" = ?",new String[] { "1" });

    }

    public long updateProductTableImageDbStatus(String productId,int flag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_IMAGE_DB_STATUS, flag);

        return db.update(ConstantsUsed.TABLE_PRODUCT_DETAILS, contentValues, ConstantsUsed.COLUMN_PRODUCT_ID+" = ?",new String[] { productId});

    }

    public long updateUserPrivilage(String userId,String canViewTrans,String canMnageStock,String canGiveDiscount,String canCreateEdit){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.VIEW_ALL_TAX, canViewTrans);
        contentValues.put(ConstantsUsed.MANAGE_STOCK, canMnageStock);
        contentValues.put(ConstantsUsed.CREATE_DISCOUNT, canGiveDiscount);
        contentValues.put(ConstantsUsed.CREATE_EDIT_PRODUCT, canCreateEdit);

        return db.update(ConstantsUsed.TABLE_USER, contentValues, ConstantsUsed.USER_ID+" = ?",new String[] { userId});

    }


    public long updateCustomerTableDbStatus(int flag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.DB_STATUS, flag);

        return db.update(ConstantsUsed.TABLE_CUSTOMER, contentValues, ConstantsUsed.DB_STATUS+" = ?",new String[] { "1" });

    }

    public long updateCErrorTableDbStatus(int flag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.DB_STATUS, flag);

        return db.update(ConstantsUsed.TABLE_ERROR_REPORT, contentValues, ConstantsUsed.DB_STATUS+" = ?",new String[] { "1" });

    }

    public long updateTransactionDetailsTableDbStatus(int flag,String productId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.DB_STATUS, flag);

        return db.update(ConstantsUsed.TABLE_TRANSACTION_DETAILS, contentValues, ConstantsUsed.DB_STATUS+" = ?",new String[] { "1" });

    }

    public long updateSupplierAmount(String id,double amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_TOTAL_AMOUNT, amount);

        return db.update(ConstantsUsed.TABLE_SUPPLLIER, contentValues, ConstantsUsed.COLUMN_SUPPLIER_ID+" = ?",new String[] { id });

    }

    public long updateTransactionSummaryTableDbStatus(int flag,String productId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.DB_STATUS, flag);

        return db.update(ConstantsUsed.TABLE_TRANSACTION_SUMMARY, contentValues, ConstantsUsed.DB_STATUS+" = ?",new String[] { "1" });

    }


    public boolean deleteRow(String productId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ConstantsUsed.TABLE_PRODUCT_DETAILS, ConstantsUsed.COLUMN_PRODUCT_ID + "=" + productId, null) > 0;
    }

    public long insertCategoryDetails (String userid,String catid,
                                      String title,String desc,
                                      String date, int activeStatus,int db_status
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_USER_ID, userid);
        contentValues.put(ConstantsUsed.COLUMN_CAT_ID, catid);
        contentValues.put(ConstantsUsed.COLUMN_CAT_TITLE, title);
        contentValues.put(ConstantsUsed.COLUMN_CAT_DESCRIPTION, desc);
        contentValues.put(ConstantsUsed.COLUMN_CAT_DATE, date);
        contentValues.put(ConstantsUsed.COLUMN_CATEGORY_ACTIVE_STATUS, activeStatus);
        contentValues.put(ConstantsUsed.COLUMN_CAT_DB_STATUS, db_status);

        return db.insert(ConstantsUsed.TABLE_CATEGORY_DETAILS, null, contentValues);
    }




    public long insertTransactionSummary(String userId,String custId,String transId,double totalItemCount,String date,
                                       double transactionTotalAmount,double taxTotal,
                                       double totalAmount,double discount, String paymentType,int transType,
                                         int saveOrderFlag,int db_status
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_USER_ID, userId);
        contentValues.put(ConstantsUsed.COLUMN_CUSTOMER_ID, custId);
        contentValues.put(ConstantsUsed.COLUMN_TRANSACTION_ID, transId);
        contentValues.put(ConstantsUsed.COLUMN_TOTAL_ITEM_COUNT, totalItemCount);
        contentValues.put(ConstantsUsed.COLUMN_TAX_DOUBLE, taxTotal);
        contentValues.put(ConstantsUsed.COLUMN_TOTAL_AMOUNT, totalAmount);
        contentValues.put(ConstantsUsed.COLUMN_TRANSACTION_DATE, date);
        contentValues.put(ConstantsUsed.COLUMN_TRANSACTION_TOTAL_AMOUNT, transactionTotalAmount);
        contentValues.put(ConstantsUsed.COLUMN_TOTAL_DISCOUNT, discount);
        contentValues.put(ConstantsUsed.COLUMN_PAYMENT_TYPE, paymentType);
        contentValues.put(ConstantsUsed.COLUMN_TRANSACTION_TYPE, transType);
        contentValues.put(ConstantsUsed.COLUMN_SAVE_ORDER_FLAG, saveOrderFlag);
        contentValues.put(ConstantsUsed.DB_STATUS, db_status);

        return db.insert(ConstantsUsed.TABLE_TRANSACTION_SUMMARY, null, contentValues);
    }

    public long insertTransactionDetails(String transId,String product_id,double itemCount,
                                         double totalItemAmount,double totalbuyingAmount,String date,double totalItemTax,int db_status
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_TRANSACTION_ID, transId);
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_ID, product_id);
        contentValues.put(ConstantsUsed.COLUMN_TOTAL_ITEM_UNITS, itemCount);
        contentValues.put(ConstantsUsed.COLUMN_TOTAL_ITEM_AMOUNT, totalItemAmount);
        contentValues.put(ConstantsUsed.COLUMN_TOTAL_BUYING_AMOUNT, totalbuyingAmount);
        contentValues.put(ConstantsUsed.COLUMN_TRANSACTION_DATE, date);
        contentValues.put(ConstantsUsed.COLUMN_TOTAL_ITEM_TAX, totalItemTax);
        contentValues.put(ConstantsUsed.DB_STATUS, db_status);



        return db.insert(ConstantsUsed.TABLE_TRANSACTION_DETAILS, null, contentValues);
    }

    public long insertUserDetails(String userName,String phone,String userId,String clientId,String password,int adminFlag,int db_status
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_USER_EMAIL, userName);
        contentValues.put(ConstantsUsed.COLUMN_USER_PHONE_NUMBER, phone);
        contentValues.put(ConstantsUsed.COLUMN_USER_ID, userId);
        contentValues.put(ConstantsUsed.COLUMN_USER_CLIENT_ID, clientId);
        contentValues.put(ConstantsUsed.COLUMN_PASSWORD, password);
        contentValues.put(ConstantsUsed.VIEW_ALL_TAX, "1");
        contentValues.put(ConstantsUsed.CREATE_EDIT_PRODUCT, "1");
        contentValues.put(ConstantsUsed.MANAGE_STOCK, "1");
        contentValues.put(ConstantsUsed.CREATE_DISCOUNT, "1");
        contentValues.put(ConstantsUsed.IS_ADMIN, adminFlag);
        contentValues.put(ConstantsUsed.DB_STATUS, db_status);

        return db.insert(ConstantsUsed.TABLE_USER, null, contentValues);
    }

    public long insertCustomerDetails (String userId,String id,String name,String number,String email,String gender,String is_vat,int db_status
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_USER_ID, userId);
        contentValues.put(ConstantsUsed.COLUMN_CUSTOMER_ID, id);
        contentValues.put(ConstantsUsed.COLUMN_CUSTOMER_NAME, name);
        contentValues.put(ConstantsUsed.COLUMN_CUSTOMER_NUMBER, number);
        contentValues.put(ConstantsUsed.COLUMN_CUSTOMER_GENDER, gender);
        contentValues.put(ConstantsUsed.COLUMN_CUSTOMER_EMAIL, email);
        contentValues.put(ConstantsUsed.COLUMN_IS_VAT, is_vat);
        contentValues.put(ConstantsUsed.DB_STATUS, db_status);

        return db.insert(ConstantsUsed.TABLE_CUSTOMER, null, contentValues);
    }

    public long insertSupplierDetails (String userId,double amount,String id,String name,String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_USER_ID, userId);
        contentValues.put(ConstantsUsed.COLUMN_SUPPLIER_ID, id);
        contentValues.put(ConstantsUsed.COLUMN_TOTAL_AMOUNT, amount);
        contentValues.put(ConstantsUsed.COLUMN_SUPPLIER_NAME, name);
        contentValues.put(ConstantsUsed.COLUMN_SUPPLIER_NUMBER, number);

        return db.insert(ConstantsUsed.TABLE_SUPPLLIER, null, contentValues);
    }




    public long insertBussinessInfo (String userId,String id,String name,String number,String address,String imagePath,int db_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_USER_ID, userId);
        contentValues.put(ConstantsUsed.COLUMN_BUSSINESS_ID, id);
        contentValues.put(ConstantsUsed.COLUMN_BUSSINESS_NAME, name);
        contentValues.put(ConstantsUsed.COLUMN_BUSSINESS_ADDRESS, address);
        contentValues.put(ConstantsUsed.COLUMN_BUSSINESS_NUMBER, number);
        contentValues.put(ConstantsUsed.COLUMN_BUSSINESS_LOGO, imagePath);
        contentValues.put(ConstantsUsed.DB_STATUS, db_status);

        return db.insert(ConstantsUsed.TABLE_BUSSINESS_INFO, null, contentValues);
    }

    public long updateBussinessInfo(String id,String name,String number,String address,String imagePath,int flag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_BUSSINESS_NAME, name);
        contentValues.put(ConstantsUsed.COLUMN_BUSSINESS_ADDRESS, address);
        contentValues.put(ConstantsUsed.COLUMN_BUSSINESS_NUMBER, number);
        contentValues.put(ConstantsUsed.COLUMN_BUSSINESS_LOGO, imagePath);
        contentValues.put(ConstantsUsed.DB_STATUS, flag);

        return db.update(ConstantsUsed.TABLE_BUSSINESS_INFO, contentValues, ConstantsUsed.COLUMN_BUSSINESS_ID+" = ?",new String[] { id });

    }

    public long updateChequeInfo(String id,String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_CHEQUE_STATUS, status);


        return db.update(ConstantsUsed.TABLE_CHEQUE, contentValues, ConstantsUsed.COLUMN_CHEQUE_ID+" = ?",new String[] { id });

    }

    public long updateCreditInfo(String id,String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_CREDIT_STATUS, status);


        return db.update(ConstantsUsed.TABLE_CREDIT, contentValues, ConstantsUsed.COLUMN_CREDIT_ID+" = ?",new String[] { id });

    }

    public long updateCustomerDetails (String custId,String name,String number,String email,String gender,String is_vat,int db_status
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_CUSTOMER_NAME, name);
        contentValues.put(ConstantsUsed.COLUMN_CUSTOMER_NUMBER, number);
        contentValues.put(ConstantsUsed.COLUMN_CUSTOMER_GENDER, gender);
        contentValues.put(ConstantsUsed.COLUMN_CUSTOMER_EMAIL, email);
        contentValues.put(ConstantsUsed.COLUMN_IS_VAT, is_vat);
        contentValues.put(ConstantsUsed.DB_STATUS, db_status);

        return db.update(ConstantsUsed.TABLE_CUSTOMER,  contentValues,ConstantsUsed.COLUMN_CUSTOMER_ID+" = ?",new String[] { custId });
    }

    public long insertOpeningBalInfo (String date,Double openingBal,String id,double totExpense,double totSale,double totReveue
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_DATE, date);
        contentValues.put(ConstantsUsed.COLUMN_OPENING_BAL, openingBal);
        contentValues.put(ConstantsUsed.COLUMN_REVENUE_ID, id);
        contentValues.put(ConstantsUsed.COLUMN_TOTAL_EXPENSE, totExpense);
        contentValues.put(ConstantsUsed.COLUMN_TOTAL_SALE, totSale);
        contentValues.put(ConstantsUsed.COLUMN_TOTAL_REVENUE, totReveue);

        return db.insert(ConstantsUsed.TABLE_REVENUE_SUMMARY, null, contentValues);
    }

    public Cursor getUSerDetails(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_USER, null );
        return res;

    }

    public Cursor getSubDetails(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_SUB_DETAILS+" where "+ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'", null );
        return res;

    }

    public Cursor getCategoriesNotUploaded(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_CATEGORY_DETAILS+" where "+ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'"+" and "
                +ConstantsUsed.COLUMN_CAT_DB_STATUS+"='"+"1"+"'", null );
        return res;

    }


    public Cursor getTransactionsSummaryNotUploaded(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "+ConstantsUsed.DB_STATUS+"='"+"1"+"'", null );
        return res;

    }

    public Cursor getUserDetails(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_USER+" where "+ConstantsUsed.USER_ID+"='"+userId+"'", null );
        return res;

    }

    public Cursor getCustomerDetailsNotUploaded(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_CUSTOMER+" where "+ConstantsUsed.DB_STATUS+"='"+"1"+"'", null );
        return res;

    }


    public Cursor getBussinessDetailsNotUploaded(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_BUSSINESS_INFO+" where "+ConstantsUsed.DB_STATUS+"='"+"1"+"'", null );
        return res;

    }

    public Cursor getErrosDetailsNotUploaded(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_ERROR_REPORT+" where "+ConstantsUsed.DB_STATUS+"='"+"1"+"'", null );

        return res;

    }


    public Cursor getTransactionsDetailsNotUploaded(String transId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_DETAILS+" where "+ConstantsUsed.COLUMN_TRANSACTION_ID+"='"+transId+"'"+" and "
                +ConstantsUsed.DB_STATUS+"='"+"1"+"'"+" and "
                +ConstantsUsed.COLUMN_SAVE_ORDER_FLAG+"='"+"0"+"'", null );
        return res;

    }

    public Cursor getProductsNotUploaded(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_PRODUCT_DETAILS+" where "+ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'"+" and "
                +ConstantsUsed.DB_STATUS+"='"+"1"+"'", null );
        return res;

    }

    public Cursor getImagesNotUploaded(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_PRODUCT_DETAILS+" where "+ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'"+" and "
                +ConstantsUsed.COLUMN_PRODUCT_IMAGE_DB_STATUS+"='"+"1"+"'", null );
        return res;

    }

    public Cursor getStockDetails(String productId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_PRODUCT_DETAILS+" where "+ConstantsUsed.COLUMN_PRODUCT_ID+"='"+productId+"'", null );
        return res;

    }

    public Cursor getBussinessDetails(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_BUSSINESS_INFO+" where "+ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'", null );
        return res;

    }

    public Cursor getAllProductList(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_PRODUCT_DETAILS+" where "+ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'"+" and "+ConstantsUsed.COLUMN_PRODUCT_ACTIVE_STATUS+"='"+"1"+"'", null );
        return res;

    }




    public Cursor getProductList(String catId,String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor id=null;
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_PRODUCT_DETAILS+" where "+ConstantsUsed.COLUMN_PRODUCT_CAT_ID+"='"+catId+"'"+" and "
                +ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'"+" and "+ConstantsUsed.COLUMN_PRODUCT_ACTIVE_STATUS+"='"+"1"+"'", null );

        return res;

    }

    public Cursor getAllSavedOrders(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "+ConstantsUsed.COLUMN_SAVE_ORDER_FLAG+"='"+1+"'"+" and "
                +ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'", null );

        return res;

    }

    public Cursor getAllTransactionSummaryWithPaymentTypeDate(String userId,String paymentType,String date){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "
                +ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'"+" and "
                +ConstantsUsed.COLUMN_PAYMENT_TYPE+"='"+paymentType+"'"+" and "
                +ConstantsUsed.COLUMN_TRANSACTION_DATE+"='"+date+"'", null );
        return res;

    }

    public Cursor getAllTransactionSummary(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "+ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'", null );

        return res;

    }

    public Cursor getAllTransactionSummaryWithDate(String transDate,String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor id=null;
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "+ConstantsUsed.COLUMN_TRANSACTION_DATE+"='"+transDate+"'"+" and "
                +ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'", null );

        return res;

    }

    public Cursor getAllTransactionSummaryWithPaymentType(String type,String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor id=null;
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "+ConstantsUsed.COLUMN_PAYMENT_TYPE+"='"+type+"'"+" and "
                +ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'", null );

        return res;

    }

    public Cursor getAllTransactionSummaryWithPaymentCustDate(String type,String custId,String date,String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor id=null;
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "
                +ConstantsUsed.COLUMN_PAYMENT_TYPE+"='"+type+"'"+" and "
                +ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'"+" and "
                +ConstantsUsed.COLUMN_CUSTOMER_ID+"='"+custId+"'"+" and "
                +ConstantsUsed.COLUMN_TRANSACTION_DATE+"='"+date+"'", null );

        return res;

    }


    public Cursor getAllTransactionSummaryWithCustomerPaymentType(String custId,String type,String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor id=null;
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "
                +ConstantsUsed.COLUMN_PAYMENT_TYPE+"='"+type+"'"+" and "
                +ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'"+" and "
                +ConstantsUsed.COLUMN_CUSTOMER_ID+"='"+custId+"'", null );

        return res;

    }

    public Cursor getAllTransactionSummaryWithCustDate(String custId,String date,String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor id=null;
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "
                +ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'"+" and "
                +ConstantsUsed.COLUMN_CUSTOMER_ID+"='"+custId+"'"+" and "
                +ConstantsUsed.COLUMN_TRANSACTION_DATE+"='"+date+"'", null );

        return res;

    }


   /* public Cursor getAllTransactionSummaryWithPaymentTypeDate(String type,String date,String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor id=null;
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "+ConstantsUsed.COLUMN_PAYMENT_TYPE+"='"+type+"'"+" and "
                +ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'"+" and "
                +ConstantsUsed.COLUMN_TRANSACTION_DATE+"='"+date+"'", null );

        return res;

    }*/

    public Cursor getAllTransactionSummaryWithCustomer(String custId,String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor id=null;
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "+ConstantsUsed.COLUMN_CUSTOMER_ID+"='"+custId+"'"+" and "
                +ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'", null );

        return res;

    }

    public Cursor getAllCustomer(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_CUSTOMER+" where "
                +ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'"+ " order by "+ConstantsUsed.COLUMN_CUSTOMER_NAME+" ASC", null );

        return res;

    }

    public Cursor getAllTransactionDetails(String transId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_DETAILS+" where "+ConstantsUsed.COLUMN_TRANSACTION_ID+"='"+transId+"'", null );

        return res;

    }

    public Cursor getChequesDeatails(String transId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_CHEQUE+" where "+ConstantsUsed.COLUMN_TRANSACTION_ID+"='"+transId+"'", null );

        return res;

    }


    public Cursor getCreditDetails(String transId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_CREDIT+" where "+ConstantsUsed.COLUMN_TRANSACTION_ID+"='"+transId+"'", null );

        return res;

    }

    public Cursor getProductTitleWithId(String productId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_PRODUCT_DETAILS+" where "+ConstantsUsed.COLUMN_PRODUCT_ID+"='"+productId+"'", null );

        return res;

    }

    public Cursor getAllSuppliers(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_SUPPLLIER+" where "
                +ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'", null );

        return res;

    }

    public Cursor getAllUsers(String clientId,int flag){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_USER+" where "
                +ConstantsUsed.CLIENT_ID+"='"+clientId+"'"+" and "
                +ConstantsUsed.IS_ADMIN+"='"+flag+"'", null );

        return res;

    }

    public Cursor getTotalAmountForCustId(String custId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select SUM("+ConstantsUsed.COLUMN_TRANSACTION_TOTAL_AMOUNT+") as Total from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "+ConstantsUsed.COLUMN_CUSTOMER_ID+"='"+custId+"'", null );
        return res;

    }

    public Cursor getProductDetailsWdProductId(String productId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor id=null;
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_PRODUCT_DETAILS+" where "+ConstantsUsed.COLUMN_PRODUCT_ID+"='"+productId+"'", null );

        return res;
    }

    public Cursor getCustomerDetailsWdCustomerId(String custId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor id=null;
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_CUSTOMER+" where "+ConstantsUsed.COLUMN_CUSTOMER_ID+"='"+custId+"'", null );

        return res;
    }


    public Cursor getTransactionDetailsWdTransId(String transId,Context ctx){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_DETAILS+" where "+ConstantsUsed.COLUMN_TRANSACTION_ID+"='"+transId+"'", null );

        return res;
    }

    public long deleteProductWdId(String productId){
        /*SQLiteDatabase db = this.getReadableDatabase();
        String deleteSql = "delete from "+ConstantsUsed.TABLE_PRODUCT_DETAILS+" where " +ConstantsUsed.COLUMN_PRODUCT_ID+"='"+productId+"'";

        db.rawQuery(deleteSql, null).moveToFirst();;*/

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantsUsed.COLUMN_PRODUCT_ACTIVE_STATUS, 0);

        return db.update(ConstantsUsed.TABLE_PRODUCT_DETAILS, contentValues, ConstantsUsed.COLUMN_PRODUCT_ID+" = ?",new String[] { productId});

    }

    public void deleteCustomerWdId(String custId){
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteSql = "delete from "+ConstantsUsed.TABLE_CUSTOMER+" where " +ConstantsUsed.COLUMN_CUSTOMER_ID+"='"+custId+"'";

        db.rawQuery(deleteSql, null).moveToFirst();;
    }

    public void deleteSavedTransactionSummary(String transId){
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteSql = "delete from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where " +ConstantsUsed.COLUMN_TRANSACTION_ID+"='"+transId+"'";

        db.rawQuery(deleteSql, null).moveToFirst();;
    }

    public void deleteSavedTransactionDetails(String transId){
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteSql = "delete from "+ConstantsUsed.TABLE_TRANSACTION_DETAILS+" where " +ConstantsUsed.COLUMN_TRANSACTION_ID+"='"+transId+"'";

        db.rawQuery(deleteSql, null).moveToFirst();;
    }

    public void deleteSupplierWdId(String supId){
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteSql = "delete from "+ConstantsUsed.TABLE_SUPPLLIER+" where " +ConstantsUsed.COLUMN_SUPPLIER_ID+"='"+supId+"'";

        db.rawQuery(deleteSql, null).moveToFirst();;
    }

    public void deleteCategoryWdId(String catId){
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteSql = "delete from "+ConstantsUsed.TABLE_CATEGORY_DETAILS+" where " +ConstantsUsed.COLUMN_CAT_ID+"='"+catId+"'";

        db.rawQuery(deleteSql, null).moveToFirst();;
    }

    public Cursor getSyncStatus(String transId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor id=null;
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "+ConstantsUsed.COLUMN_TRANSACTION_ID+"='"+transId+"'", null );

        return res;
    }

    public void deleteTransactionSummary(String transId){
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteSql = "delete from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where " +ConstantsUsed.COLUMN_TRANSACTION_ID+"='"+transId+"'";
        db.rawQuery(deleteSql, null).moveToFirst();
    }

    public void deleteTransactionDetails(String transId){
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteSql = "delete from "+ConstantsUsed.TABLE_TRANSACTION_DETAILS+" where " +ConstantsUsed.COLUMN_TRANSACTION_ID+"='"+transId+"'";

        db.rawQuery(deleteSql, null).moveToFirst();;
    }

    public Cursor getCatId(String catTitle){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select "+ConstantsUsed.COLUMN_CAT_ID+" from "+ConstantsUsed.TABLE_CATEGORY_DETAILS+" where "+ConstantsUsed.COLUMN_CAT_TITLE+"='"+catTitle+"'", null );
        return res;

    }

    public Cursor getCatTitle(String catId){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select "+ConstantsUsed.COLUMN_CAT_TITLE+" from "+ConstantsUsed.TABLE_CATEGORY_DETAILS+" where "+ConstantsUsed.COLUMN_CAT_ID+"='"+catId+"'", null );
        return res;

    }


    public Cursor getDistinctProductId(String date,Context ctx){

        //Toast.makeText(ctx,date,Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select DISTINCT "+ConstantsUsed.COLUMN_PRODUCT_ID+" from "+ConstantsUsed.TABLE_TRANSACTION_DETAILS+" where "+ConstantsUsed.COLUMN_TRANSACTION_DATE+"='"+date+"'", null );
        //Cursor res = db.query(ConstantsUsed.TABLE_TRANSACTION_DETAILS, new String[] {ConstantsUsed.COLUMN_PRODUCT_ID}, null, null,null,null, null);
        return res;

    }

    public Cursor getSumOfUnitItemsWdProductId(String date, String productId, Context ctx){

        //Toast.makeText(ctx,productId,Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery( "select SUM("+ConstantsUsed.COLUMN_TOTAL_ITEM_UNITS+") as Total from "+ConstantsUsed.TABLE_TRANSACTION_DETAILS+" where "+ConstantsUsed.COLUMN_TRANSACTION_DATE+"='"+date+"'"+" and "
                +ConstantsUsed.COLUMN_PRODUCT_ID+"='"+productId+"'", null );
        //Cursor res = db.query(ConstantsUsed.TABLE_TRANSACTION_DETAILS, new String[] {ConstantsUsed.COLUMN_PRODUCT_ID}, null, null,null,null, null);
        return res;


    }

    public Cursor getSumOfTotalSaleWdDate(String date, Context ctx){

        //Toast.makeText(ctx,productId,Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select SUM("+ConstantsUsed.COLUMN_TOTAL_AMOUNT+") as Total from "+ConstantsUsed.TABLE_TRANSACTION_DETAILS+" where "+ConstantsUsed.COLUMN_TRANSACTION_DATE+"='"+date+"'", null );
        //Cursor res = db.query(ConstantsUsed.TABLE_TRANSACTION_DETAILS, new String[] {ConstantsUsed.COLUMN_PRODUCT_ID}, null, null,null,null, null);
        return res;


    }

    public Cursor getSumOfTotalBuyingPriceDate(String date, Context ctx){

        //Toast.makeText(ctx,productId,Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select SUM("+ConstantsUsed.COLUMN_TOTAL_BUYING_AMOUNT+") as Total from "+ConstantsUsed.TABLE_TRANSACTION_DETAILS+" where "+ConstantsUsed.COLUMN_TRANSACTION_DATE+"='"+date+"'", null );
        //Cursor res = db.query(ConstantsUsed.TABLE_TRANSACTION_DETAILS, new String[] {ConstantsUsed.COLUMN_PRODUCT_ID}, null, null,null,null, null);
        return res;


    }

    public Cursor getLastUpdateOpeningBal(String date, Context ctx){

        //Toast.makeText(ctx,productId,Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_REVENUE_SUMMARY+" where "+ConstantsUsed.COLUMN_DATE+"='"+date+"'", null );
        return res;


    }

    public Cursor getProduct(String barcode){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_PRODUCT_DETAILS+" where "+ConstantsUsed.COLUMN_PRODUCT_SERIAL_CODE+"='"+barcode+"'", null );
        return res;

    }

    public Cursor getAllCategoryList(String userId){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ConstantsUsed.TABLE_CATEGORY_DETAILS+" where "+ConstantsUsed.COLUMN_USER_ID+"='"+userId+"'", null );
        return res;

    }

    public Cursor getLastTransactionDate( ){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select "+ConstantsUsed.COLUMN_TRANSACTION_DATE+" from "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY+" where "+"id = (SELECT MAX(id)  FROM "+ConstantsUsed.TABLE_TRANSACTION_SUMMARY + ")", null );
        return res;

    }


}
