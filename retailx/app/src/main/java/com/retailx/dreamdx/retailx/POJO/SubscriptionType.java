package com.retailx.dreamdx.retailx.POJO;

public class SubscriptionType {
    private String description="";
    private double price=399.00;
    private String tile="Standard";
    private String discountLabel="";
    private int id=-1;
    private String titleLabel="";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getDiscountLabel() {
        return discountLabel;
    }

    public void setDiscountLabel(String discountLabel) {
        this.discountLabel = discountLabel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(String titleLabel) {
        this.titleLabel = titleLabel;
    }



    public int getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(int selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    private int selectedMonth=1;

    public SubscriptionType(String description, double price, String tile, String discountLabel, int id, String titleLabel) {
        this.description = description;
        this.price = price;
        this.tile = tile;
        this.discountLabel = discountLabel;
        this.id = id;
        this.titleLabel = titleLabel;
    }

    public SubscriptionType(double price, String tile) {
        this.price = price;
        this.tile = tile;
    }

    private SubscriptionType() {

    }


    public static SubscriptionType getSubObj() {
        return subObj;
    }

    public static void setSubObj(SubscriptionType subObj) {
        SubscriptionType.subObj = subObj;
    }

    static SubscriptionType subObj=null;
    public static SubscriptionType getInstance(){
        if(subObj==null)
            subObj=new SubscriptionType();

        return subObj;
    }

}
