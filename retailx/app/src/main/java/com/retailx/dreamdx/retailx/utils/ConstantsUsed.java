package com.retailx.dreamdx.retailx.utils;

public class ConstantsUsed {

    public static final int SPLASH_TIME=2000;
    public static final int SERVICE_TIME=100;

    public static final int TYPE_LOGIN =0;
    public static final int TYPE_SIGNUP =1;
    public static final int TYPE_SUB_GROUP =2;
    public static final int TYPE_CREATE_CATEGORY =3;
   // public static final int TYPE_GET_PRODUCT_LIST =4;
    public static final int TYPE_CREATE_INVOICE =5;
    public static final int TYPE_IMAGE =6;
    public static final int TYPE_CREATE_PRODUCT =7;
    public static final int UPLOAD_IMAGE =8;
    public static final int TYPE_GET_CATEGORY =9;
    public static final int TYPE_GET_PRODUCTS =10;
    public static final int TYPE_OTP_SUBMIT =11;
    public static final int TYPE_OTP_RE_SUBMIT =12;
    public static final int TYPE_UPLOAD_CUSTOMER=13;
    public static final int TYPE_BUSSINESS_INFO=14;
    public static final int TYPE_SUBSCRIPTION=15;
    public static final int TYPE_PRIVILAGE=16;
    public static final int TYPE_ERROR=17;
    public static final int TYPE_POST_SUB=18;
    public static final int TYPE_SUBMIT=19;
    public static final int TYPE_RESET_PW=20;
    public static final int TYPE_GET_BUSSINESS_INFO=21;

    public static final String EDIT_PRODUCT_KEY="key";
    public static final String EDIT_PRODUCT_VALUE="true";

    public static final String PRODUCT_ID_KEY="key";
    public static final String EDIT_CAT_KEY="key";
    public static final String EDIT_CAT_ID="key_id";
    public static final String PRODUCT_ID_VALUE="";

    public static final String FLAG_EDIT_VIEW="flag_edit_view";

    public static final String SUCCESS ="success";
    public static final String OTP_STATUS ="otp_reg_status";//0 not completed,1-process completed
    public static final String OTP_CODE ="otp_code";
    public static final String LOGIN="login";
    public static final String MOBILE="M";
    public static final String PRODUCT="P";
    public static final String CATEGORY="C";
    public static final String TRANSACTION="INV";
 public static final String SALES_RETURN="SAR";
    public static final String TITLE="title";
    public static final String MY_PREFS_NAME="_floating_launcher_data";

    public static final String URL_LOGIN="http://213.136.88.114:8000/retailx-user-backend/user-login";
    public static final String URL_REGISTER="http://213.136.88.114:8000/retailx-user-backend/register-new-client";
    public static final String URL_GET_CATEGORY="http://213.136.88.114:8000/retailx-user-backend/load-category-list";
    public static final String URL_GET_PRODUCTS="http://213.136.88.114:8000/retailx-user-backend/load-product-list";
    public static final String URL_CREATE_BUSSINESS="http://213.136.88.114:8000/retailx-user-backend/update-company-info";

    public static final String URL_CREATE_PRODUCT ="http://213.136.88.114:8000/retailx-user-backend/add-new-product";
    public static final String URL_CREATE_CATEGORY ="http://213.136.88.114:8000/retailx-user-backend/add-new-category";
    public static final String URL_POST_IMAGE="http://213.136.88.114:8000/retailx-user-backend/product-image-upload";
    public static final String URL_TRANSACTION_SUMMARY="http://213.136.88.114:8000/retailx-user-backend/save-order-details";
    public static final String URL_CUSTOMER_UPLOAD="http://213.136.88.114:8000/retailx-user-backend/customer-sync";
    public static final String URL_SUBMIT_OTP="http://213.136.88.114:8000/retailx-user-backend/otp-submit";
    public static final String URL_RE_SUBMIT_OTP="http://213.136.88.114:8000/retailx-user-backend/resend-otp";
    public static final String URL_SUBSCRIPTION="http://213.136.88.114:8000/retailx-user-backend/get-all-subscription-info";
    public static final String URL_PRIVILAGE="http://213.136.88.114:8000/retailx-user-backend/update-credentials";
    public static final String URL_ERROR_REPORT="http://213.136.88.114:8000/retailx-user-backend/report-error";
    public static final String URL_POST_SUBSCRIPTION="http://213.136.88.114:8000/retailx-user-backend/subscribe-new";
    public static final String URL_SUBMIT_="http://213.136.88.114:8000/retailx-user-backend/forget-password";
    public static final String URL_RESET_PW_="http://213.136.88.114:8000/retailx-user-backend/reset-password";
    public static final String URL_GET_BUSSINESS_INFO="http://213.136.88.114:8000/retailx-user-backend/client-information";


    public static  String DATABASE_NAME="retailx_app_db";

    public static  String TABLE_PRODUCT_DETAILS="product_details";

