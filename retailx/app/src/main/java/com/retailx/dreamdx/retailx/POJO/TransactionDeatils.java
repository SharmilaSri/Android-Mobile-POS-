package com.retailx.dreamdx.retailx.POJO;

public class TransactionDeatils {

    private String productId="";
    private double totalUnits=0.0;
    private double totalAmount=0.0;
    private double spentAmountBuying=0.0;
    private double tax=0.0;
    private double discount=0.0;
    static TransactionDeatils transDetailsObj;

    private TransactionDeatils(){

    }

    public static  TransactionDeatils getInstance(){
        if(transDetailsObj==null)
            transDetailsObj=new TransactionDeatils();
        return transDetailsObj;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    public double getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(double totalUnits) {
        this.totalUnits = totalUnits;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getSpentAmountBuying() {
        return spentAmountBuying;
    }

    public void setSpentAmountBuying(double spentAmountBuying) {
        this.spentAmountBuying = spentAmountBuying;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }




}
