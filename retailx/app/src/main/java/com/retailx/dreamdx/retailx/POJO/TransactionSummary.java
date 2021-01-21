package com.retailx.dreamdx.retailx.POJO;

import java.util.ArrayList;

public class TransactionSummary {


    private TransactionSummary(){

    }


    private double transactionTotalAmount=0.0;
    private String taxTotal="0.0";
    private double totalAmount=0.0;
    private double totalDiscount =0.0;
    private String transactionId="";
    private String transactionDate="";
    private String receivedAmount="0.0";
    private String transactionBalance="0.0";
    private String paymentType="CASH";
    private String custId="0";
    static TransactionSummary obj;

    public static TransactionSummary getInstance(){

        if(obj==null){
            obj=new TransactionSummary();
        }
        return obj;
    }

    private static ArrayList<TransactionSummary> transList;

    public static void setTransactionList( ArrayList<TransactionSummary> list) {
        transList=new ArrayList<>();
        transList = list;
    }

    public static void addTransactionToList(TransactionSummary transObj){
      if(transList==null)
          transList=new ArrayList<TransactionSummary>();

      transList.add(transObj);
    }

    public static ArrayList<TransactionSummary> getTransList(){
       return  transList;
    }

    public static void Clear(){
        if(transList==null && !transList.isEmpty())
            transList.clear();
    }
    private double totalItemCount=0.0;

    public double getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(Double totalItemCount) {
        this.totalItemCount = totalItemCount;
    }



    public TransactionSummary(String transId, String transDate,
                              String paymentType, double itemCount,double transactionTotalAmount,String custId) {
        this.totalItemCount = itemCount;
        this.paymentType = paymentType;
        this.transactionDate = transDate;
        this.transactionId = transId;
        this.transactionTotalAmount=transactionTotalAmount;
        this.custId=custId;
    }


    public double getTransactionTotalAmount() {
        return transactionTotalAmount;
    }

    public void setTransactionTotalAmount(double transactionTotalAmount) {
        this.transactionTotalAmount = transactionTotalAmount;
    }

    public String getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(String taxTotal) {
        this.taxTotal = taxTotal;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getTransactionBalance() {
        return transactionBalance;
    }

    public void setTransactionBalance(String transactionBalance) {
        this.transactionBalance = transactionBalance;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }



    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }


}
