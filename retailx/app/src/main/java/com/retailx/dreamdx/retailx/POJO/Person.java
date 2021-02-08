package com.retailx.dreamdx.retailx.POJO;

public class Person {

    public Person(String name, String number, String type, String id,double amount) {
        this.name = name;
        this.number = number;
        this.type = type;
        this.id=id;
        this.amount=amount;
    }

    public Person() {

    }

    String name="";
    String number="";
    String type="CUSTOMER";//SUPPLIER
    double amount=0.0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id="";
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    static Person selectedCustomer=null;

    public static Person getSelectedCustomer(){

        return selectedCustomer;
    }

    public static void  setSelectedCustomer(Person customer){
        if(selectedCustomer==null)
            selectedCustomer=new Person();
        selectedCustomer.setId(customer.getId());
        selectedCustomer.setName(customer.getName());
        selectedCustomer.setNumber(customer.getNumber());
        selectedCustomer.setType(customer.getType());
    }

    public static void resetCustomer(){
        selectedCustomer=null;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}
