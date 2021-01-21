package com.retailx.dreamdx.retailx.POJO;

public class ChequeDetails {

    static ChequeDetails obj;

    private ChequeDetails(){

    }

    public static ChequeDetails getInstance(){
        if(obj==null)
            obj=new ChequeDetails();
        return obj;
    }

    public String getChequeId() {
        return chequeId;
    }

    public void setChequeId(String chequeId) {
        this.chequeId = chequeId;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public double getChequeAmount() {
        return chequeAmount;
    }

    public void setChequeAmount(double chequeAmount) {
        this.chequeAmount = chequeAmount;
    }

    public String getChequeBank() {
        return chequeBank;
    }

    public void setChequeBank(String chequeBank) {
        this.chequeBank = chequeBank;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    String chequeId="";
    String chequeNo="";
    double chequeAmount=0.0;
    String chequeBank="";
    String chequeDate="";
}
