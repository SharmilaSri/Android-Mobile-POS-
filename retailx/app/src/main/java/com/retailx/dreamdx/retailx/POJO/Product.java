package com.retailx.dreamdx.retailx.POJO;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Product {

    private static HashMap<String,ProductDetailsListGrid> mapSelectedProductDetails =new HashMap<String,ProductDetailsListGrid>();
    private static int selectedItemCount=0;
    private static double receivedAmount=0.0;
    private static double Balance=0.0;
    private static double discount=0.0;
    private static double discountRate=0.0;
    private static int unitType=0;
    private double stockInHand=0.0;
    private double stockMinimum=0.0;
    private static double selectedProductTotal=0.00;
    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_UNIT_OF_MEASURE)
    private  double unit_of_measure_count=0.0;

    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_SERIAL_CODE)
    private  String serial_code="";

    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_BRAND_CODE)
    private  String brand_code="";

    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_SUPPLIER_CODE)
    private  String supplier_code="";

    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_BUYING_PRICE)
    private  double unit_buying_price=0.00;

    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_SELLING_PRICE)
    private  double unit_selling_price=0.00;

    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_CREATED_DATE)
    private  String item_created_date="";

    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_ACTIVE_STATUS)
    private  int active_status=0;

    @SerializedName(ConstantsUsed.DB_STATUS)
    private  int db_status=0;

    private String imagePath="NO_IMAGE";

    private static  ArrayList<Product> productList;

    @SerializedName(ConstantsUsed.COLUMN_USER_ID)
    private  String user_id="";

    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_ID)
    private  String product_id="";

    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_TITLE)
    private  String product_title="";

    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_DESCRIPTION)
    private  String description="";

    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_CAT_ID)
    private  String product_cat_id="";

    @SerializedName(ConstantsUsed.COLUMN_PRODUCT_UNIT_OF_MEASURE)
    private  String unit_of_measure="";


    public static void setSelectedList(Product p,Context ctx){

        String proudtcId="";
        proudtcId=p.getProduct_id();
        if(mapSelectedProductDetails==null)
            mapSelectedProductDetails=new HashMap<String,ProductDetailsListGrid>();
        ProductDetailsListGrid existingSameProductDetails=mapSelectedProductDetails.get(p.getProduct_id());
        if(existingSameProductDetails!=null){


            if(p.getUnit_of_measure().equalsIgnoreCase("0")) {//kilo.inch etc
                existingSameProductDetails = mapSelectedProductDetails.get(proudtcId);
                existingSameProductDetails.setMeasureCount(p.getUnit_of_measure_count());
                existingSameProductDetails.setItemCount(p.getUnit_of_measure_count());
                //existingSameProductDetails.setNoOfItem(existingSameProductDetails.getNoOfItem() + 1);
                existingSameProductDetails.setItemTotal(existingSameProductDetails.getMeasureCount() * existingSameProductDetails.getUnitPrice());
                mapSelectedProductDetails.put(proudtcId, existingSameProductDetails);
            }else{
                existingSameProductDetails = mapSelectedProductDetails.get(proudtcId);
                existingSameProductDetails.setNoOfItem(existingSameProductDetails.getNoOfItem() + 1);
                existingSameProductDetails.setItemCount(existingSameProductDetails.getItemCount() + 1.0);
                existingSameProductDetails.setItemTotal(existingSameProductDetails.getItemCount() * existingSameProductDetails.getUnitPrice());
                mapSelectedProductDetails.put(proudtcId, existingSameProductDetails);
            }

        }else{

                if(p.getUnit_of_measure().equalsIgnoreCase("0")) {//kilo.inch etc
                    mapSelectedProductDetails.put(p.getProduct_id(),new ProductDetailsListGrid(1.0,p.getUnit_selling_price(),p.getProduct_title(),p.getUnit_selling_price(),p.getUnit_buying_price(),proudtcId,p.getStockInHand(),p.getStockMinimum(),p.getUnit_of_measure_count(),"0",1,p.getSerial_code()));
                    existingSameProductDetails = mapSelectedProductDetails.get(proudtcId);
                    existingSameProductDetails.setItemCount(existingSameProductDetails.getMeasureCount());
                    existingSameProductDetails.setItemTotal(existingSameProductDetails.getMeasureCount() * existingSameProductDetails.getUnitPrice());
                    mapSelectedProductDetails.put(proudtcId, existingSameProductDetails);
                }else{
                    mapSelectedProductDetails.put(p.getProduct_id(),new ProductDetailsListGrid(1.0,p.getUnit_selling_price(),p.getProduct_title(),p.getUnit_selling_price(),p.getUnit_buying_price(),proudtcId,p.getStockInHand(),p.getStockMinimum(),p.getUnit_of_measure_count(),"1",1,p.getSerial_code()));

                }
        }


    }


    public static int getUnitType() {
        return unitType;
    }

    public static void setUnitType(int unitType) {
        Product.unitType = unitType;
    }



    public static void removeItem(String productId, Context ctx) {

        ProductDetailsListGrid existingSameProductDetails = mapSelectedProductDetails.get(productId);
        if (existingSameProductDetails != null) {

            if (existingSameProductDetails.getItemCount() == 1.0) {
                mapSelectedProductDetails.remove(productId);
            } else {
                existingSameProductDetails.setNoOfItem(existingSameProductDetails.getNoOfItem() - 1);
                existingSameProductDetails.setItemCount(existingSameProductDetails.getItemCount() - 1.0);
                existingSameProductDetails.setItemTotal(existingSameProductDetails.getItemCount() * existingSameProductDetails.getUnitPrice());
                mapSelectedProductDetails.put(productId,existingSameProductDetails);
            }

        } else {
            // Key might be present...
            if (mapSelectedProductDetails.containsKey(productId)) {
                // Okay, there's a key but the value is null
            } else {
                // Definitely no such key
            }
        }

    }

    public static void clearItems() {
        mapSelectedProductDetails.clear();
    }

    public static double getSelectedProductTotal() {
        selectedProductTotal=0.0;
        if(mapSelectedProductDetails!=null) {
            Iterator it = mapSelectedProductDetails.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                double eachTotal = mapSelectedProductDetails.get(pair.getKey()).getItemTotal();
                selectedProductTotal = eachTotal + selectedProductTotal;
                //it.remove(); // avoids a ConcurrentModificationException
            }

        }
        return selectedProductTotal;
    }

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    static SharedPreferences  mPrefs=null;

    private static String serialize(HashMap<String,ProductDetailsListGrid> o) throws IOException {
        String serializedObject = "";

        // serialize the object
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(o);
            so.flush();
            serializedObject = bo.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return serializedObject;
    }

    private static void deserialise(String serializedObject){
        try {
            byte b[] = serializedObject.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            mapSelectedProductDetails = (HashMap<String,ProductDetailsListGrid>) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void saveTempDetails(Context ctx){

        mPrefs = ctx.getSharedPreferences(MY_PREFS_NAME,ctx.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        try{
            prefsEditor.putString("MyObject", serialize(mapSelectedProductDetails));
        }catch(Exception e){

        }

        prefsEditor.commit();

    }



    public static void installTempDetails(Context ctx){

        mPrefs = ctx.getSharedPreferences(MY_PREFS_NAME,ctx.MODE_PRIVATE);

        deserialise(mPrefs.getString("MyObject",""));

    }


    public static int getSelectedItemCount() {
        selectedItemCount=0;
        Iterator it = mapSelectedProductDetails.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int eachCount=mapSelectedProductDetails.get(pair.getKey()).getNoOfItem();

            selectedItemCount=eachCount+selectedItemCount;
        }


        return selectedItemCount;
    }

    public static int getSelectedItemCountWdProductId(String productId) {
        selectedItemCount=0;
        if(mapSelectedProductDetails!=null && mapSelectedProductDetails.size()>0 &&
                mapSelectedProductDetails.get(productId)!=null)
            selectedItemCount=mapSelectedProductDetails.get(productId).getNoOfItem();
        return selectedItemCount;
    }



    public static ArrayList<ProductDetailsListGrid>
    getSelectedListDupliacteRemoved(){
        ArrayList<ProductDetailsListGrid> list=new ArrayList<>();

        Iterator it = mapSelectedProductDetails.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            list.add(mapSelectedProductDetails.get(pair.getKey()));
            //it.remove(); // avoids a ConcurrentModificationException
        }

        return list;
    }

    public static ArrayList<Product> getProductList() {

        return productList;
    }

    public static void setProductList( ArrayList<Product> list) {
        productList=new ArrayList<>();
        productList = list;
    }







    public Product(String user_id, String product_id, String product_title, String description,
                   String product_cat_id, String unit_of_measure, String serial_code, String brand_code,
                   String supplier_code, double unit_buying_price, double unit_selling_price,
                   String item_created_date, int active_status, int product_db_status,String imagePath) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.product_title = product_title;
        this.description = description;
        this.product_cat_id = product_cat_id;
        this.unit_of_measure = unit_of_measure;
        this.serial_code = serial_code;
        this.brand_code = brand_code;
        this.supplier_code = supplier_code;
        this.unit_buying_price = unit_buying_price;
        this.unit_selling_price = unit_selling_price;
        this.item_created_date = item_created_date;
        this.active_status = active_status;
        this.db_status = product_db_status;
        this.imagePath=imagePath;
    }

    public Product(String product_title, double unit_selling_price,double unit_buying_price,String imagePath,String product_id,double stockHand,double stockMin,String unitType,double unit_of_measure_count,String barcode) {

        this.product_title = product_title;
        this.unit_selling_price = unit_selling_price;
        this.unit_buying_price=unit_buying_price;
        this.imagePath=imagePath;
        this.product_id=product_id;
        this.stockInHand=stockHand;
        this.stockMinimum=stockMin;
        this.unit_of_measure=unitType;
        this.unit_of_measure_count=unit_of_measure_count;
        this.serial_code=barcode;
    }
    public static double getReceivedAmount() {
        return receivedAmount;
    }

    public static void setReceivedAmount(double receivedAmount) {
        Product.receivedAmount = receivedAmount;
    }

    public static double getBalance() {
        return Balance;
    }

    public static void setBalance(double balance) {
        Balance = balance;
    }


    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_cat_id() {
        return product_cat_id;
    }

    public void setProduct_cat_id(String product_cat_id) {
        this.product_cat_id = product_cat_id;
    }

    public String getUnit_of_measure() {
        return unit_of_measure;
    }

    public void setUnit_of_measure(String unit_of_measure) {
        this.unit_of_measure = unit_of_measure;
    }

    public String getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(String serial_code) {
        this.serial_code = serial_code;
    }

    public String getBrand_code() {
        return brand_code;
    }

    public void setBrand_code(String brand_code) {
        this.brand_code = brand_code;
    }

    public String getSupplier_code() {
        return supplier_code;
    }

    public void setSupplier_code(String supplier_code) {
        this.supplier_code = supplier_code;
    }

    public double getUnit_buying_price() {
        return unit_buying_price;
    }

    public void setUnit_buying_price(double unit_buying_price) {
        this.unit_buying_price = unit_buying_price;
    }

    public double getUnit_selling_price() {
        return unit_selling_price;
    }

    public void setUnit_selling_price(double unit_selling_price) {
        this.unit_selling_price = unit_selling_price;
    }

    public String getItem_created_date() {
        return item_created_date;
    }

    public void setItem_created_date(String item_created_date) {
        this.item_created_date = item_created_date;
    }

    public int getActive_status() {
        return active_status;
    }

    public void setActive_status(int active_status) {
        this.active_status = active_status;
    }

    public int getProduct_db_status() {
        return db_status;
    }

    public void setProduct_db_status(int product_db_status) {
        this.db_status = product_db_status;
    }


    public double getUnit_of_measure_count() {
        return unit_of_measure_count;
    }

    public void setUnit_of_measure_count(double unit_of_measure_count) {
        this.unit_of_measure_count = unit_of_measure_count;
    }



    public double getStockInHand() {
        return stockInHand;
    }

    public void setStockInHand(double stockInHand) {
        this.stockInHand = stockInHand;
    }

    public double getStockMinimum() {
        return stockMinimum;
    }

    public void setStockMinimum(double stockMinimum) {
        this.stockMinimum = stockMinimum;
    }



    public static double getIncrease() {
        return increase;
    }

    public static void setIncrease(double increa) {
        increase = increase;
    }

    static double increase =0.0;

    public static double getDiscount() {
        return discount;
    }

    public static double getDiscountRate() {
        return discountRate;
    }

    public static void setDiscount(double dis,double disRate) {
        discount = dis;
        discountRate=disRate;
    }





    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {

        this.imagePath = imagePath;
    }

}
