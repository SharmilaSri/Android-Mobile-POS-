package com.retailx.dreamdx.retailx.POJO;

public class
Invoice {

    static Invoice invoiceObj;
    public static  Invoice getInstance(){
        if (invoiceObj==null){
            invoiceObj=new Invoice();
            return  invoiceObj;
        }else {
            return  invoiceObj;
        }
    }

    private Invoice(){

    }

    double receivedAmount=0.0;
    double balance=0.0;
    BussinessInfo bussinessInfoObj=null;
    TransactionSummary transactionSummaryObj;
    TransactionDeatils transactionDeatilsObj;


    public TransactionDeatils getTransactionDeatilsObj() {
        return transactionDeatilsObj;
    }

    public void setTransactionDeatilsObj(TransactionDeatils transactionDeatilsObj) {
        this.transactionDeatilsObj = transactionDeatilsObj;
    }



    public BussinessInfo getBussinessInfoObj() {
        return bussinessInfoObj;
    }

    public void setBussinessInfoObj(BussinessInfo bussinessInfoObj) {
        this.bussinessInfoObj = bussinessInfoObj;
    }


    public TransactionSummary getTransactionSummaryObj() {
        return transactionSummaryObj;
    }

    public void setTransactionSummaryObj(TransactionSummary transactionSummaryObj) {
        this.transactionSummaryObj = transactionSummaryObj;
    }


    public double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
