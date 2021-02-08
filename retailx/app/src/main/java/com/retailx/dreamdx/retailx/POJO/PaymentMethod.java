package com.retailx.dreamdx.retailx.POJO;

import java.util.ArrayList;

public class PaymentMethod {

    public static ArrayList<PaymentMethod> getPaymentMethodList() {
        return paymentMethodList;
    }

    public static void setPaymentMethodList(ArrayList<PaymentMethod> paymentList) {
        paymentMethodList = paymentList;
    }

   private static ArrayList<PaymentMethod> paymentMethodList =new ArrayList<>();

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public PaymentMethod(int imageDrawable, String methodName) {
        this.imageDrawable = imageDrawable;
        this.methodName = methodName;
    }

    private PaymentMethod() {

    }

    public static String getSelectedPaymentMethod() {
        return selectedPaymentMethod;
    }

    public static void setSelectedPaymentMethod(String selectedPaymentMethod) {
        PaymentMethod.selectedPaymentMethod = selectedPaymentMethod;
    }

    public  static String selectedPaymentMethod="1";
    static PaymentMethod payObj;

    public static PaymentMethod getInstance(){
        if(payObj==null)
            payObj=new PaymentMethod();
        return payObj;
    }

    private int imageDrawable=0;
    private String methodName="";
}
