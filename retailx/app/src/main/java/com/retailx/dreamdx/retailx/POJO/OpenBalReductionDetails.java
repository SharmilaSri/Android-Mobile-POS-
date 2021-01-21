package com.retailx.dreamdx.retailx.POJO;

import java.util.ArrayList;

public class OpenBalReductionDetails {


    private String description="";

    private double amount=0.0;

    private static ArrayList<OpenBalReductionDetails> openDetList=new ArrayList<>();

    public OpenBalReductionDetails(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public static ArrayList<OpenBalReductionDetails> getOpenDetList() {
        return openDetList;
    }

    public static void setOpenDetList(OpenBalReductionDetails obj) {
        if(!obj.getDescription().isEmpty())
            openDetList.add(obj);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