    public static final String COLUMN_USER_ID="user_id";
    public static  final String COLUMN_PRODUCT_ID="product_id";
    public static final String COLUMN_PRODUCT_TITLE="product_title";
    public static final String COLUMN_PRODUCT_DESCRIPTION="description";
    public static final String COLUMN_PRODUCT_CAT_ID="product_cat_id";
    public static final String COLUMN_PRODUCT_UNIT_OF_MEASURE="unit_of_measure";
    public static final String COLUMN_PRODUCT_SERIAL_CODE="serial_code";
    public static final String COLUMN_PRODUCT_BRAND_CODE="brand_code";
    public static final String COLUMN_PRODUCT_SUPPLIER_CODE="supplier_code";
    public static final String COLUMN_PRODUCT_BUYING_PRICE="unit_buying_price";
    public static final String COLUMN_PRODUCT_SELLING_PRICE="unit_selling_price";
    public static final String COLUMN_PRODUCT_CREATED_DATE="created_date";
    public static final String COLUMN_PRODUCT_IMAGE_PATH="image_path";
    public static final String COLUMN_PRODUCT_ACTIVE_STATUS="active_status";
    public static final String DB_STATUS="db_status";
    public static final String COLUMN_PRODUCT_STOCK_IN_HAND="product_db_stock_inhand";
    public static final String COLUMN_PRODUCT_IMAGE_DB_STATUS="product_image_db_status";
    public static final String COLUMN_PRODUCT_STOCK_MINIMUM="product_db_stock_minimum";

    public static  String TABLE_CATEGORY_DETAILS="category_master";

    public static  String COLUMN_CAT_ID="cat_id";
    public static  String COLUMN_MOBILE_ID="mobile_id";
    public static  final String COLUMN_CAT_TITLE="cat_title";
    public static  String COLUMN_CAT_DESCRIPTION="description";
    public static  String COLUMN_CAT_DATE="created_date";
    public static  String COLUMN_CATEGORY_ACTIVE_STATUS="active_status";
    public static  String COLUMN_CAT_DB_STATUS ="category_db_status";

    public static  String COLUMN_PROD_CAT_ID="product_cat_id";
    public static  String COLUMN_PROD_SERIAL_CODE="serial_code";
    public static  String COLUMN_PROD_TITLE="product_title";
    public static  String COLUMN_PROD_DESC="description";
    public static  String COLUMN_PROD_UNIT="unit_of_measure";
    public static  String COLUMN_PROD_BRAND_CODE="brand_code";
    public static  String COLUMN_PROD_SUPPLIER_CODE="supplier_code";
    public static  String COLUMN_PROD_BUYING_PRICE="unit_buying_price";

    public static  String TABLE_TRANSACTION_SUMMARY="transaction_summary";


    public static  String COLUMN_TOTAL_ITEM_COUNT="total_item_count";
    public static  String COLUMN_TRANSACTION_TOTAL_AMOUNT="transaction_total_amount";
    public static  String COLUMN_TAX_DOUBLE="total_tax_amount";
    public static  String COLUMN_TOTAL_AMOUNT="total_amount";
    public static  String COLUMN_TOTAL_DISCOUNT="total_item_discount";
    public static  String COLUMN_PAYMENT_TYPE="payment_type";
    public static  String COLUMN_TRANSACTION_ID="transaction_id";
    public static  String COLUMN_TRANSACTION_DATE="transaction_date";
    public static  String COLUMN_MAX_ITEM_UNITS="maximum_item_units";
    public static  String COLUMN_MAX_ITEM_ID="maximum_item_id";
    public static  String COLUMN_MIN_ITEM_UNITS="minumum_item_units";
    public static  String COLUMN_MIN_ITEM_ID="minumum_item_id";
    public static  String TABLE_TRANSACTION_DETAILS="transaction_details";
    public static  String COLUMN_TRANSACTION_TYPE="transaction_type";
    public static  String COLUMN_SAVE_ORDER_FLAG="save_order_flag";

    public static  String COLUMN_TOTAL_ITEM_UNITS="total_units";
    public static  String COLUMN_TOTAL_ITEM_AMOUNT="total_amount";
    public static  String COLUMN_TOTAL_BUYING_AMOUNT="spent_in_buying";
    public static  String COLUMN_TOTAL_ITEM_TAX="total_tax";
    //public static  String COLUMN_TRANSACTION_ID="transaction_id";
    //public static  final String COLUMN_PRODUCT_ID="product_id";


    public static  String TABLE_USER="user_table";
    public static final String IS_ADMIN="is_admin";


    public static  String COLUMN_USER_NAME="user_name";
    public static  String COLUMN_PW="password";
    public static  String COLUMN_USER_EMAIL="user_email";
    public static  String COLUMN_USER_PHONE_NUMBER="phone_number";
    public static  String COLUMN_USER_USER_ID="user_id";
    public static  String COLUMN_USER_CLIENT_ID="client_id";
    public static  String COLUMN_ADDRESS="user_addreess";

