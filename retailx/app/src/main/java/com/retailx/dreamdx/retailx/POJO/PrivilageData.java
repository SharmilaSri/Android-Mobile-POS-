package com.retailx.dreamdx.retailx.POJO;

import com.google.gson.annotations.SerializedName;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

public class PrivilageData {

    @SerializedName(ConstantsUsed.CREATE_DISCOUNT)
    int create_discount=1;

    @SerializedName(ConstantsUsed.CREATE_EDIT_PRODUCT)
    int create_edit_product=1;

    @SerializedName(ConstantsUsed.VIEW_ALL_TAX)
    int view_all_tx=1;

    @SerializedName(ConstantsUsed.MANAGE_STOCK)
    int manage_stock=1;


    @SerializedName(ConstantsUsed.IS_ADMIN)
    String is_admin="1";

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    public int getCreate_discount() {
        return create_discount;
    }

    public void setCreate_discount(int create_discount) {
        this.create_discount = create_discount;
    }

    public int getCreate_edit_product() {
        return create_edit_product;
    }

    public void setCreate_edit_product(int create_edit_product) {
        this.create_edit_product = create_edit_product;
    }

    public int getView_all_tx() {
        return view_all_tx;
    }

    public void setView_all_tx(int view_all_tx) {
        this.view_all_tx = view_all_tx;
    }

    public int getManage_stock() {
        return manage_stock;
    }

    public void setManage_stock(int manage_stock) {
        this.manage_stock = manage_stock;
    }

    public static PrivilageData getData() {
        return data;
    }

    public static void setData(PrivilageData data) {
        PrivilageData.data = data;
    }

    static  PrivilageData data;

    public static void setInstance(PrivilageData dat){
        data=dat;
    }

    public static PrivilageData getInstance(){
        if(data!=null)
            return  data;
        else {
            data=new PrivilageData();
            return data;
        }
    }

}
