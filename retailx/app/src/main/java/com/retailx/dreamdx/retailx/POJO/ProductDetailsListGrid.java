package com.retailx.dreamdx.retailx.POJO;

import android.content.Context;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

public class ProductDetailsListGrid {

    @SerializedName("imageView")
    ImageView imageView=null;

    @SerializedName("imagePath")
    String imagePath="NO_IMAGE";

    @SerializedName("itemCount")//1 for unit measure, 0.987 eg for fraction measure
    double itemCount=0.0;

    int noOfItem=1;

    @SerializedName("itemTotal")
    double itemTotal=0.0;

    @SerializedName("measureCount")
    double measureCount=0.0;

    @SerializedName("title")
    String title="";

    @SerializedName("unitPrice")
    double unitPrice;

    @SerializedName("unitType")
    String unitType;

    @SerializedName("unitOfMeasure")
    String unitOfMeasure="1";

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @SerializedName("barCode")
    String barCode="";

    public double getMeasureCount() {
        return measureCount;
    }

    public void setMeasureCount(double measureCount) {
        this.measureCount = measureCount;
    }


    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public ProductDetailsListGrid(String imagePath, ImageView image,  Context ctx) {
        this.imagePath = imagePath;
        this.imageView = image;
        this.context=ctx;
    }


    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }


    public String getImagePath() {
        return imagePath;
    }

    public ImageView getImageView() {
        return imageView;
    }


    public ProductDetailsListGrid(double itemCount, double itemTotal, String title, double unitPrice,
                                  double unitBuyingPrice,
                                  String productId,double stockHand,double stockMin,double measureCount,
                                  String measureType,int noOfItem,String barcode) {
        this.itemCount = itemCount;
        this.itemTotal = itemTotal;
        this.title = title;
        this.unitPrice = unitPrice;
        this.unit_buying_price = unitBuyingPrice;
        this.productId=productId;
        this.stockInHand=stockHand;
        this.stockMin=stockMin;
        this.measureCount=measureCount;
        this.noOfItem=noOfItem;
        this.unitOfMeasure=measureType;
        this.barCode=barcode;
    }


    public int getNoOfItem() {
        return noOfItem;
    }

    public void setNoOfItem(int noOfItem) {
        this.noOfItem = noOfItem;
    }


    public double getItemCount() {
        return itemCount;
    }

    public void setItemCount(double itemCount) {
        this.itemCount = itemCount;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



    public double getUnit_buying_price() {
        return unit_buying_price;
    }

    public void setUnit_buying_price(double unit_buying_price) {
        this.unit_buying_price = unit_buying_price;
    }

    @SerializedName("unit_buying_price")
    double unit_buying_price;

    @SerializedName("productId")
    String productId="";


    public double getStockInHand() {
        return stockInHand;
    }

    public void setStockInHand(int stockInHand) {
        this.stockInHand = stockInHand;
    }

    public double getStockMin() {
        return stockMin;
    }

    public void setStockMin(double stockMin) {
        this.stockMin = stockMin;
    }

    @SerializedName("stockInHand")
    double stockInHand=0.0;

    @SerializedName("stockMin")
    double stockMin=0.0;

   /* @tran("layout")
    LinearLayout layout;*/

    @SerializedName("context")
    Context context;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }




}
