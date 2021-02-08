package com.retailx.dreamdx.retailx.POJO;

public class OpeningBalance {

    static OpeningBalance openingBalanceObj=null;
    private double enteredOpeningBal=0.0;


    public static OpeningBalance getInstance(){
        if(openingBalanceObj==null)
            openingBalanceObj=new OpeningBalance();

        return openingBalanceObj;
    }


    public double getEnteredOpeningBal() {
        return enteredOpeningBal;
    }

    public void setEnteredOpeningBal(double enteredOpeningBal) {
        this.enteredOpeningBal = enteredOpeningBal;
    }


}