    public static  String TABLE_CUSTOMER="customer_table";
    public static  String COLUMN_CUSTOMER_ID="customer_id";
    public static  String COLUMN_CUSTOMER_NAME="customer_name";
    public static  String COLUMN_CUSTOMER_NUMBER="customer_number";
    public static  String COLUMN_CUSTOMER_GENDER="customer_gender";
    public static  String COLUMN_IS_VAT="customer_is_vat";
    public static  String COLUMN_CUSTOMER_EMAIL="email";

    public static  String TABLE_SUPPLLIER="supplier_table";
    public static  String COLUMN_SUPPLIER_ID="supplier_id";
    public static  String COLUMN_SUPPLIER_NAME="supplier_name";
    public static  String COLUMN_SUPPLIER_NUMBER="supplier_number";

    public static  String TABLE_BUSSINESS_INFO="bussiness_table";
    public static  String COLUMN_BUSSINESS_ID="bussiness_id";
    public static  String COLUMN_BUSSINESS_NAME="bussiness_name";
    public static  String COLUMN_BUSSINESS_NUMBER="bussiness_number";
    public static  String COLUMN_BUSSINESS_ADDRESS="bussiness_address";
    public static  String COLUMN_BUSSINESS_LOGO="bussiness_logo";

    public static final String ALL="ALL";

    public static  String TABLE_REVENUE_SUMMARY ="revenue_summary_table";
    public static  String COLUMN_DATE="date";
    public static  String COLUMN_OPENING_BAL="opening_balance";
    public static  String COLUMN_REVENUE_ID="revenue_id";
    public static  String COLUMN_TOTAL_EXPENSE="total_expense";
    public static  String COLUMN_TOTAL_SALE="total_sale";
    public static  String COLUMN_TOTAL_REVENUE="total_revenue";

    public static  String TABLE_SAVED_ORDER ="saved_order";
    public static  String COLUMN_SAVED_ORDER_ID ="saved_order_id";
    public static  String COLUMN_SAVED_DATA ="saved_order_data";

    public static  String TABLE_USER_DETAILS ="user_details";
    public static  String COLUMN_PASSWORD ="user_password";


    public static  String TABLE_CHEQUE="cheque_table";
    public static  String COLUMN_CHEQUE_ID="cheque_id";
    public static  String COULMN_CHEQUE_NO="cheque_no";
    public static  String COLUMN_CHEQUE_AMOUNT ="cheque_amount";
    public static  String COLUMN_CHEQUE_BANK ="bank_name";
    public static  String COLUMN_CHEQUE_DATE ="cheque_date";
    public static  String COLUMN_CHEQUE_STATUS ="cheque_status";

   public static  String TABLE_CREDIT="credit_table";
   public static  String COLUMN_CREDIT_ID="credit_id";
   public static  String COLUMN_CREDIT_STATUS="credit_status";


    public static  String TABLE_ERROR_REPORT="error_table";
    public static  String BRAND="brand";
    public static  String MODEL="model";
    public static  String PHONE_ID="phone_id";
    public static  String PHONE_PRODUCT="product";
    public static  String SDK="sdk";
    public static  String RELEASE="release";
    public static  String INCREMENTAL="incremental";
    public static  String DEVICE="device";
    public static  String DESCRIPTION="description";

    public static  String TABLE_SUB_DETAILS="subscription_table";
    public static  String SUB_TYPE="sub_type";
    public static  String SUB_VALUE="sub_value";


    public static  String TABLE_SYNC_TABLE="sync_table";
    public static  String SYNC_STATUS="sync_status";
    public static  String SYNC_DATE="sync_date";


    //Login
    public static final String EMAIL_ID="email_id";
    public static final String PASSWORD="password";
    public static final String OTP="otp_code";

    //sign up
    public static final String FIRST_NAME="first_name";
    public static final String LAST_NAME="last_name";
    public static final String CONTACT_ID="contact_id";
    public static final String MOBILE_NO="mobile_no";
    public static final String CLIENT_ID="client_id";
    public static final String USER_ID="user_id";
    public static final String LOCATION_ADDRESS="location_address";
    public static final String DISC_AMOUNT="disc_amt";
    public static final String USER_DATA="user_data";

    //privilage data object
    public static final String PRIVILAGED_DATA="privilage_data";
    public static final String CREATE_DISCOUNT="create_discount";
    public static final String CREATE_EDIT_PRODUCT="create_edit_product";
    public static final String VIEW_ALL_TAX="view_all_tx";
    public static final String MANAGE_STOCK="manage_stock";


    public static final String PAYMENT_TYPE_CASH="cash";
    public static final String PAYMENT_TYPE_DEBIT="debit";
    public static final String PAYMENT_TYPE_CREDIT="credit";
    public static final String PAYMENT_TYPE_CHEQUE="cheque";
    public static final String PAYMENT_TYPE_OTHER="other";
    public static final String PAYMENT_TYPE_SPLIT="split";

    public static final String GENDER_M="male";
    public static final String GENDER_F="female";
    public static final String GENDER_C="company";

    public static final String SUB_ID="subscription_id";


}
